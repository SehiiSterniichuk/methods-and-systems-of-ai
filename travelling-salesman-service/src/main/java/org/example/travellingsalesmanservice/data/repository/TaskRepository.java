package org.example.travellingsalesmanservice.data.repository;

import org.example.travellingsalesmanservice.data.domain.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
}
