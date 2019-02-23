package pico.erp.fax;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.ContentInputStream;
import pico.erp.user.UserId;

public interface FaxRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class SendRequest {

    @Valid
    @NotNull
    FaxId id;

    @Valid
    @Size(max = TypeDefinitions.ID_LENGTH)
    String faxNumber;

    @NotNull
    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    @Valid
    @NotNull
    ContentInputStream content;

    @Valid
    @NotNull
    UserId requesterId;

  }

  @Data
  @NoArgsConstructor
  @Builder
  class RevalidateRequest {

  }

}
