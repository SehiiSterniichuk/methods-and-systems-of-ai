package org.example.travellingsalesmanservice.app.service;


import org.example.travellingsalesmanservice.algorithm.domain.Dataset;
import org.example.travellingsalesmanservice.algorithm.domain.TaskId;
import org.example.travellingsalesmanservice.app.domain.ResultResponse;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;

public interface TaskService {
    TaskId createTask(TaskConfig config, Dataset dataset);

    ResultResponse getTask(String id);
}
