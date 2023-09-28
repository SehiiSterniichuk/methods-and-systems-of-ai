package org.example.travellingsalesmanservice.data.domain;

import lombok.*;
import org.example.travellingsalesmanservice.algorithm.domain.Dataset;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Task {
    @Id
    private String id;
    private TaskConfig config;
    private Dataset dataset;
    private List<Statistic> statisticList;
    private String message;
}
