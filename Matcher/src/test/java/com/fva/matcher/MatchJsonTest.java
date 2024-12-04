package com.fva.matcher;

import org.junit.jupiter.api.Test;

import static com.fva.matcher.MatchJson.hasExactlySameStateAs;
import static com.fva.matcher.MatchJson.hasSameStateAs;
import static org.junit.jupiter.api.Assertions.*;

public class MatchJsonTest {

    @Test
    void MatcherTest() {
        // age field is missing in expected, it will be ignored as MatchJson.strict = false
        String expected = """
                {
                  "name": "john doe",
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

        assertFalse(hasSameStateAs(expected).matches(wrongJson));
        assertTrue(hasSameStateAs(expected).matches(goodJson));

    }

    @Test
    void MatcherWithNullValuesTest() {

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


        assertTrue(hasSameStateAs(expected).matches(actual));

    }

    @Test
    void MatcherExpectedUnknownKeyTest() {

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


        assertThrows(IllegalArgumentException.class, () -> hasSameStateAs(expected).matches(actual));

    }

    @Test
    void StrictMatcherTest() {

        String expected = """
                [{
                  "name": "john doe",
                  "age": 30,
                  "address": {
                    "street": "White street",
                    "number": 1,
                    "zip": [
                      {
                        "postcode": 123
                      }
                    ]
                  }
                }]""";

        String actual = """
                [{
                                 "name": "john doe",
                                 "age": 30,
                                 "address": {
                                   "street": "White street",
                                   "number": 1,
                                   "zip": [
                                     {
                                       "postcode": 123
                                     }
                                   ]
                                 }
                               }]""";

        assertTrue(hasExactlySameStateAs(expected).matches(actual));


    }

    @Test
    void complexJsonTest() {
        String expected = """
                {
                  "name": "john doe",
                  "address": {
                    "street": "White street",
                    "number": 1,
                    "zip": [
                      {
                        "test": 123
                      },
                      {
                        "test": 456,
                        "zozo": [
                          {
                            "zaza": "908",
                            "zizi": "0987"
                          }
                        ]
                      }
                    ]
                  }
                }""";

        String actual = """
                {
                  "name": "john doe",
                  "address": {
                    "street": "White street",
                    "number": 1,
                    "zip": [
                      {
                        "test": 123
                      },
                      {
                        "test": 456,
                        "zozo": [
                          {
                            "zaza": "908",
                            "zizi": "0987"
                          }
                        ]
                      }
                    ]
                  }
                }""";


        assertTrue(hasSameStateAs(expected).matches(actual));
        // Assertions.assertFalse(MatchJson.hasExactlyStateAs(expected).matches(actual));
    }

}
