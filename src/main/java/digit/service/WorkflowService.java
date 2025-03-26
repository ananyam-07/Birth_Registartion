package digit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.BTRConfiguration;
import digit.repository.ServiceRequestRepository;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.models.RequestInfoWrapper;
import org.egov.common.contract.request.User;
import org.egov.common.contract.workflow.*;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Component
@Slf4j
public class WorkflowService {

    @Autowired
    private ObjectMapper mapper;  // For JSON parsing

    @Autowired
    private ServiceRequestRepository repository;  // Repository for making external service calls

    @Autowired
    private BTRConfiguration config;  // Configuration for workflow service

    /**
     * Updates the workflow status for each birth registration application.
     * 
     * @param birthRegistrationRequest The request containing birth registration applications
     */
    public void updateWorkflowStatus(BirthRegistrationRequest birthRegistrationRequest) {
        birthRegistrationRequest.getBirthRegistrationApplications().forEach(application -> {
            ProcessInstance processInstance = getProcessInstanceForBTR(application, birthRegistrationRequest.getRequestInfo());
            ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest(birthRegistrationRequest.getRequestInfo(), Collections.singletonList(processInstance));
            callWorkFlow(workflowRequest);  // Call the workflow service
        });
    }

    /**
     * Calls the workflow service to process the given request.
     * 
     * @param workflowReq The workflow request containing process instances
     * @return The state of the process instance
     */
    public State callWorkFlow(ProcessInstanceRequest workflowReq) {
        ProcessInstanceResponse response = null;
        StringBuilder url = new StringBuilder(config.getWfHost().concat(config.getWfTransitionPath()));
        Object optional = repository.fetchResult(url, workflowReq);  // Fetch response from external service
        response = mapper.convertValue(optional, ProcessInstanceResponse.class);  // Convert response to ProcessInstanceResponse
        return response.getProcessInstances().get(0).getState();  // Return the state of the first process instance
    }

    /**
     * Builds the process instance for a birth registration application.
     * 
     * @param application The birth registration application
     * @param requestInfo The request information
     * @return The created process instance
     */
    private ProcessInstance getProcessInstanceForBTR(BirthRegistrationApplication application, RequestInfo requestInfo) {
        Workflow workflow = application.getWorkflow();

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setBusinessId(application.getApplicationNumber());
        processInstance.setAction(workflow.getAction());
        processInstance.setModuleName("birth-services");
        processInstance.setTenantId(application.getTenantId());
        processInstance.setBusinessService("BTR");
        processInstance.setDocuments(workflow.getDocuments());
        processInstance.setComment(workflow.getComments());

        if (!CollectionUtils.isEmpty(workflow.getAssignes())) {
            List<User> users = new ArrayList<>();
            workflow.getAssignes().forEach(uuid -> {
                User user = new User();
                user.setUuid(uuid);
                users.add(user);
            });
            processInstance.setAssignes(users);  // Set the assignees for the process
        }

        return processInstance;
    }

    /**
     * Fetches the current workflow process instance.
     * 
     * @param requestInfo Request information
     * @param tenantId Tenant ID for the request
     * @param businessId The business ID to search for
     * @return The current process instance
     */
    public ProcessInstance getCurrentWorkflow(RequestInfo requestInfo, String tenantId, String businessId) {
        RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        StringBuilder url = getSearchURLWithParams(tenantId, businessId);
        Object res = repository.fetchResult(url, requestInfoWrapper);
        ProcessInstanceResponse response = null;

        try {
            response = mapper.convertValue(res, ProcessInstanceResponse.class);  // Parse the response
        } catch (Exception e) {
            throw new CustomException("PARSING_ERROR", "Failed to parse workflow search response");
        }

        if (response != null && !CollectionUtils.isEmpty(response.getProcessInstances()) && response.getProcessInstances().get(0) != null) {
            return response.getProcessInstances().get(0);  // Return the first process instance if available
        }

        return null;
    }

    /**
     * Fetches the business service for birth registration.
     * 
     * @param application The birth registration application
     * @param requestInfo Request information
     * @return The business service
     */
    private BusinessService getBusinessService(BirthRegistrationApplication application, RequestInfo requestInfo) {
        String tenantId = application.getTenantId();
        StringBuilder url = getSearchURLWithParams(tenantId, "BTR");
        RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        Object result = repository.fetchResult(url, requestInfoWrapper);
        BusinessServiceResponse response = null;

        try {
            response = mapper.convertValue(result, BusinessServiceResponse.class);  // Parse the response
        } catch (IllegalArgumentException e) {
            throw new CustomException("PARSING_ERROR", "Failed to parse response of workflow business service search");
        }

        if (CollectionUtils.isEmpty(response.getBusinessServices())) {
            throw new CustomException("BUSINESSSERVICE_NOT_FOUND", "The businessService BTR is not found");
        }

        return response.getBusinessServices().get(0);
    }

    /**
     * Builds the search URL for fetching process instances.
     * 
     * @param tenantId Tenant ID
     * @param businessService Business service name
     * @return The URL for the search query
     */
    private StringBuilder getSearchURLWithParams(String tenantId, String businessService) {
        StringBuilder url = new StringBuilder(config.getWfHost());
        url.append(config.getWfBusinessServiceSearchPath());
        url.append("?tenantId=");
        url.append(tenantId);
        url.append("&businessServices=");
        url.append(businessService);
        return url;
    }

    /**
     * Prepares the process instance for birth registration payment action.
     * 
     * @param updateRequest The update request containing birth registration details
     * @return The process instance request for payment action
     */
    public ProcessInstanceRequest getProcessInstanceForBirthRegistrationPayment(BirthRegistrationRequest updateRequest) {
        BirthRegistrationApplication application = updateRequest.getBirthRegistrationApplications().get(0);

        ProcessInstance process = ProcessInstance.builder()
                .businessService("BTR")
                .businessId(application.getApplicationNumber())
                .comment("Payment for birth registration processed")
                .moduleName("birth-services")
                .tenantId(application.getTenantId())
                .action("PAY")
                .build();

        return ProcessInstanceRequest.builder()
                .requestInfo(updateRequest.getRequestInfo())
                .processInstances(Arrays.asList(process))
                .build();
    }
}
