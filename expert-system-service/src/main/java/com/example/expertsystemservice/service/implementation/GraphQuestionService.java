package com.example.expertsystemservice.service.implementation;

import com.example.expertsystemservice.domain.Question;
import com.example.expertsystemservice.repository.RuleRepository;
import com.example.expertsystemservice.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GraphQuestionService implements QuestionService {
    private final RuleRepository repository;

    @Override
    public List<Question> getQuestions(int limit) {
        return repository.findAllLimited(limit);
    }

    @Override
    public List<Question> findQuestionsContainString(String string) {
        List<Question> response = repository.findRuleByContainingString(string, 30L);
        if (response.isEmpty()) {
            String[] strings = string.toLowerCase().split("\\s+");
            response = repository.findRuleByContainingStringFromArray(strings, 5);
        }
        return response;
    }
}
