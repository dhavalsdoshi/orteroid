package com.thoughtworks.orteroid.utilities;

public class URLParser {
    public static String parse(String url) {
        String[] parsedURL = url.split("^www.ideaboardz.com/for/*");
        for (String string : parsedURL) {
            System.out.println("/././././././././././././././"+string);
        }
        return null;
    }
}
