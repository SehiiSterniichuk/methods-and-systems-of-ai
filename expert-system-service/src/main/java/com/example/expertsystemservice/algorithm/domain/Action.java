package com.example.expertsystemservice.algorithm.domain;

import java.util.List;

public record Action(Move move,
                     List<String> positions,
                     PieceState newPieceState,
                     List<String> removedPieces) {
}
