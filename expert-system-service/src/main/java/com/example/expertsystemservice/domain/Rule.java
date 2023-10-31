package com.example.expertsystemservice.domain;

import com.example.expertsystemservice.domain.decision.DecisionInfo;
import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.List;

@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rule {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String condition;

    private RuleType type;

    private String formula;

    private List<String> variables;

    @Relationship(type = "THEN", direction = Relationship.Direction.OUTGOING)
    private List<Action> thenAction = new ArrayList<>(1);

    @Relationship(type = "ELSE", direction = Relationship.Direction.OUTGOING)
    private List<Action> elseAction= new ArrayList<>(1);

    public Rule(Long id, String name, String condition, DecisionInfo decisionInfo, List<Action> thenEntity, List<Action> elseEntity) {
        this(id, name, condition, decisionInfo.type(), decisionInfo.formula(), decisionInfo.variables(), thenEntity, elseEntity);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }

    public DecisionInfo getDecisionInfo(){
        return new DecisionInfo(type, formula, variables);
    }
}
