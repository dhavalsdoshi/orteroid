package com.thoughtworks.orteroid.utilities;

import com.thoughtworks.orteroid.models.Point;

public class URLGenerator {
    public String getBoardURL(String input, String id) {
        return "http://www.ideaboardz.com/for/" + input + "/" + id + ".json";
    }

    public String getPointsURL(String input, String id) {
        return "http://www.ideaboardz.com/retros/" + input + "/" + id + "/points.json";
    }

    public String setUrlForAddingIdea(int sectionId, String idea) {
        return "http://www.ideaboardz.com/points.json?point[section_id]=" + sectionId + "&point[message]=" + idea;
    }

    public String urlForEditingIdea(int ideaId, String idea) {
        return "http://www.ideaboardz.com/points/" + ideaId + "?point[message]=" + idea;
    }

    public String urlForDeletingIdea(Point selectedPoint) {
        return "http://www.ideaboardz.com/points/delete/" + selectedPoint.id() + ".json";
    }
}
