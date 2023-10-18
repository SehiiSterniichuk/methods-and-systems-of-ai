package com.example.expertsystemservice.repository;

import com.example.expertsystemservice.domain.Action;
import com.example.expertsystemservice.domain.ActionRelationship;
import com.example.expertsystemservice.domain.Rule;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;


public interface RuleRepository extends Neo4jRepository<Rule, Long> {
    @Query("""
            MATCH(r:Rule{name: :#{#rule.name}}),(a:Action{name: :#{#action.name}})
            CREATE(r)-[::#{#relationship}]->(a)
            """)
    void connect(Rule rule, Action action, ActionRelationship relationship);

    boolean existsRuleByName(String name);

    void deleteByName(String name);
    Optional<Rule> findRuleByName(String name);

    @Query("""
            MATCH (rule:Rule)
            WHERE elementId(rule) = :#{#id}
            RETURN rule
            """)
    Optional<Rule> findRuleById(long id);

    @Query("""
            CREATE (r:Rule)
            SET r = rule
            RETURN r
            """)
    Rule saveRule(Rule rule);
}

