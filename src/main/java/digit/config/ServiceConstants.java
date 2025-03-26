package digit.config;

import org.springframework.stereotype.Component;

@Component
public class ServiceConstants {

    // General Error Messages
    public static final String EXTERNAL_SERVICE_EXCEPTION = "External Service threw an Exception: ";
    public static final String SEARCHER_SERVICE_EXCEPTION = "Exception while fetching from searcher: ";
    public static final String ERROR_WHILE_FETCHING_FROM_MDMS = "Exception occurred while fetching category lists from mdms: ";

    // ID Generation Errors
    public static final String IDGEN_ERROR = "IDGEN ERROR";
    public static final String NO_IDS_FOUND_ERROR = "No ids returned from idgen Service";

    // Response and Status Messages
    public static final String RES_MSG_ID = "uief87324";
    public static final String SUCCESSFUL = "successful";
    public static final String FAILED = "failed";

    // URL Shortening Errors
    public static final String URL = "url";
    public static final String URL_SHORTENING_ERROR_CODE = "URL_SHORTENING_ERROR";
    public static final String URL_SHORTENING_ERROR_MESSAGE = "Unable to shorten url: ";

    // Date Formats
    public static final String DOB_FORMAT_Y_M_D = "yyyy-MM-dd";  // Year-Month-Day format
    public static final String DOB_FORMAT_D_M_Y = "dd/MM/yyyy";  // Day-Month-Year format
    public static final String DOB_FORMAT_D_M_Y_H_M_S = "dd-MM-yyyy HH:mm:ss";  // Date with Time (HH:mm:ss)

    // Exception Messages
    public static final String ILLEGAL_ARGUMENT_EXCEPTION_CODE = "IllegalArgumentException";
    public static final String OBJECTMAPPER_UNABLE_TO_CONVERT = "ObjectMapper not able to convertValue in userCall";
    public static final String INVALID_DATE_FORMAT_CODE = "INVALID_DATE_FORMAT";
    public static final String INVALID_DATE_FORMAT_MESSAGE = "Failed to parse date format in user";

    // Fields
    public static final String CREATED_DATE = "createdDate";  // Field name for created date
    public static final String LAST_MODIFIED_DATE = "lastModifiedDate";  // Field name for last modified date
    public static final String DOB = "dob";  // Field name for date of birth
    public static final String PWD_EXPIRY_DATE = "pwdExpiryDate";  // Field name for password expiry date

    // User Roles and Types
    public static final String CITIZEN_UPPER = "CITIZEN";  // Uppercase "CITIZEN"
    public static final String CITIZEN_LOWER = "Citizen";  // Lowercase "Citizen"
    public static final String USER = "user";  // User role/type

    // Parsing Errors
    public static final String PARSING_ERROR = "PARSING ERROR";
    public static final String FAILED_TO_PARSE_BUSINESS_SERVICE_SEARCH = "Failed to parse response of workflow business service search";

    // Business Service Errors
    public static final String BUSINESS_SERVICE_NOT_FOUND = "BUSINESSSERVICE_NOT_FOUND";
    public static final String THE_BUSINESS_SERVICE = "The businessService ";  // Prefix for business service messages
    public static final String NOT_FOUND = " is not found";  // Suffix for business service not found messages

    // Tenant and Business Service Query Params
    public static final String TENANTID = "?tenantId=";  // Tenant ID query parameter
    public static final String BUSINESS_SERVICES = "&businessServices=";  // Business services query parameter
}
