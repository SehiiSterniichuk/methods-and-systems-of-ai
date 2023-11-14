package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Pattern;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Weight;

import java.util.List;

public interface HopfieldWeightCalculator {
    Weight calc(List<Pattern> patterns);
}
