package org.example.travellingsalesmanservice.data.service;

import org.example.travellingsalesmanservice.data.domain.DatasetDTO;

public interface StorageService {
    void saveDataset(DatasetDTO request);

    DatasetDTO findDatasetByName(String name);
}
