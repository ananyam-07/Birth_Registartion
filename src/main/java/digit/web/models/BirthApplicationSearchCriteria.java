package digit.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;


/**
 * BirthApplicationSearchCriteria holds the search criteria for searching birth registration applications.
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-03-17T10:01:06.211691200+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BirthApplicationSearchCriteria {

    /**
     * Tenant ID for which the birth registration search is to be performed.
     */
    @JsonProperty("tenantId")
    @NotNull
    private String tenantId = null;

    /**
     * Status of the birth registration application (e.g., pending, approved).
     */
    @JsonProperty("status")
    private String status = null;

    /**
     * List of application IDs to search for.
     * Maximum of 50 IDs allowed.
     */
    @JsonProperty("ids")
    @Size(max = 50)
    private List<String> ids = null;

    /**
     * Application number for searching the birth registration application.
     * Must be between 2 and 64 characters.
     */
    @JsonProperty("applicationNumber")
    @Size(min = 2, max = 64)
    private String applicationNumber = null;

    /**
     * Adds an ID to the list of IDs for search criteria.
     * @param idsItem The ID to be added.
     * @return The updated BirthApplicationSearchCriteria object.
     */
    public BirthApplicationSearchCriteria addIdsItem(String idsItem) {
        if (this.ids == null) {
            this.ids = new ArrayList<>();
        }
        this.ids.add(idsItem);
        return this;
    }
}
