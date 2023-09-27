package org.example.travellingsalesmanservice.data.service;

import org.example.travellingsalesmanservice.data.domain.DatasetDTO;

import java.util.List;

public interface StorageService {
    void saveDataset(DatasetDTO request);

    DatasetDTO findDatasetByName(String name);

    List<String> getAll();
}
