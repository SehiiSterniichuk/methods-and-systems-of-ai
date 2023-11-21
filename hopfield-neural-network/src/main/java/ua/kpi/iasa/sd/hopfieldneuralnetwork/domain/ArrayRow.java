package ua.kpi.iasa.sd.hopfieldneuralnetwork.domain;

import io.hypersistence.utils.hibernate.type.array.IntArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
public class ArrayRow {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Integer rowId;
    @Column(columnDefinition = "INTEGER[]", nullable = false)
    @Type(IntArrayType.class)
    private int[] row;
}
