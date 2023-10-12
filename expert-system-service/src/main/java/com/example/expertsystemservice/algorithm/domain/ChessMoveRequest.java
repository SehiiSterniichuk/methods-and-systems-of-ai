package com.example.expertsystemservice.algorithm.domain;

public record ChessMoveRequest(
        String gameId,  // Unique identifier for the game
        String fen,     // FEN (Forsyth-Edwards Notation) representing the current board position
        String playerColor, // Color of the player making the next move (e.g., "white" or "black")
        String difficultyLevel // Difficulty level for the move recommendation, if applicable
) {}

