package io.github.jacoblee23.bibleguesser.scriptures;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TranslationsTest {
    @Test
    void testGetTable() {
        Translations translations;
        try {
            translations = Translations.getInstance();
        } catch (IOException e) {
            Assertions.fail(e);
            return;
        }

        Map<String, String> table = translations.getTable();
        Set<String> keys = table.keySet();
        for (Translations.Version version : Translations.Version.values()) {
            Assertions.assertTrue(keys.contains(version.toString()));
        }
    }

    @Test
    void testGetName() {
        Translations translations;
        try {
            translations = Translations.getInstance();
        } catch (IOException e) {
            Assertions.fail(e);
            return;
        }

        for (Translations.Version version : Translations.Version.values()) {
            Assertions.assertTrue(translations.getName(version) != null);
        }
    }
}
