package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.PostTaskRequest;

public interface TaskService {
    Long createTask(PostTaskRequest postRequest);
}
