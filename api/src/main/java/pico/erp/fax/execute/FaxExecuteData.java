package pico.erp.fax.execute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FaxExecuteData {

  FaxExecuteId id;

  boolean completed;

  boolean processing;

  boolean failed;

}
