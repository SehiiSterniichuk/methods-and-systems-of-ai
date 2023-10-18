package com.example.expertsystemservice.service.implementation;

import com.example.expertsystemservice.domain.Question;
import com.example.expertsystemservice.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphQuestionService implements QuestionService {
    @Override
    public List<Question> getQuestions(int i) {
        return null;
    }

    @Override
    public List<Question> findQuestionsContainString(String string) {
        return null;
    }
}
