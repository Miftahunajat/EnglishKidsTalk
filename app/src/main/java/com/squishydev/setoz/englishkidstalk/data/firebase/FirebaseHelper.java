package com.squishydev.setoz.englishkidstalk.data.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.squishydev.setoz.englishkidstalk.data.firebase.model.Match;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface FirebaseHelper {

    void observeScore(String matchId, ChildEventListener listener);

    Observable<Match> postMatch(String userId, String userName, DatabaseReference.CompletionListener listener);

    Flowable<List<Match>> joinRandomMatch(String userId);

    Completable joinMatch(String userId, String matchId);

    Flowable<Match> observeScore(String matchId);

    Completable addScore(String userId, String matchId, Integer score);

    Observable<Task<Void>> deleteMatch(String matchId);

    Completable startMatch(String matchId);

    Flowable<Match> observeMatch(String matchId);
}
