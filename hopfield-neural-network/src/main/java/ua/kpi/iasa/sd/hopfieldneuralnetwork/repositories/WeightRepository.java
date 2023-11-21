package ua.kpi.iasa.sd.hopfieldneuralnetwork.repositories;

import org.springframework.data.repository.CrudRepository;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.WeightEntity;

public interface WeightRepository extends CrudRepository<WeightEntity, Long> {
}
