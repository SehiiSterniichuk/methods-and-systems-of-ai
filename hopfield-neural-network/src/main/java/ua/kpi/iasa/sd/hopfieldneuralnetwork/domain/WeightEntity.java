package ua.kpi.iasa.sd.hopfieldneuralnetwork.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
public class WeightEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany
    @Column(nullable = false)
    private List<ArrayRow> w;
}
