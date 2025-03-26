package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * BirthApplicationSearchRequest encapsulates the request for searching birth registration applications.
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BirthApplicationSearchRequest {

    /**
     * RequestInfo contains metadata about the incoming request (e.g., request ID, user info).
     */
    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo = null;

    /**
     * BirthApplicationSearchCriteria contains the criteria for searching birth registration applications.
     */
    @JsonProperty("BirthApplicationSearchCriteria")
    @Valid
    private BirthApplicationSearchCriteria birthApplicationSearchCriteria = null;
}
