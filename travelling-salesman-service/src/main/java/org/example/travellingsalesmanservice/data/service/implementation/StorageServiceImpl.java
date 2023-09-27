package org.example.travellingsalesmanservice.data.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.algorithm.domain.Point;
import org.example.travellingsalesmanservice.data.domain.Dataset;
import org.example.travellingsalesmanservice.data.domain.DatasetDTO;
import org.example.travellingsalesmanservice.data.repository.DatasetRepository;
import org.example.travellingsalesmanservice.data.service.StorageService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final DatasetRepository repository;

    private Dataset dtoToPojo(DatasetDTO dto) {
        return new Dataset(dto.name(), dto.points());
    }

    private DatasetDTO pojoToDto(Dataset pojo) {
        return new DatasetDTO(pojo.getName(), pojo.getPoints());
    }

    @Override
    public void saveDataset(DatasetDTO request) {
        Mono<Dataset> mono = repository.findById(request.name())
                .switchIfEmpty(Mono.defer(() -> repository.save(dtoToPojo(request))))
                .flatMap(Mono::just)
                .onErrorResume(Mono::error);
        mono.block();
    }

    @Override
    public DatasetDTO findDatasetByName(String name) {
        return repository.findById(name)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(STR."Dataset \{name} not found")))
                .flatMap(data -> Mono.just(pojoToDto(data)))
                .onErrorResume(Mono::error).block();
    }
}
