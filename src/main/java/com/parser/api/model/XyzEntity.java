package com.parser.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author tolik
 * @project xyz
 * @created 04.01.2023 - 11:57
 */

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "xyz")
public class XyzEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String newspaperName;
    Integer width;
    Integer height;
    Integer dpi;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Timestamp uploadTime;
    String fileName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        XyzEntity xyzEntity = (XyzEntity) o;
        return id != null && Objects.equals(id, xyzEntity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
