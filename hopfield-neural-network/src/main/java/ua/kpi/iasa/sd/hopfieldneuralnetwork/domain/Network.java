package ua.kpi.iasa.sd.hopfieldneuralnetwork.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
public class Network {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private String name;
    @OneToOne
    private WeightEntity weight;
}
