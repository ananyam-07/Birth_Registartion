package digit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import static digit.config.ServiceConstants.*;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.common.contract.user.UserDetailResponse;
import org.egov.common.contract.user.enums.UserType;
import digit.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utility class for handling user-related operations such as calling the user service, 
 * parsing responses, and adding default fields to user info.
 */
@Component
public class UserUtil {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private Configuration configs;

    /**
     * Calls the user service and returns UserDetailResponse.
     * 
     * @param userRequest Request object for user service
     * @param uri The URI of the user service endpoint
     * @return Parsed UserDetailResponse from the service response
     */
    public UserDetailResponse userCall(Object userRequest, StringBuilder uri) {
        String dobFormat = determineDateFormat(uri.toString());
        try {
            LinkedHashMap responseMap = (LinkedHashMap) serviceRequestRepository.fetchResult(uri, userRequest);
            parseResponse(responseMap, dobFormat);  // Parses date fields in the response
            return mapper.convertValue(responseMap, UserDetailResponse.class);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE, OBJECTMAPPER_UNABLE_TO_CONVERT);
        }
    }

    /**
     * Determines the date format for date fields based on the URI.
     * 
     * @param uri The URI of the user service endpoint
     * @return The date format as a string
     */
    private String determineDateFormat(String uri) {
        if (uri.contains(configs.getUserSearchEndpoint()) || uri.contains(configs.getUserUpdateEndpoint())) {
            return DOB_FORMAT_Y_M_D;
        } else if (uri.contains(configs.getUserCreateEndpoint())) {
            return DOB_FORMAT_D_M_Y;
        }
        return null;
    }

    /**
     * Parses date strings in the response map into long values.
     * 
     * @param responseMap The response map from the user service
     * @param dobFormat The date format to use for parsing DOB
     */
    public void parseResponse(LinkedHashMap responseMap, String dobFormat) {
        List<LinkedHashMap> users = (List<LinkedHashMap>) responseMap.get(USER);
        String format1 = DOB_FORMAT_D_M_Y_H_M_S;
        if (users != null) {
            users.forEach(map -> {
                map.put(CREATED_DATE, dateTolong((String) map.get(CREATED_DATE), format1));
                if (map.get(LAST_MODIFIED_DATE) != null)
                    map.put(LAST_MODIFIED_DATE, dateTolong((String) map.get(LAST_MODIFIED_DATE), format1));
                if (map.get(DOB) != null)
                    map.put(DOB, dateTolong((String) map.get(DOB), dobFormat));
                if (map.get(PWD_EXPIRY_DATE) != null)
                    map.put(PWD_EXPIRY_DATE, dateTolong((String) map.get(PWD_EXPIRY_DATE), format1));
            });
        }
    }

    /**
     * Converts date string to long (timestamp).
     * 
     * @param date The date string to be converted
     * @param format The format of the date string
     * @return The timestamp as a long value
     */
    private Long dateTolong(String date, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        try {
            Date d = f.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            throw new CustomException(INVALID_DATE_FORMAT_CODE, INVALID_DATE_FORMAT_MESSAGE);
        }
    }

    /**
     * Enriches user info with default fields such as mobile number, tenant ID, and role.
     * 
     * @param mobileNumber The mobile number of the user
     * @param tenantId The tenant ID for the user
     * @param userInfo The user object to be enriched
     */
    public void addUserDefaultFields(String mobileNumber, String tenantId, User userInfo) {
        Role role = getCitizenRole(tenantId);
        userInfo.setMobileNumber(mobileNumber);
        userInfo.setTenantId(getStateLevelTenant(tenantId));  // Extracts the state-level tenant ID
        userInfo.setType("CITIZEN");
    }

    /**
     * Creates a citizen role object with default values.
     * 
     * @param tenantId The tenant ID for the role
     * @return The created Role object
     */
    private Role getCitizenRole(String tenantId) {
        Role role = Role.builder().build();
        role.setCode(CITIZEN_UPPER);
        role.setName(CITIZEN_LOWER);
        role.setTenantId(getStateLevelTenant(tenantId));  // Sets the state-level tenant ID
        return role;
    }

    /**
     * Extracts the state-level tenant ID from the full tenant ID.
     * 
     * @param tenantId The full tenant ID
     * @return The state-level tenant ID
     */
    public String getStateLevelTenant(String tenantId) {
        return tenantId.split("\\.")[0];  // Extracts state-level part of the tenant ID
    }

}
