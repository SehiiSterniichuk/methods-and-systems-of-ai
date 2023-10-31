package com.example.expertsystemservice.service;

import com.example.expertsystemservice.domain.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestions(int i);

    List<Question> findQuestionsContainString(String string);
}
