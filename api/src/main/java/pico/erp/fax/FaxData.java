package pico.erp.fax;

import java.time.LocalDateTime;
import lombok.Data;
import pico.erp.fax.execute.FaxExecuteId;
import pico.erp.user.UserId;

@Data
public class FaxData {

  FaxId id;

  String description;

  UserId requesterId;

  LocalDateTime requestedDate;

  LocalDateTime executedDate;

  int executedCount;

  FaxExecuteId executeId;

  boolean terminated;

  boolean failed;

}
