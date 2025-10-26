package ${package}.common.data.dto;

import ${package}.common.data.entity.BaseAuditingEntity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseGetDto extends BaseAuditingEntity {
    protected Integer id;
}
