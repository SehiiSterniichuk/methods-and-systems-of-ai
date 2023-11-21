package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import org.springframework.stereotype.Component;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.ArrayRow;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Weight;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.WeightEntity;

@Component
public class WeightConverter {
    public Weight convertToDTO(WeightEntity entity) {
        int[][] w = new int[entity.getW().size()][];
        entity.getW().stream().mapToInt(ArrayRow::getRowId)
                .forEach(i -> w[i] = entity.getW().get(i).getRow());
        return new Weight(w);
    }
}
