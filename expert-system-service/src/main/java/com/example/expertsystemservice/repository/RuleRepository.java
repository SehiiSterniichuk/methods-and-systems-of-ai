package com.example.expertsystemservice.repository;

import com.example.expertsystemservice.domain.Question;
import com.example.expertsystemservice.domain.Rule;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;


public interface RuleRepository extends Neo4jRepository<Rule, Long> {
//    @Query("""
//            MATCH(r:Rule{name: :#{#rule.name}}),(a:Action{name: :#{#action.name}})
//            CREATE(r)-[::#{#relationship}]->(a)
//            """)
//    void connectByName(Rule rule, Action action, ActionRelationship relationship);
//
//    @Query("""
//            MATCH (a:Action), (r:Rule)
//            WHERE elementId(a) = :#{#action.id} AND elementId(r) = :#{#rule.id}
//            CREATE(r)-[:THEN]->(a)
//            """)
//    void connectThenById(Rule rule, Action action);
//
//    @Query("""
//            MATCH (a:Action), (r:Rule)
//            WHERE elementId(a) = :#{#action.id} AND elementId(r) = :#{#rule.id}
//            CREATE(r)-[:ELSE]->(a)
//            """)
//    void connectElseById(Rule rule, Action action);
//
//    @Query("""
//            MATCH (action:Action), (rule:Rule)
//            WHERE elementId(action) = :#{#action.id} AND elementId(rule) = :#{#rule.id}
//            CREATE (action)-[::#{#relationship}]->(rule)
//            """)
//    void connectGoto(Action action, Rule rule, GotoRelationship relationship);

    boolean existsRuleByName(String name);

    Optional<Rule> findRuleByIdOrName(Long id, String name);

    void deleteByName(String name);

    Optional<Rule> findRuleById(long id);

    @Query("""
            MATCH (n:Rule)
            WHERE NOT ()-[:GOTO]->(n)
            MATCH (anyRule:Rule)
            WITH COLLECT(n) AS rootRules, COLLECT(anyRule) AS allRules
            WHERE NOT ALL(node IN allRules WHERE node IN rootRules)
            RETURN allRules[0..:#{#limit}]
            """)
    List<Question> findAllLimited(long limit);
    //goal of the query is to provide user with root rules or
    // if roots too few

    @Query("""
                MATCH (r:Rule)
                WHERE toLower(r.name) CONTAINS toLower(:#{#string})
                OR toLower(r.condition) CONTAINS toLower(:#{#string})
                RETURN r
                LIMIT :#{#limit}
            """)
    List<Question> findRuleByContainingString(String string, long limit);


    @Query("""
                MATCH (r:Rule)
                WHERE ANY(searchString IN :#{#strings} WHERE toLower(r.name) CONTAINS searchString
                OR toLower(r.condition) CONTAINS searchString)
                RETURN r
                LIMIT :#{#limit}
            """)
    List<Question> findRuleByContainingStringFromArray(String[] strings, long limit);


    @Query("""
            MATCH (r:Rule)
            RETURN r
            LIMIT 1
            """)
    Optional<Rule> findAnyRule();
}

