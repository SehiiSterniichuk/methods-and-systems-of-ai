package ua.kpi.iasa.sd.hopfieldneuralnetwork.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PostRequest(@NotBlank String name, @NotEmpty List<Pattern> patterns) {
}
