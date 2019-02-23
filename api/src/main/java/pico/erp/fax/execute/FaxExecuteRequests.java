package pico.erp.fax.execute;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.fax.FaxId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.ContentInputStream;

public interface FaxExecuteRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class ExecuteRequest {

    FaxId faxId;

    @Valid
    @Size(max = TypeDefinitions.ID_LENGTH)
    String faxNumber;

    @Valid
    @NotNull
    ContentInputStream content;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class RetryRequest {

    FaxExecuteId id;

    FaxId faxId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class ClearRequest {

    FaxExecuteId id;

    FaxId faxId;

  }

}
