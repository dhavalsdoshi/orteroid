package com.thoughtworks.orteroid.utilities;

import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Section;
import junit.framework.TestCase;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParserTest extends TestCase{

    public void testShouldParseJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject("{\"name\":\"test\",\"sections\":[{\"name\":\"Went Well\",\"id\":4},{\"name\":\"Didn't Go Well\",\"id\":5},{\"name\":\"Action Items\",\"id\":6}],\"id\":2,\"description\":\"Try it out\"}");
        Board board = JSONParser.parseToBoard(jsonObject);
        List<Section> sections = new ArrayList<Section>(){{
            add(new Section("Went Well",4));
            add(new Section("Didn't Go Well",5));
            add(new Section("Action Item",6));
        }};

        Board expectedBoard = new Board("test",2,"Try it out", sections);

        assertEquals(board, expectedBoard);
    }
}
