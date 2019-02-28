package pico.erp.fax;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface FaxService {

  FaxData get(@Valid @NotNull FaxId id);

  void revalidate(@Valid @NotNull FaxRequests.RevalidateRequest request);

  FaxData send(@Valid @NotNull FaxRequests.SendRequest request);

}
