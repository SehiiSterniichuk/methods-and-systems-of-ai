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
public class Rule {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String condition;

    @Relationship(type = "THEN", direction = Relationship.Direction.OUTGOING)
    private List<Action> thenAction = new ArrayList<>(1);

    @Relationship(type = "ELSE", direction = Relationship.Direction.OUTGOING)
    private List<Action> elseAction= new ArrayList<>(1);

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }
}
