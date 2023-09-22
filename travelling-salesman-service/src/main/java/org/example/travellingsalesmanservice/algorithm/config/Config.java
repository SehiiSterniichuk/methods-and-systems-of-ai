package org.example.travellingsalesmanservice.algorithm.config;

import org.example.travellingsalesmanservice.algorithm.service.implementation.RandomGeneMutation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class Config {
    @Bean
    public RandomGeneMutation randomGeneMutation(){
        return new RandomGeneMutation(new Random());
    }
}
