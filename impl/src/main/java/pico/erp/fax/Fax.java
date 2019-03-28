package pico.erp.fax;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.val;
import pico.erp.fax.execute.FaxExecuteId;
import pico.erp.user.UserId;

/**
 * 주문 접수
 */
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Fax implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  FaxId id;

  String description;

  UserId requesterId;

  LocalDateTime requestedDate;

  LocalDateTime executedDate;

  int executedCount;

  FaxExecuteId executeId;

  boolean terminated;

  boolean failed;

  public Fax() {
    executedCount = 0;
    terminated = false;
    failed = false;
  }

  public FaxMessages.Create.Response apply(
    FaxMessages.Create.Request request) {
    this.id = request.getId();
    this.description = request.getDescription();
    this.requesterId = request.getRequesterId();
    this.requestedDate = LocalDateTime.now();
    return new FaxMessages.Create.Response(
      Collections.emptyList()
    );
  }

  public FaxMessages.Execute.Response apply(
    FaxMessages.Execute.Request request) {
    if (executedCount > 0) {
      val previous = request.getPrevious();
      if (previous.isCompleted()) {
        terminated = true;
        return new FaxMessages.Execute.Response(
          Arrays.asList(new FaxEvents.SentEvent(id))
        );
      } else if (previous.isProcessing()) {
        return new FaxMessages.Execute.Response(
          Collections.emptyList()
        );
      } else if (previous.isFailed()) {
        val now = LocalDateTime.now();
        val yet = now.isBefore(executedDate.plusSeconds(request.getRetryIntervalSeconds()));
        if (yet) {
          return new FaxMessages.Execute.Response(
            Collections.emptyList()
          );
        }
        // 제한시간 초과
        val expired = now.isAfter(requestedDate.plusSeconds(request.getExpirationSeconds()));
        // 제한횟수 초과
        val limited = executedCount > request.getRetryLimit();

        if (expired || limited) {
          terminated = true;
          failed = true;
          return new FaxMessages.Execute.Response(
            Arrays.asList(new FaxEvents.FailedEvent(id))
          );
        }
      }
    }
    // 실행
    val execute = executedCount > 0 ? request.retry() : request.execute();
    this.executeId = execute.getId();
    this.executedCount++;
    this.executedDate = LocalDateTime.now();
    return new FaxMessages.Execute.Response(
      Collections.emptyList()
    );
  }

}
