package com.thoughtworks.orteroid.repositories;

import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.utilities.ContentFetcher;
import com.thoughtworks.orteroid.utilities.JSONParser;
import com.thoughtworks.orteroid.utilities.URLGenerator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BoardRepository {

    private static BoardRepository boardRepository;


    private BoardRepository() {
    }

    public void retrieveBoard(String boardKey, String boardId, final Callback callback) {
        String boardURL = new URLGenerator().getBoardURL(boardKey, boardId);
        Callback<List<String>> serverCallback = generateServerCallback(callback);
        ContentFetcher contentFetcher = new ContentFetcher(serverCallback);
        String pointsURL = new URLGenerator().getPointsURL(boardKey, boardId);
        contentFetcher.execute(boardURL, pointsURL);
    }

    private Callback<List<String>> generateServerCallback(final Callback<Board> callback) {
        return new Callback<List<String>>() {
            @Override
            public void execute(List<String> jsonResponseList) throws JSONException {
                JSONObject jsonObject = new JSONObject(jsonResponseList.get(0));
                final Board boardSkeleton = JSONParser.parseToBoard(jsonObject);
                boardSkeleton.update(JSONParser.parseToPoints(jsonResponseList.get(1)));
                callback.execute(boardSkeleton);
            }
        };
    }

    public static BoardRepository getInstance() {
        if (boardRepository == null) {
            boardRepository = new BoardRepository();
        }
        return boardRepository;
    }

    public static void setBoardRepository(BoardRepository boardRepository) {
        BoardRepository.boardRepository = boardRepository;
    }
}