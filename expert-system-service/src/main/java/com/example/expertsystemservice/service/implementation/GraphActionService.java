package com.example.expertsystemservice.service.implementation;

import com.example.expertsystemservice.domain.Action;
import com.example.expertsystemservice.repository.ActionRepository;
import com.example.expertsystemservice.service.ActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GraphActionService implements ActionService {
    private final ActionRepository repository;

    @Override
    public long delete(long id) {
        Optional<Action> action = repository.findById(id);
        if (action.isPresent()) {
            repository.delete(action.get());
            return id;
        }
        return -1;
    }
}
