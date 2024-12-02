package com.fva.matcher;


import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatchJsonTest {

    @Test
    public void MatcherTest() {

        String expected = """
                {
                  "name": "john doe",
                  "age": 30,
                  "address": {
                    "street": "White street",
                    "number": 1,
                    "zip": [
                      "001",
                      "002",
                      "003"
                    ]
                  }
                }""";

        String wrongJson = """
                {
                  "name": "john doe",
                  "age": 30,
                  "address": {
                    "street": "White street",
                    "number": 12,
                    "zip": [
                      "001",
                      "002",
                      "0032"
                    ]
                  }
                }""";

        String goodJson = """
                {
                  "name": "john doe",
                  "age": 30,
                  "address": {
                    "street": "White street",
                    "number": 1,
                    "zip": [
                      "001",
                      "002",
                      "003"
                    ]
                  }
                }""";

        assertFalse(new MatchJson(expected).matches(wrongJson));
        assertTrue(new MatchJson(expected).matches(goodJson));

    }

    @Test
    public void MatcherWithNullValuesTest() {

        String expected = """
                {
                  "name": null,
                  "age": 30,
                  "address": {
                    "street": "White street",
                    "number": 1,
                    "zip": [
                      "001",
                      "002",
                      "003"
                    ]
                  }
                }""";

        String actual = """
                {
                  "name": null,
                  "age": 30,
                  "address": {
                    "street": "White street",
                    "number": 1,
                    "zip": [
                      "001",
                      "002",
                      "003"
                    ]
                  }
                }""";


        assertTrue(new MatchJson(expected).matches(actual));

    }

    @Test
    public void MatcherExpectedUnknownKeyTest() {

        String expected = """
                {
                  "name": null,
                  "sport": {},
                  "age": 30,
                  "address": {
                    "street": "White street",
                    "number": 1,
                    "zip": [
                      "001",
                      "002",
                      "003"
                    ]
                  }
                }""";

        String actual = """
                {
                  "name": null,
                  "age": 30,
                  "address": {
                    "street": "White street",
                    "number": 1,
                    "zip": [
                      "001",
                      "002",
                      "003"
                    ]
                  }
                }""";


        Assertions.assertThrows(IllegalStateException.class, () -> new MatchJson(expected).matches(actual));

    }

}
