package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.egov.common.contract.models.Workflow;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.User;

/**
 * BirthRegistrationApplication holds the essential details for a birth registration application.
 */
@Schema(description = "An object that holds the basic data for a Birth Registration Application")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-03-17T10:01:06.211691200+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BirthRegistrationApplication {

    /**
     * Unique identifier for the birth registration application.
     */
    @JsonProperty("id")
    @Size(min = 2, max = 64)
    private String id = null;

    /**
     * Tenant ID indicating the jurisdiction for the registration.
     */
    @JsonProperty("tenantId")
    @NotNull
    @Size(min = 2, max = 128)
    private String tenantId = null;

    /**
     * Unique application number for the birth registration.
     */
    @JsonProperty("applicationNumber")
    @Size(min = 2, max = 128)
    private String applicationNumber = null;

    /**
     * First name of the baby being registered.
     */
    @JsonProperty("babyFirstName")
    @NotNull
    @Size(min = 2, max = 128)
    private String babyFirstName = null;

    /**
     * Last name of the baby being registered (optional).
     */
    @JsonProperty("babyLastName")
    @Size(min = 2, max = 128)
    private String babyLastName = null;

    /**
     * Details of the father of the baby (user object).
     */
    @JsonProperty("father")
    @NotNull
    @Valid
    private User father = null;

    /**
     * Details of the mother of the baby (user object).
     */
    @JsonProperty("mother")
    @NotNull
    @Valid
    private User mother = null;

    /**
     * Name of the doctor who attended the birth.
     */
    @JsonProperty("doctorName")
    @NotNull
    @Size(min = 2, max = 128)
    private String doctorName = null;

    /**
     * Name of the hospital where the birth occurred.
     */
    @JsonProperty("hospitalName")
    @NotNull
    @Size(min = 2, max = 128)
    private String hospitalName = null;

    /**
     * Place where the baby was born.
     */
    @JsonProperty("placeOfBirth")
    @NotNull
    @Size(min = 2, max = 128)
    private String placeOfBirth = null;

    /**
     * Time of birth (optional).
     */
    @JsonProperty("timeOfBirth")
    private Integer timeOfBirth = null;

    /**
     * Address associated with the birth registration.
     */
    @JsonProperty("address")
    @Valid
    private BirthApplicationAddress address = null;

    /**
     * Workflow associated with the birth registration.
     */
    @JsonProperty("workflow")
    @Valid
    private Workflow workflow = null;

    /**
     * Audit details for the birth registration application.
     */
    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails = null;
}
