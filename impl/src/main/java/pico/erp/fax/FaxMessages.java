package pico.erp.fax;

import java.util.Collection;
import java.util.function.Supplier;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Value;
import pico.erp.fax.execute.FaxExecuteData;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.event.Event;
import pico.erp.user.UserId;

public interface FaxMessages {

  interface Create {

    @Data
    class Request {

      @Valid
      @NotNull
      FaxId id;

      @Valid
      @NotNull
      UserId requesterId;

      @NotNull
      @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
      String description;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Execute {

    @Builder
    class Request {

      private final Supplier<FaxExecuteData> execute;

      private final Supplier<FaxExecuteData> retry;

      @Getter
      FaxExecuteData previous;

      @Getter
      int retryLimit;

      @Getter
      int expirationSeconds;

      @Getter
      int retryIntervalSeconds;

      public FaxExecuteData execute() {
        if (execute == null) {
          throw new FaxExceptions.CannotExecuteException();
        }
        return execute.get();
      }

      public FaxExecuteData retry() {
        if (retry == null) {
          throw new FaxExceptions.CannotRetryException();
        }
        return retry.get();
      }

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }


}
