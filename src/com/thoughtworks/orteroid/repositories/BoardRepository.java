package com.thoughtworks.orteroid.repositories;

import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.utilities.ContentFetcher;
import com.thoughtworks.orteroid.utilities.JSONParser;
import com.thoughtworks.orteroid.utilities.URLGenerator;
import org.json.JSONException;
import org.json.JSONObject;

public class BoardRepository {


    public void  retrieveBoard(String boardKey, String boardId, final Callback<Board> callback) {
        String url = new URLGenerator().getBoardURL(boardKey, boardId);
        Callback<String> serverCallback = generateServerCallback(callback);
        ContentFetcher contentFetcher = new ContentFetcher(serverCallback);
        contentFetcher.execute(url);
    }

    private Callback<String> generateServerCallback(final Callback<Board> callback) {
        return new Callback<String>() {
            @Override
            public void execute(String jsonBoard) throws JSONException {
                JSONObject jsonObject = new JSONObject(jsonBoard);
                final Board boardSkeleton = JSONParser.parseToBoard(jsonObject);
                updateBoard(boardSkeleton, callback);
            }
        };
    }

    private void updateBoard(final Board boardSkeleton, final Callback<Board> callback) {
        new ContentFetcher(new Callback<String>() {
            @Override
            public void execute(String object) throws JSONException {
                boardSkeleton.update(JSONParser.parseToPoints(object));
                callback.execute(boardSkeleton);
            }

        }).execute("http://www.ideaboardz.com/retros/test/2/points.json");
    }
}
