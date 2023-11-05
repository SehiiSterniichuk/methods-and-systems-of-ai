package com.example.expertsystemservice.repository;

import com.example.expertsystemservice.domain.Action;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface ActionRepository extends Neo4jRepository<Action, Long> {
    void deleteActionByName(String name);

    @Query("""
            MATCH (rule:Rule)
            WHERE id(rule) = :#{#ruleId}
            MATCH (action:Action)
            WHERE id(action) IN :#{#actionIds}
            CREATE (action)-[:GOTO]->(rule)
            """)
    void createGOTORelationships(Long ruleId, List<Long> actionIds);
}
