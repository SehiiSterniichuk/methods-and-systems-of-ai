package ua.kpi.iasa.sd.hopfieldneuralnetwork.domain;

import jakarta.validation.constraints.NotBlank;

public record PostTaskRequest(@NotBlank String networkName, Pattern pattern) {
}
