package com.example.expertsystemservice.repository;

import com.example.expertsystemservice.domain.Action;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ActionRepository extends Neo4jRepository<Action, Long> {
}
