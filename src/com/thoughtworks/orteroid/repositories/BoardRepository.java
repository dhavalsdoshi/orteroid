package com.thoughtworks.orteroid.repositories;

import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.utilities.ContentFetcher;
import com.thoughtworks.orteroid.utilities.JSONParser;
import com.thoughtworks.orteroid.utilities.URLGenerator;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class BoardRepository {

    private static BoardRepository boardRepository;


    private BoardRepository() {
    }

    public void retrieveBoard(String boardKey, String boardId, final Callback callback) {
        String boardURL = new URLGenerator().getBoardURL(boardKey, boardId);
        Callback<List<String>> serverCallback = generateServerCallbackForGetRequest(callback);
        ContentFetcher contentFetcher = new ContentFetcher(serverCallback, Constants.GET);
        String pointsURL = new URLGenerator().getPointsURL(boardKey, boardId);
        contentFetcher.execute(boardURL, pointsURL);
    }

    public void addIdea(String idea, int sectionId, final Callback callback) {
        URLGenerator urlGenerator = new URLGenerator();
        String encodedMessage = null;
        try {
            encodedMessage = URLEncoder.encode(idea, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String response = urlGenerator.setUrlForAddingIdea(sectionId, encodedMessage);
        Callback<List<String>> serverCallback = generateServerCallbackForPostRequest(callback);
        ContentFetcher contentFetcher = new ContentFetcher(serverCallback, Constants.POST);
        contentFetcher.execute(response);
    }

    private Callback<List<String>> generateServerCallbackForPostRequest(final Callback callback) {
        return new Callback<List<String>>() {
            @Override
            public void execute(List<String> jsonResponseList) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponseList.get(0));
                    JSONParser.parseToPoint(jsonObject);
                    callback.execute(true);
                } catch (JSONException e) {
                    callback.execute(false);
                    e.printStackTrace();
                }

            }
        };
    }

    private Callback<List<String>> generateServerCallbackForGetRequest(final Callback<Board> callback) {
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