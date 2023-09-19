package org.example.travellingsalesmanservice.service.implementation;

import org.example.travellingsalesmanservice.service.PathLengthEstimator;
import org.example.travellingsalesmanservice.service.XYPopulationGenerator;

public class SimplePathLengthEstimator implements PathLengthEstimator {
    @Override
    public void calculateSquaredPathLength(XYPopulationGenerator.Points chromosomes, int[] pathLengths) {
        var x = chromosomes.x();
        var y = chromosomes.y();
        int length = x.length - 1;
        int geneNumber = length / pathLengths.length;
        int sum = 0;
        for (int i = 0; i < length; i++) {
            var dX = x[i + 1] - x[i];
            var dY = y[i + 1] - y[i];
            sum += dX * dX + dY * dY;
            if ((i + 1) % geneNumber == 0) {
                pathLengths[i / geneNumber] = sum;
                sum = 0;
            }
        }
    }
}
