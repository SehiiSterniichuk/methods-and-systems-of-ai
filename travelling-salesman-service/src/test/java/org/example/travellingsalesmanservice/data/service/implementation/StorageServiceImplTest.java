package org.example.travellingsalesmanservice.data.service.implementation;

import org.example.travellingsalesmanservice.algorithm.domain.Point;
import org.example.travellingsalesmanservice.data.domain.Dataset;
import org.example.travellingsalesmanservice.data.domain.DatasetDTO;
import org.example.travellingsalesmanservice.data.repository.DatasetRepository;
import org.example.travellingsalesmanservice.data.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StorageServiceImplTest {
    private StorageService service;
    @Autowired
    private DatasetRepository repository;

    @BeforeEach
    void setUp() {
        service = new StorageServiceImpl(repository);
    }

    @Test
    void testFinding() {
        Supplier<DatasetDTO> supplier = () -> service.findDatasetByName(
                (new Random()).ints().limit(100)
                        .mapToObj(x -> (char) x)
                        .map(String::valueOf) // Convert each character to a String
                        .collect(Collectors.joining()) // Join the characters into a single String
        );
        assertThrows(RuntimeException.class, supplier::get);
    }

    @Test
    void testSaving() {
        String name = (new Random()).ints('A', 'Z' + 1).limit(100)
                .mapToObj(x -> (char) x)
                .map(String::valueOf) // Convert each character to a String
                .collect(Collectors.joining());
        service.saveDataset(DatasetDTO.builder().name(name).points(new Point[]{new Point(1, 1)}).build());
        Dataset saved = repository.findById(name).block();
        assertNotNull(saved);
        assertEquals(saved.getName(), name);
        assertEquals(saved.getPoints().length, 1);
        repository.deleteById(name).block();
    }
}