package org.example.travellingsalesmanservice.data.repository;

import org.example.travellingsalesmanservice.data.domain.Dataset;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DatasetRepository extends ReactiveMongoRepository<Dataset, String> {
}
