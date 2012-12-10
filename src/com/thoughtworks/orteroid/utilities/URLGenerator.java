package com.thoughtworks.orteroid.utilities;

public class URLGenerator {
    public String getBoardURL(String input, String id) {
        return "http://www.ideaboardz.com/for/" + input + "/" + id + ".json";
    }

    public String getPointsURL(String input, String id) {
        return "http://www.ideaboardz.com/retros/" + input + "/" + id + "/points.json";
    }

    public String setUrlForAddingIdea(int sectionId, String idea) {
        return "http://www.ideaboardz.com//points.json?point[section_id]=" + sectionId + "&point[message]=" + idea;
    }
}
