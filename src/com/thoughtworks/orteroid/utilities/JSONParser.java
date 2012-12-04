package com.thoughtworks.orteroid.utilities;

import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Point;
import com.thoughtworks.orteroid.models.Section;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    public static Board parseToBoard(JSONObject jsonObject) {
        try {
            String name = jsonObject.getString("name");
            String description = jsonObject.getString("description");
            Integer id = jsonObject.getInt("id");
            JSONArray sectionJSON = new JSONArray(jsonObject.getString("sections"));
            List<Section> sections = parseToSections(sectionJSON);
            return new Board(name, id, description, sections);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failure in JSON Parse");
        }
    }

    private static List<Section> parseToSections(JSONArray sectionJSON) throws JSONException {
        List<Section> sections = new ArrayList<Section>();

        for (int i = 0; i < sectionJSON.length(); i++) {
            JSONObject jsonObject = sectionJSON.getJSONObject(i);
            sections.add(new Section(jsonObject.getString("name"), jsonObject.getInt("id")));
        }
        return sections;
    }

    public static List<Point> parseToPoints(String resultString) throws JSONException  {
        List<String> stringJSONObjectsList = toStringArrayOfPoints(resultString);

        JSONArray jsonArray = toJSONArrayOfPoints(stringJSONObjectsList);

        return getPointsFromJSONArray(jsonArray);
    }

    private static List<String> toStringArrayOfPoints(String resultString) {
        StringBuilder stringBuilder = new StringBuilder(resultString);
        List<String> stringJSONObjectsList = new ArrayList<String>();

        while(stringBuilder.toString().length() > 0){ //TODO: what if /} is present..? change the logic accordingly
            int endIndex = stringBuilder.indexOf("}");

            if(endIndex < 0){
                break;
            }
            stringJSONObjectsList.add(stringBuilder.substring(1, endIndex + 1));
            stringBuilder.delete(1, endIndex + 2);
        }
        return stringJSONObjectsList;
    }

    private static List<Point> getPointsFromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Point> points = new ArrayList<Point>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            points.add(new Point(jsonObject.getInt("section_id"),jsonObject.getInt("id"), jsonObject.getString("message")));

        }
        return points;
    }

    private static JSONArray toJSONArrayOfPoints(List<String> stringJSONObjectsList) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (String jsonString : stringJSONObjectsList) {
            jsonArray.put(new JSONObject(jsonString));
        }
        return jsonArray;
    }


}
