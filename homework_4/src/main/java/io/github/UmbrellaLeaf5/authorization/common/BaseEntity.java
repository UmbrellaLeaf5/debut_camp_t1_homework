package io.github.UmbrellaLeaf5.authorization.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;

  @CreatedDate
  @Column(name = "CREATED_DATE", updatable = false, nullable = false)
  private LocalDateTime createdDate;

  @LastModifiedDate
  @Column(name = "LAST_MODIFIED_DATE", insertable = false)
  private LocalDateTime lastModifiedDate;

  @Column(name = "CREATED_BY", nullable = false, updatable = false) private String createdBy;

  @Column(name = "LAST_MODIFIED_BY", insertable = false) private String lastModifiedBy;
}
