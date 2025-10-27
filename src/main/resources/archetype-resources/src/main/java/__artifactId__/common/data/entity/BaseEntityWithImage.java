package ${package}.${artifactId}.common.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseEntityWithImage extends BaseEntity {
    @Column(name = "file_name", nullable = true)
    private String filename;
}
