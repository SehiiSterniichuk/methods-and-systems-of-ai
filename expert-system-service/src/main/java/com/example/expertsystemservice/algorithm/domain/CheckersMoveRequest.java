package com.example.expertsystemservice.algorithm.domain;

public record CheckersMoveRequest(
        String gameId,  // Unique identifier for the game
        Color player // Color of the player making the next move (e.g., "white" or "black")
) {}

