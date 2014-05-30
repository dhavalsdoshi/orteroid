package com.ideaboardz.android.utilities;

import com.ideaboardz.android.models.Point;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

    public String urlForEditingIdea(int ideaId, String idea, String oldIdea) {
        return "http://www.ideaboardz.com/points/" + ideaId + "?point[message]=" + idea + "&point[oldmessage]=" + oldIdea;
    }

    public String urlForDeletingIdea(Point selectedPoint) throws UnsupportedEncodingException {
        return "http://www.ideaboardz.com/points/delete/" + selectedPoint.id() + ".json?message=" + URLEncoder.encode(selectedPoint.message(), "UTF-8");
    }

    public String urlForVotingForAnIdea(Point selectedPoint) {
        return "http://www.ideaboardz.com/points/" + selectedPoint.id() + "/votes.json" + "?vote[point_id]=" + selectedPoint.id();
    }
}
