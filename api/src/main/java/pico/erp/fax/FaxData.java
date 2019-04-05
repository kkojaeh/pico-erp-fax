package pico.erp.fax;

import java.time.OffsetDateTime;
import lombok.Data;
import pico.erp.fax.execute.FaxExecuteId;
import pico.erp.user.UserId;

@Data
public class FaxData {

  FaxId id;

  String description;

  UserId requesterId;

  OffsetDateTime requestedDate;

  OffsetDateTime executedDate;

  int executedCount;

  FaxExecuteId executeId;

  boolean terminated;

  boolean failed;

}
