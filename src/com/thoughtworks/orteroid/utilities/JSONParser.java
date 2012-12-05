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
            throw new RuntimeException("Failure in JSON Parse to sections");
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
        List<Point> points = parse(resultString);
        return points;
    }

    private static List<Point> parse(String resultString) {
        JSONArray result;
        List<Point> points = new ArrayList<Point>();
        try {
            result = new JSONArray(resultString);
            for(int i = 0 ; i < result.length() ; i++ ){
                JSONObject jsonObject = result.getJSONObject(i);
                points.add(new Point(jsonObject.getInt("section_id"),jsonObject.getInt("id"),jsonObject.getString("message")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failure in JSON Parse to points");
        }
        return points;
    }
}
