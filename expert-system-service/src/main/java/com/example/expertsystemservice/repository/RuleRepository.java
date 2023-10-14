package com.example.expertsystemservice.repository;

import com.example.expertsystemservice.domain.Action;
import com.example.expertsystemservice.domain.Rule;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;


public interface RuleRepository extends Neo4jRepository<Rule, Long> {
    @Query("MATCH(r:Rule{name: :#{#rule.name}}),(a:Action{name: :#{#action.name}}) CREATE(r)-[:THEN]->(a)")
    void connectThen(@Param("rule") Rule rule, @Param("action") Action action);
}

