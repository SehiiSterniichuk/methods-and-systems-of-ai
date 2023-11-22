package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import org.springframework.stereotype.Component;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.ArrayRow;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Weight;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.WeightEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class WeightConverter {

    private static final int[] EMPTY_ARRAY = {};

    public Weight convertToDTO(WeightEntity entity) {
        int size = entity.getW().size();
        int[][] w = new int[size][];
        entity.getW().stream().mapToInt(ArrayRow::getRowId)
                .forEach(i -> {
                    int[] row = entity.getW().get(i).getRow();
                    if (row.length != 0) {
                        w[i] = row;
                    } else {
                        w[i] = new int[size];
                    }
                });
        return new Weight(w);
    }

    public List<ArrayRow> toArrayRows(Weight weight) {
        List<ArrayRow> rows = new ArrayList<>(weight.w().length);
        for (int i = 0; i < weight.w().length; i++) {
            ArrayRow row = new ArrayRow();
            row.setRowId(i);
            final int index = i;
            Arrays.stream(weight.w()[i]).parallel()
                    .filter(v -> v != 0)
                    .findAny()
                    .ifPresentOrElse(p -> row.setRow(weight.w()[index]), () -> row.setRow(EMPTY_ARRAY));
            rows.add(row);
        }
        return rows;
    }
}
