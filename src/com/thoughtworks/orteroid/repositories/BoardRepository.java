package com.thoughtworks.orteroid.repositories;

import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.utilities.ContentFetcher;
import com.thoughtworks.orteroid.utilities.JSONParser;
import com.thoughtworks.orteroid.utilities.URLGenerator;
import org.json.JSONObject;

public class BoardRepository {

    public void retrieveBoard(String boardKey, String boardId, final Callback callback) {
        String url = new URLGenerator().getBoardURL(boardKey, boardId);
        Callback<JSONObject> serverCallback = generateServerCallback(callback);
        ContentFetcher contentFetcher = new ContentFetcher(serverCallback);
        contentFetcher.execute(url);
    }

    private Callback<JSONObject> generateServerCallback(final Callback callback) {
        return new Callback<JSONObject>() {
            @Override
            public void execute(JSONObject jsonBoard) {
                callback.execute(JSONParser.parseToBoard(jsonBoard));
            }
        };
    }
}
