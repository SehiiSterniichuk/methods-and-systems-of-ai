package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Pattern;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.PostTaskRequest;

public interface TaskService {
    Pattern createTask(PostTaskRequest postRequest);

    Resource createTask(MultipartFile image, String name);
}
