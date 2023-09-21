package org.example.travellingsalesmanservice.service;

import org.example.travellingsalesmanservice.domain.ResultResponse;

public interface Subscriber {
    void onNext(ResultResponse resultResponse);
}
