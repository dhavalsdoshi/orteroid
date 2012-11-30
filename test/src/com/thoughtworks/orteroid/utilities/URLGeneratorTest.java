package com.thoughtworks.orteroid.utilities;

import com.thoughtworks.orteroid.utilities.URLGenerator;
import junit.framework.TestCase;

public class URLGeneratorTest extends TestCase {

    public void testShouldGenerateCorrectURLFromBoardNameAndID(){
        String name = "test";
        String id="2";
        URLGenerator urlGenerator = new URLGenerator();

        String result = urlGenerator.getBoardURL(name,id);

        assertEquals(result, "http://www.ideaboardz.com/for/test/2.json");
    }
}
