package pico.erp.fax.execute;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface FaxExecuteService {

  void clear(@Valid @NotNull FaxExecuteRequests.ClearRequest request);

  FaxExecuteData execute(@Valid @NotNull FaxExecuteRequests.ExecuteRequest request);

  FaxExecuteData get(@Valid @NotNull FaxExecuteId id);

  FaxExecuteData retry(@Valid @NotNull FaxExecuteRequests.RetryRequest request);

}
