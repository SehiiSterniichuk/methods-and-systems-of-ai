package org.example.travellingsalesmanservice.algorithm.service;

public interface SecondParentSearcher {
    int PARENT_NOT_FOUND = -1;
    int findSecond(int firstParentIndex);
}
