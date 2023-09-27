package org.example.travellingsalesmanservice.data.domain;

import lombok.*;
import org.example.travellingsalesmanservice.algorithm.domain.Point;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dataset {
    @Id
    private String name;
    private Point[] points;
}
