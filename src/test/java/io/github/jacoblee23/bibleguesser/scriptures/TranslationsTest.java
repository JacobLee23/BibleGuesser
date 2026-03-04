package io.github.jacoblee23.bibleguesser.scriptures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TranslationsTest {
    @Test
    void testPath() {
        Assertions.assertNotNull(
            getClass().getResource(Translations.PATH), String.format(
                "Failed to locate resource: %s", Translations.PATH
            )
        );
    }

    @Test
    void testDirectory() {
        Assertions.assertNotNull(
            getClass().getResource(Translations.DIRECTORY), String.format(
                "Failed to locate resource: %s", Translations.DIRECTORY
            )
        );
    }

    @Test
    void testGetName() {
        Translations translations = Translations.getInstance();
        for (Translations.Translation transl : Translations.Translation.values()) {
            Assertions.assertNotNull(
                translations.getName(transl), String.format(
                    "No unabbreviated name found for translation: %s", transl
                )
            );
        }
    }

    @Test
    void testGetResourceName() {
        Translations translations = Translations.getInstance();
        for (Translations.Translation transl : Translations.Translation.values()) {
            String name = translations.getResourceName(transl);
            Assertions.assertNotNull(
                getClass().getResource(name), String.format("Failed to locate resource: %s", name)
            );
        }
    }
}
