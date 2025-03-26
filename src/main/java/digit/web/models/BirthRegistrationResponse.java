package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
import org.egov.common.contract.response.ResponseInfo;


/**
 * Represents the response contract for birth registration operations.
 * Supports both search results and create responses (array), 
 * or a single item response for updates.
 */
@Schema(description = "Contract class to send response. Array of items are used in case of search results or response for create, whereas single item is used for update")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-03-17T10:01:06.211691200+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BirthRegistrationResponse {

    /**
     * Metadata about the response, such as status and timestamp.
     */
    @JsonProperty("ResponseInfo")
    @Valid
    private ResponseInfo responseInfo = null;

    /**
     * List of birth registration applications, used in search results or for create responses.
     */
    @JsonProperty("BirthRegistrationApplications")
    @Valid
    private List<BirthRegistrationApplication> birthRegistrationApplications = null;

    /**
     * Adds a birth registration application to the response, 
     * typically used when handling multiple applications.
     */
    public BirthRegistrationResponse addBirthRegistrationApplicationsItem(BirthRegistrationApplication birthRegistrationApplicationsItem) {
        if (this.birthRegistrationApplications == null) {
            this.birthRegistrationApplications = new ArrayList<>();
        }
        this.birthRegistrationApplications.add(birthRegistrationApplicationsItem);
        return this;
    }
}
