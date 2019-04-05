package pico.erp.fax;


import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pico.erp.fax.execute.FaxExecuteId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.user.UserId;

@Entity(name = "Fax")
@Table(name = "FAX_FAX", indexes = {
  @Index(columnList = "requestedDate")
})
@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaxEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  FaxId id;

  @Column(length = TypeDefinitions.DESCRIPTION_LENGTH)
  String description;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "REQUESTER_ID", length = TypeDefinitions.ID_LENGTH)),
  })
  UserId requesterId;

  @Column
  OffsetDateTime requestedDate;

  @Column
  OffsetDateTime executedDate;

  int executedCount;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "EXECUTED_ID", length = TypeDefinitions.EXTERNAL_ID_LENGTH))
  })
  FaxExecuteId executeId;

  @Column(name = "SEND_TERMINATED")
  boolean terminated;

  @Column(name = "SEND_FAILED")
  boolean failed;

}
