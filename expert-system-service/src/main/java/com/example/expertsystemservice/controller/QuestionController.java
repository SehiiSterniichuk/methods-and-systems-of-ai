package com.example.expertsystemservice.controller;

import com.example.expertsystemservice.domain.Question;
import com.example.expertsystemservice.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/expert-system/question")
@CrossOrigin(origins = {"http://localhost:3000"})
public class QuestionController {
    //the role of this class is to provide user with hints of what problems can be solved here
    private final QuestionService service;

    //method to get default question for user-interface
    @GetMapping
    public List<Question> getQuestions(@RequestParam(defaultValue = "100") Integer limit) {
        return service.getQuestions(limit);
    }

    //intend to load questions that contain a word or a phrase
    @GetMapping("/{string}")
    public List<Question> findQuestionsContainString(@PathVariable String string){
        return service.findQuestionsContainString(string);
    }
}
