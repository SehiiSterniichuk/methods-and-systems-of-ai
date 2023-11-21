package ua.kpi.iasa.sd.hopfieldneuralnetwork.repositories;

import org.springframework.data.repository.CrudRepository;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Network;

import java.util.Optional;

public interface NetworkRepository extends CrudRepository<Network, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<Network> findByNameIgnoreCase(String name);
}
