package pico.erp.fax;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;

@Repository
public interface FaxRepository {

  Fax create(@NotNull Fax fax);

  void deleteBy(@NotNull FaxId id);

  boolean exists(@NotNull FaxId id);

  Stream<Fax> findAllProcessing();

  Optional<Fax> findBy(@NotNull FaxId id);

  void update(@NotNull Fax fax);


}
