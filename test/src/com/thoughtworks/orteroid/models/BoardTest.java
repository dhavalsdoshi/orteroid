package com.thoughtworks.orteroid.models;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;


public class BoardTest extends TestCase{
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

        firstPoint = new Point(1, 112, "Hey ! This is a test");
        secondPoint = new Point(2, 301, "This is a test too");
        board = new Board("test", 2, "to test board", listOfSections);
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
        assertTrue(board.sections().get(0).contains(firstPoint));
        assertTrue(board.sections().get(1).contains(secondPoint));
    }

}
