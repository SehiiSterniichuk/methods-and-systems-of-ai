package ua.kpi.iasa.sd.hopfieldneuralnetwork.domain;

import io.hypersistence.utils.hibernate.type.array.BooleanArrayType;
import io.hypersistence.utils.hibernate.type.array.IntArrayType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
public class ArrayRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer rowIndex;
    @Column(columnDefinition = "BOOLEAN[]", nullable = false)
    @Type(BooleanArrayType.class)
    private boolean[] row;
}
