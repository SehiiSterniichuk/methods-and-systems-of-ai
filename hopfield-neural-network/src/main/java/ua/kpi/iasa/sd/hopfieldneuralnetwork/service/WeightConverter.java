package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import org.springframework.stereotype.Component;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.ArrayRow;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Weight;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.WeightEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class WeightConverter {

    private static final boolean[] EMPTY_ARRAY = {};

    public Weight convertToDTO(WeightEntity entity) {
        int size = entity.getW().size();
        var w = new byte[size][];
        entity.getW().forEach(i -> {
            var row = i.getRow();
            if (row.length != 0) {
                w[i.getRowIndex()] = toByteArray(row);
            } else {
                w[i.getRowIndex()] = new byte[size];
            }
        });
        return new Weight(w);
    }

    private byte[] toByteArray(boolean[] row) {
        var data = new byte[row.length];
        IntStream.range(0, row.length).parallel().forEach(i -> data[i] = (byte) (row[i] ? 1 : 0));
        return data;
    }
    private boolean[] toBooleanArray(byte[] row) {
        var data = new boolean[row.length];
        IntStream.range(0, row.length).parallel().forEach(i -> data[i] = row[i] == 1);
        return data;
    }

    public List<ArrayRow> toArrayRows(Weight weight) {
        List<ArrayRow> rows = new ArrayList<>(weight.w().length);
        for (int i = 0; i < weight.w().length; i++) {
            ArrayRow row = new ArrayRow();
            row.setRowIndex(i);
            final int index = i;
            IntStream.range(0, weight.w()[i].length).parallel().map(v -> weight.w()[index][v])
                    .filter(v -> v != 0)
                    .findAny()
                    .ifPresentOrElse(p -> row.setRow(toBooleanArray(weight.w()[index])), () -> row.setRow(EMPTY_ARRAY));
            rows.add(row);
        }
        return rows;
    }
}
