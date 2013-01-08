package com.thoughtworks.orteroid.utilities;

import android.app.Activity;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class SharedData {
    public static final String PREFS_NAME = "file";
    private static JSONArray jsonArray = new JSONArray();
    public static JSONArray getJsonArrayOfRecentBoard() {
        return jsonArray;
    }

    public static void add(String boardId, String boardKey,Activity activity) {
        if(boardId.equals("2") || boardId.equals("1")) return;

        SharedPreferences sharedPreferences = activity.getSharedPreferences(SharedData.PREFS_NAME, 0);
        String data = sharedPreferences.getString("boards", null);
        if(data == null || isBoardAlreadyInList(boardId, data)) return;
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("board_name", boardKey);
            jsonObject.put("board_id", boardId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
    }

    private static boolean isBoardAlreadyInList(String boardId, String data) {
        final String[] recentBoardId = JSONParser.parseStringToRecentBoardsId(data);
        for (String id : recentBoardId) {
            if(boardId.equals(id)) return true;
        }
        return false;
    }
}
