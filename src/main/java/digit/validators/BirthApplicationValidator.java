package digit.validators;

import digit.repository.BirthRegistrationRepository;
import digit.web.models.BirthApplicationSearchCriteria;
import digit.web.models.BirthRegistrationApplication;
import digit.web.models.BirthRegistrationRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class BirthApplicationValidator {

    @Autowired
    private BirthRegistrationRepository repository;

    /**
     * Validates the birth registration request by checking if tenantId is provided for each application.
     * 
     * @param birthRegistrationRequest The birth registration request to be validated
     * @throws CustomException if tenantId is missing for any application
     */
    public void validateBirthApplication(BirthRegistrationRequest birthRegistrationRequest) {
        birthRegistrationRequest.getBirthRegistrationApplications().forEach(application -> {
            if (ObjectUtils.isEmpty(application.getTenantId())) {
                throw new CustomException("EG_BT_APP_ERR", "tenantId is mandatory for creating birth registration applications");
            }
        });
    }

    /**
     * Validates if the given birth registration application exists in the repository.
     * 
     * @param birthRegistrationApplication The birth registration application to check for existence
     * @return The existing birth registration application if found
     * @throws CustomException if no matching application is found
     */
    public BirthRegistrationApplication validateApplicationExistence(BirthRegistrationApplication birthRegistrationApplication) {
        return repository.getApplications(BirthApplicationSearchCriteria.builder().applicationNumber(birthRegistrationApplication.getApplicationNumber()).build()).get(0);
    }
}
