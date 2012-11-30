package com.thoughtworks.orteroid.utilities;

import com.thoughtworks.orteroid.models.Board;
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
            return new Board(name,id,description,sections);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failure in JSON Parse");
        }
    }

    private static List<Section> parseToSections(JSONArray sectionJSON) throws JSONException {
        List<Section> sections = new ArrayList<Section>();

        for (int i = 0; i < sectionJSON.length(); i++) {
            JSONObject jsonObject = sectionJSON.getJSONObject(i);
            sections.add(new Section(jsonObject.getString("name"),jsonObject.getInt("id")));
        }
        return sections;
    }
}
