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

    private static final String NAME_KEY = "name";
    private static final String ID_KEY = "id";
    private static final String SECTIONID_KEY = "section_id";
    private static final String MESSAGE_KEY = "message";

    public static Board parseToBoard(JSONObject jsonObject) {
        try {
            String name = jsonObject.getString(NAME_KEY);
            Integer id = jsonObject.getInt(ID_KEY);
            JSONArray sectionJSON = new JSONArray(jsonObject.getString("sections"));
            List<Section> sections = parseToSections(sectionJSON);
            return new Board(name, id, sections);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failure in JSON Parse to sections");
        }
    }

    public static List<Point> parseToPoints(String resultString) throws JSONException {
        return parse(resultString);
    }

    private static List<Section> parseToSections(JSONArray sectionJSON) throws JSONException {
        List<Section> sections = new ArrayList<Section>();

        for (int i = 0; i < sectionJSON.length(); i++) {
            JSONObject jsonObject = sectionJSON.getJSONObject(i);
            sections.add(new Section(jsonObject.getString(NAME_KEY), jsonObject.getInt(ID_KEY)));
        }
        return sections;
    }

    private static List<Point> parse(String resultString) throws JSONException {
        JSONArray result;
        List<Point> points = new ArrayList<Point>();
        result = new JSONArray(resultString);
        for (int i = 0; i < result.length(); i++) {
            JSONObject jsonObject = result.getJSONObject(i);
            points.add(parseToPoint(jsonObject));
        }
        return points;
    }

    public static Point parseToPoint(JSONObject jsonObject) throws JSONException {
        return new Point(jsonObject.getInt(SECTIONID_KEY), jsonObject.getInt(ID_KEY), jsonObject.getString(MESSAGE_KEY));
    }
}
