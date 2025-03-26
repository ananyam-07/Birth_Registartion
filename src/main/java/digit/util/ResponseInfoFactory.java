package digit.util;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.stereotype.Component;

import static digit.config.ServiceConstants.*;

/**
 * Factory class to create ResponseInfo from RequestInfo and success status.
 */
@Component
public class ResponseInfoFactory {

    /**
     * Creates a ResponseInfo object based on the RequestInfo and success flag.
     * 
     * @param requestInfo The incoming request info object
     * @param success Flag indicating if the request was successful
     * @return The ResponseInfo object
     */
    public ResponseInfo createResponseInfoFromRequestInfo(final RequestInfo requestInfo, final Boolean success) {

        // Extract values from the incoming request info or use default values
        final String apiId = requestInfo != null ? requestInfo.getApiId() : "";
        final String ver = requestInfo != null ? requestInfo.getVer() : "";
        Long ts = requestInfo != null ? requestInfo.getTs() : null;  // Timestamp
        final String resMsgId = RES_MSG_ID;  // Hardcoded response message ID
        final String msgId = requestInfo != null ? requestInfo.getMsgId() : "";
        final String responseStatus = success ? SUCCESSFUL : FAILED;  // Set response status based on success

        // Build and return the ResponseInfo object
        return ResponseInfo.builder()
                .apiId(apiId)
                .ver(ver)
                .ts(ts)
                .resMsgId(resMsgId)
                .msgId(msgId)
                .status(responseStatus)
                .build();
    }
}
