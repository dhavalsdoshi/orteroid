package com.thoughtworks.orteroid.models;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;


public class BoardTest extends TestCase {
    List<Section> listOfSections;
    List<Point> listOfPoints;
    Point firstPoint, secondPoint;
    Board board;

    @Override
    public void setUp() {
        listOfSections = new ArrayList<Section>() {{
            add(new Section("What went well", 1));
            add(new Section("What did not go well", 2));
        }};

        firstPoint = new Point(1, 112, "Hey ! This is a test", 1,"2013/01/29 20:40:18 +0000");
        secondPoint = new Point(2, 301, "This is a test too", 2,"2013/01/29 20:40:18 +0000");
        board = new Board("test", 2, listOfSections);
        listOfPoints = new ArrayList<Point>();
        listOfPoints.add(firstPoint);
        listOfPoints.add(secondPoint);
        board.update(listOfPoints);
    }

    public void testShouldUpdateBoardIfPointsWereNotAlreadyPresent() {
        assertTrue(board.sections().get(0).contains(firstPoint));
        assertTrue(board.sections().get(1).contains(secondPoint));
    }

    public void testShouldNotUpdateBoardIfPointsWereAlreadyPresent() {
        List<Point> newListOfPoints = new ArrayList<Point>();

        newListOfPoints.add(firstPoint);
        newListOfPoints.add(secondPoint);

        board.update(newListOfPoints);

        assertTrue(board.sections().get(0).contains(firstPoint));
        assertTrue(board.sections().get(1).contains(secondPoint));

        assertEquals(board.sections().size(), listOfPoints.size());
    }

}
