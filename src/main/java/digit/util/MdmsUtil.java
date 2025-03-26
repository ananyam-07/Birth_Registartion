package digit.util;

import com.jayway.jsonpath.JsonPath;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MdmsUtil {

    @Autowired
    private RestTemplate restTemplate;  // For making HTTP requests to MDMS service

    @Value("${egov.mdms.host}")
    private String mdmsHost;  // MDMS service host URL

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsUrl;  // MDMS search endpoint

    /**
     * Fetches the registration charges from MDMS.
     * 
     * @param requestInfo The request info object
     * @param tenantId The tenant ID for the MDMS query
     * @return Registration charge or null if an error occurs
     */
    public Integer fetchRegistrationChargesFromMdms(RequestInfo requestInfo, String tenantId) {
        StringBuilder uri = new StringBuilder();
        uri.append(mdmsHost).append(mdmsUrl);
        
        // Create MDMS request object
        MdmsCriteriaReq mdmsCriteriaReq = getMdmsRequestForCategoryList(requestInfo, tenantId);
        
        // Send request to MDMS and parse response
        Object response = new HashMap<>();
        Integer rate = 0;
        try {
            response = restTemplate.postForObject(uri.toString(), mdmsCriteriaReq, Map.class);
            // Extract registration charges using JsonPath
            rate = JsonPath.read(response, "$.MdmsRes.BTR.RegistrationCharges.[0].amount");
        } catch (Exception e) {
            return null;  // Return null in case of an exception
        }
        return rate;
    }

    /**
     * Creates an MDMS request object for fetching category list.
     * 
     * @param requestInfo The request info object
     * @param tenantId The tenant ID for the MDMS query
     * @return MdmsCriteriaReq object
     */
    private MdmsCriteriaReq getMdmsRequestForCategoryList(RequestInfo requestInfo, String tenantId) {
        // Create master detail for RegistrationCharges
        MasterDetail masterDetail = new MasterDetail();
        masterDetail.setName("RegistrationCharges");
        
        List<MasterDetail> masterDetailList = new ArrayList<>();
        masterDetailList.add(masterDetail);

        // Create module detail for BTR
        ModuleDetail moduleDetail = new ModuleDetail();
        moduleDetail.setMasterDetails(masterDetailList);
        moduleDetail.setModuleName("BTR");
        
        List<ModuleDetail> moduleDetailList = new ArrayList<>();
        moduleDetailList.add(moduleDetail);

        // Set tenant ID and module details in MDMS criteria
        MdmsCriteria mdmsCriteria = new MdmsCriteria();
        mdmsCriteria.setTenantId(tenantId.split("\\.")[0]);
        mdmsCriteria.setModuleDetails(moduleDetailList);

        // Create MDMS request with criteria and request info
        MdmsCriteriaReq mdmsCriteriaReq = new MdmsCriteriaReq();
        mdmsCriteriaReq.setMdmsCriteria(mdmsCriteria);
        mdmsCriteriaReq.setRequestInfo(requestInfo);

        return mdmsCriteriaReq;
    }
}
