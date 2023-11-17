package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Pattern;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.PostTaskRequest;

public interface TaskService {
    Pattern createTask(PostTaskRequest postRequest);
}
