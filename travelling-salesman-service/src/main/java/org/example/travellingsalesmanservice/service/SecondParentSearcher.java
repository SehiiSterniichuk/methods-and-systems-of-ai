package org.example.travellingsalesmanservice.service;

public interface SecondParentSearcher {
    int PARENT_NOT_FOUND = -1;
    int findSecond(int firstParentIndex, int[] pathLengths);
}
