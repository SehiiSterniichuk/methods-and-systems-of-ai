package org.example.travellingsalesmanservice.domain;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.service.Subscriber;

import java.util.List;

@RequiredArgsConstructor
public class TrackingEntity {
    private final List<Subscriber> subscribers;

    public void update(Result result, int currentIteration) {
        var r = new ResultResponse(result, currentIteration);
        subscribers.forEach(s -> s.onNext(r));
    }
}
