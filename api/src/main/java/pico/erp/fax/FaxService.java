package pico.erp.fax;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface FaxService {

  void revalidate(@Valid @NotNull FaxRequests.RevalidateRequest request);

  void send(@Valid @NotNull FaxRequests.SendRequest request);

}
