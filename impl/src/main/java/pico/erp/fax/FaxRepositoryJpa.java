package pico.erp.fax;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface FaxEntityRepository extends
  CrudRepository<FaxEntity, FaxId> {

  @Query("SELECT f FROM Fax f WHERE f.requestedDate > :date AND f.terminated = false")
  Stream<FaxEntity> findAllProcessing(@Param("date") OffsetDateTime date);

}

@Repository
@Transactional
public class FaxRepositoryJpa implements FaxRepository {

  @Autowired
  private FaxEntityRepository repository;

  @Autowired
  private FaxMapper mapper;


  @Override
  public Fax create(Fax fax) {
    val entity = mapper.jpa(fax);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(FaxId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(FaxId id) {
    return repository.existsById(id);
  }

  @Override
  public Stream<Fax> findAllProcessing() {
    return repository.findAllProcessing(OffsetDateTime.now().minusDays(3))
      .map(mapper::jpa);
  }

  @Override
  public Optional<Fax> findBy(FaxId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(Fax fax) {
    val entity = repository.findById(fax.getId()).get();
    mapper.pass(mapper.jpa(fax), entity);
    repository.save(entity);
  }


}
