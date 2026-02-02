package io.github.jacoblee23.bibleguesser.canon;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;


public class AbbreviationsTest {
    @Test
    void testGetHeaders() {
        Abbreviations abbreviations;
        try {
            abbreviations = Abbreviations.getInstance();
        } catch (IOException e) {
            Assertions.fail(e);
            return;
        }

        List<String> headers = abbreviations.getHeaders();
        Arrays.asList(Abbreviations.System.values()).forEach(
            (value) -> { Assertions.assertTrue(headers.contains(value.getName())); }
        );
    }

    @Test
    void testGetTable() {
        Canon canon = Canon.getInstance();
        Abbreviations abbreviations;
        try {
            abbreviations = Abbreviations.getInstance();
        } catch (IOException e) {
            Assertions.fail(e);
            return;
        }

        List<String> books = new LinkedList<>();
        abbreviations.getTable().forEach(
            (key, value) -> { books.add(key); }
        );
        Assertions.assertEquals(canon.getBooks(), books);
    }

    @ParameterizedTest
    @EnumSource(Abbreviations.System.class)
    void testEncode(Abbreviations.System system) {
        Abbreviations abbreviations;
        try {
            abbreviations = Abbreviations.getInstance();
        } catch (IOException e) {
            Assertions.fail(e);
            return;
        }

        Map<String, String> entries = abbreviations.getAbbreviations(system);
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Assertions.assertEquals(entry.getValue(), abbreviations.encode(entry.getKey(), system));
        }
    }

    @ParameterizedTest
    @EnumSource(Abbreviations.System.class)
    void testDecode(Abbreviations.System system) {
        Abbreviations abbreviations;
        try {
            abbreviations = Abbreviations.getInstance();
        } catch (IOException e) {
            Assertions.fail(e);
            return;
        }

        Map<String, String> entries = abbreviations.getAbbreviations(system);
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Assertions.assertEquals(entry.getKey(), abbreviations.decode(entry.getValue(), system));
            Assertions.assertEquals(entry.getKey(), abbreviations.decode(entry.getValue()));
        }
    }
}
