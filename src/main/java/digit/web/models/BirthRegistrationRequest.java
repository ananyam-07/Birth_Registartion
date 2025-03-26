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
import org.egov.common.contract.request.RequestInfo;

/**
 * Represents a contract for receiving requests for birth registration.
 * Supports both creating multiple applications (array) or updating a single application (single item).
 */
@Schema(description = "Contract class to receive request. Array of items are used in case of create, whereas single item is used for update")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-03-17T10:01:06.211691200+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BirthRegistrationRequest {

    /**
     * Metadata related to the request, such as user info and request timestamp.
     */
    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo = null;

    /**
     * List of birth registration applications; used for creating multiple applications or updating a single one.
     */
    @JsonProperty("BirthRegistrationApplications")
    @Valid
    private List<BirthRegistrationApplication> birthRegistrationApplications = null;

    /**
     * Adds a birth registration application to the list.
     * Used for handling multiple applications in case of create operation.
     */
    public BirthRegistrationRequest addBirthRegistrationApplicationsItem(BirthRegistrationApplication birthRegistrationApplicationsItem) {
        if (this.birthRegistrationApplications == null) {
            this.birthRegistrationApplications = new ArrayList<>();
        }
        this.birthRegistrationApplications.add(birthRegistrationApplicationsItem);
        return this;
    }
}
