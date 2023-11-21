package ua.kpi.iasa.sd.hopfieldneuralnetwork.repositories;

import org.springframework.data.repository.CrudRepository;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.ArrayRow;

public interface ArrayRepository extends CrudRepository<ArrayRow, Long> {
}
