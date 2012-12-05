package com.thoughtworks.orteroid.models;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BoardTest {
    List<Section> listOfSections;
    List<Point> listOfPoints;
    Point firstPoint, secondPoint;

    @Before
    public void setUp() {
        listOfSections = new ArrayList<Section>() {{
            add(new Section("What went well", 1));
            add(new Section("What did not go well", 2));
        }};

        firstPoint = new Point(1, 112, "Hey ! This is a test");
        secondPoint = new Point(2, 301, "This is a test too");

        listOfPoints = new ArrayList<Point>();
        listOfPoints.add(firstPoint);
        listOfPoints.add(secondPoint);
    }

    @Test
    public void shouldUpdateBoardIfPointsWereNotAlreadyPresent() {
        Board board = new Board("test", 2, "to test board", listOfSections);
        board.update(listOfPoints);

        Assert.assertTrue(board.sections().get(0).contains(firstPoint));
        Assert.assertTrue(board.sections().get(1).contains(secondPoint));
    }
    @Test
    public void shouldNotUpdateBoardIfPointsWereAlreadyPresent() {
        Board board = new Board("test", 2, "to test board", listOfSections);
        board.update(listOfPoints);

        Assert.assertTrue(board.sections().get(0).contains(firstPoint));
        Assert.assertTrue(board.sections().get(1).contains(secondPoint));
    }

}
