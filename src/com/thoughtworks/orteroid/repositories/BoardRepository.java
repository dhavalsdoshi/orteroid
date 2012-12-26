package com.thoughtworks.orteroid.repositories;

import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Point;
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
    private static URLGenerator urlGenerator;


    private BoardRepository() {
        urlGenerator = new URLGenerator();
    }

    public void retrieveBoard(String boardKey, String boardId, final Callback callback) {
        String boardURL = urlGenerator.getBoardURL(boardKey, boardId);
        String pointsURL = urlGenerator.getPointsURL(boardKey, boardId);
        Callback<List<String>> serverCallback = generateServerCallbackForGetRequestForBoard(callback);
        executeAsyncTask(serverCallback, boardURL, pointsURL);
    }

    public void retrievePoints(String boardKey, String boardId, Callback<List<Point>> pointsCallback) {
        String pointsURL = urlGenerator.getPointsURL(boardKey, boardId);
        Callback<List<String>> serverCallback = generateServerCallbackForGetRequestForPoints(pointsCallback);
        executeAsyncTask(serverCallback, pointsURL);
    }

    private void executeAsyncTask(Callback<List<String>> serverCallback, String... urls) {
        ContentFetcher contentFetcher = new ContentFetcher(serverCallback, Constants.GET);
        int length = urls.length;
        if(length == 1){
            contentFetcher.execute(urls[0]);
        } else {
            contentFetcher.execute(urls[0], urls[1]);
        }
    }

    public void addIdea(String idea, int sectionId, final Callback callback) {
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

    public void editIdea(String editedIdea, int ideaId, final Callback callback) {
        String encodedMessage = editedIdea.replace(" ","%20");
        String response = urlGenerator.setUrlForEditingIdea(ideaId, encodedMessage);
        Callback<List<String>> serverCallback = generateServerCallbackForPutRequest(callback);
        ContentFetcher contentFetcher = new ContentFetcher(serverCallback, Constants.PUT);
        contentFetcher.execute(response);
    }

    private Callback<List<String>> generateServerCallbackForGetRequestForBoard(final Callback<Board> callback) {
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

    private Callback<List<String>> generateServerCallbackForGetRequestForPoints(final Callback<List<Point>> pointsCallback) {
        return new Callback<List<String>>() {
            @Override
            public void execute(List<String> jsonResponseList) throws JSONException {
                final List<Point> points = JSONParser.parseToPoints(jsonResponseList.get(0));
                pointsCallback.execute(points);
            }
        };
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

    private Callback<List<String>> generateServerCallbackForPutRequest(final Callback callback) {
        return new Callback<List<String>>() {
            @Override
            public void execute(List<String> jsonResponseList) throws JSONException {
                callback.execute(true);
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