package com.fva.matcher;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import static com.google.gson.JsonParser.parseString;
import static java.util.stream.IntStream.range;

/**
 * This Matcher compares recursively two JSON strings field by field.
 * If strict mode is on, it compares the whole JSON. Otherwise:
 * It uses the expected JSON string as a base and compares each key-value in the actual JSON to the expected.
 * If the expected JSON contains a key that is not in the actual JSON, an exception is thrown.
 * If any value does not match the expected, it returns false.
 */

public class MatchJson extends TypeSafeMatcher<String> {

    private final String expected;
    private final boolean strict;

    private MatchJson(String expected, boolean strict) {
        this.expected = expected;
        this.strict = strict;
    }

    public static MatchJson hasSameStateAs(String expected) {
        return new MatchJson(expected, false); // Non-strict by default
    }

    public static MatchJson hasExactlySameStateAs(String expected) {
        return new MatchJson(expected, true); // Strict mode
    }

    @Override
    protected boolean matchesSafely(String actualJson) {
        JsonElement actualElement = parseString(actualJson);
        JsonElement expectedElement = parseString(expected);

        return matchesSafely(actualElement, expectedElement);
    }

    private boolean matchesSafely(JsonElement actual, JsonElement expected) {
        if (strict) {
            return expected.equals(actual);
        }

        if (expected.isJsonObject()) {
            return matchesJsonObject(actual.getAsJsonObject(), expected.getAsJsonObject());
        }

        if (expected.isJsonArray()) {
            return matchesJsonArray(actual.getAsJsonArray(), expected.getAsJsonArray());
        }

        // Base case: primitive value comparison
        return expected.toString().equals(actual.toString());
    }

    private boolean matchesJsonObject(JsonObject actualObj, JsonObject expectedObj) {
        for (String key : expectedObj.keySet()) {
            JsonElement expectedValue = expectedObj.get(key);
            JsonElement actualValue = actualObj.get(key);

            if (actualValue == null) {
                throw new IllegalArgumentException("Key '" + key + "' is missing in the actual JSON.");
            }

            if (!matchesSafely(actualValue, expectedValue)) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesJsonArray(JsonArray actualArray, JsonArray expectedArray) {
        if (actualArray.size() != expectedArray.size()) {
            throw new IllegalArgumentException("Array sizes do not match.");
        }

        return range(0, expectedArray.size())
                .allMatch(i -> matchesSafely(actualArray.get(i), expectedArray.get(i)));
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }

}
