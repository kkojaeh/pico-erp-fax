package pico.erp.fax;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.ApplicationId;
import pico.erp.shared.data.Role;

public final class FaxApi {

  public final static ApplicationId ID = ApplicationId.from("fax");

  @RequiredArgsConstructor
  public enum Roles implements Role {

    FAX_MANAGER;

    @Id
    @Getter
    private final String id = name();

  }
}
