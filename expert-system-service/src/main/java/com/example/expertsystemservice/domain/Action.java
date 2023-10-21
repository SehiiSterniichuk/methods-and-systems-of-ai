package com.example.expertsystemservice.domain;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Action {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String formula;

    @Relationship(type = "GOTO", direction = Relationship.Direction.OUTGOING)
    private List<Rule> gotoAction = new ArrayList<>(0);

    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public List<Action> toUnmodifiableList() {
        return List.of(this);
    }
}