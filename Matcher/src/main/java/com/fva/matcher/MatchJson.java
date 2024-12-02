package com.fva.matcher;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Map;

public class MatchJson extends TypeSafeMatcher<String> {

    String expected;
    JsonElement expectedElement;
    JsonElement actualElement;

    public MatchJson(String expected) {
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(String actual) {

        expectedElement = JsonParser.parseString(expected);
        actualElement = JsonParser.parseString(actual);

        if (expectedElement.isJsonArray()) {
            expectedElement.getAsJsonArray()
                    .asList()
                    .forEach(it -> matchesSafely(it.getAsString()));
        }

        if (expectedElement.isJsonObject()) {
            return expectedElement.getAsJsonObject()
                    .entrySet()
                    .stream()
                    .allMatch(expItem -> {
                        if (expItem.getValue().isJsonObject()) {
                            this.expected = expItem.getValue().toString();
                            JsonObject asJsonObject = actualElement.getAsJsonObject().getAsJsonObject(expItem.getKey());
                            if (asJsonObject == null) {
                                throw new IllegalStateException("Actual json has no '%s' key ".formatted(expItem.getKey()));
                            }
                            return matchesSafely(asJsonObject.toString());
                        }
                        if (expItem.getValue().isJsonArray()) {
                            expItem.getValue().getAsJsonArray().asList()
                                    .forEach(item -> matchesSafely(expItem.getValue().toString()));
                        }
                        return fieldsMatch(expItem, actualElement);
                    });
        }


        boolean equals = expectedElement.toString().equals(actualElement.toString());
        System.out.println(equals);
        return equals;


    }

    private static boolean fieldsMatch(Map.Entry<String, JsonElement> it, JsonElement actualElement) {
        if (actualElement.isJsonObject()) {
            return actualElement.getAsJsonObject().has(it.getKey()) && actualElement.getAsJsonObject().get(it.getKey()).equals(it.getValue());
        }
        return actualElement.equals(it.getValue());

    }


    @Override
    public void describeTo(Description description) {
        description
                .appendValue(expectedElement);

    }

}
