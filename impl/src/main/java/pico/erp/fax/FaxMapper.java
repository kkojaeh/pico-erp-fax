package pico.erp.fax;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public abstract class FaxMapper {

  public abstract FaxEntity jpa(Fax data);

  public Fax jpa(FaxEntity entity) {
    return Fax.builder()
      .id(entity.getId())
      .description(entity.getDescription())
      .requesterId(entity.getRequesterId())
      .requestedDate(entity.getRequestedDate())
      .executedDate(entity.getExecutedDate())
      .executedCount(entity.getExecutedCount())
      .executeId(entity.getExecuteId())
      .terminated(entity.isTerminated())
      .build();
  }

  public abstract FaxMessages.Create.Request map(
    FaxRequests.SendRequest request);

  public abstract void pass(FaxEntity from,
    @MappingTarget FaxEntity to);


}


