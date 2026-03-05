package io.github.jacoblee23.bibleguesser.scriptures;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;


public class AbbreviationsTest {
    @ParameterizedTest
    @EnumSource(Abbreviations.System.class)
    void testGetMap(Abbreviations.System system) {
        Abbreviations abbreviations = Abbreviations.getInstance();
        Map<String, String> map = abbreviations.getMap(system);
        Assertions.assertNotNull(map, system.toString());

        Canon canon = Canon.getInstance();
        for (String book : canon.listBooks()) {
            Assertions.assertNotNull(
                map.containsKey(book), String.format(
                    "No entry found in system %s for book %s", system.toString(), book
                )
            );
        }
    }

    @ParameterizedTest
    @EnumSource(Abbreviations.System.class)
    void testEncode(Abbreviations.System system) {
        Abbreviations abbreviations = Abbreviations.getInstance();
        Map<String, String> map = abbreviations.getMap(system);

        Assertions.assertNull(abbreviations.encode(system, "foobar"), system.toString());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Assertions.assertEquals(
                entry.getValue(), abbreviations.encode(system, entry.getKey()), system.toString()
            );
        }
        Assertions.assertEquals(map, abbreviations.encode(system, map.keySet()), system.toString());
    }

    @ParameterizedTest
    @EnumSource(Abbreviations.System.class)
    void testDecode(Abbreviations.System system) {
        Abbreviations abbreviations = Abbreviations.getInstance();
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> entry : abbreviations.getMap(system).entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }

        Assertions.assertNull(abbreviations.decode(system, "foobar"), system.toString());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Assertions.assertEquals(
                entry.getValue(), abbreviations.decode(system, entry.getKey()), system.toString()
            );
        }
        Assertions.assertEquals(map, abbreviations.decode(system, map.keySet()), system.toString());
    }
}
