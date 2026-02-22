package io.github.jacoblee23.bibleguesser.scriptures;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TranslationsTest {
    @Test
    void testGetTable() {
        Translations translations = Translations.getInstance();
        Map<String, String> table = translations.getTable();
        Set<String> keys = table.keySet();
        for (Translations.Version version : Translations.Version.values()) {
            Assertions.assertTrue(keys.contains(version.toString()));
        }
    }

    @Test
    void testGetName() {
        Translations translations = Translations.getInstance();
        for (Translations.Version version : Translations.Version.values()) {
            Assertions.assertTrue(translations.getName(version) != null);
        }
    }
}
