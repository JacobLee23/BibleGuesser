package io.github.jacoblee23.bibleguesser.scriptures;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


public class ReferenceTest {
    private static final Reference FIRST_VERSE = new Reference("Genesis", 1, 1);
    private static final Reference LAST_VERSE = new Reference("Revelation", 22, 21);

    public static Stream<Arguments> referenceFactory() {
        return Stream.of(
            Arguments.of("Genesis", 3, 15),
            Arguments.of("Exodus", 12, 13),
            Arguments.of("Leviticus", 17, 11),
            Arguments.of("Numbers", 21, 9),
            Arguments.of("Deuteronomy", 6, 4),
            Arguments.of("Joshua", 24, 15),
            Arguments.of("Judges", 21, 25),
            Arguments.of("Ruth", 4, 14),
            Arguments.of("1 Samuel", 16, 7),
            Arguments.of("2 Samuel", 7, 16),
            Arguments.of("1 Kings", 8, 23),
            Arguments.of("2 Kings", 17, 13),
            Arguments.of("1 Chronicles", 17, 14),
            Arguments.of("2 Chronicles", 7, 14),
            Arguments.of("Ezra", 7, 10),
            Arguments.of("Nehemiah", 9, 17),
            Arguments.of("Esther", 4, 14),
            Arguments.of("Job", 19, 25),
            Arguments.of("Psalm", 23, 1),
            Arguments.of("Proverbs", 1, 7),
            Arguments.of("Ecclesiastes", 12, 13),
            Arguments.of("Song of Solomon", 8, 7),
            Arguments.of("Isaiah", 53, 5),
            Arguments.of("Jeremiah", 31, 33),
            Arguments.of("Lamentations", 3, 22),
            Arguments.of("Ezekiel", 36, 26),
            Arguments.of("Daniel", 7, 14),
            Arguments.of("Hosea", 6, 6),
            Arguments.of("Joel", 2, 32),
            Arguments.of("Amos", 5, 24),
            Arguments.of("Obadiah", 1, 15),
            Arguments.of("Jonah", 2, 9),
            Arguments.of("Micah", 5, 2),
            Arguments.of("Nahum", 1, 7),
            Arguments.of("Habakkuk", 2, 4),
            Arguments.of("Zephaniah", 3, 17),
            Arguments.of("Haggai", 2, 9),
            Arguments.of("Zechariah", 12, 10),
            Arguments.of("Malachi", 4, 2),
            Arguments.of("Matthew", 1, 21),
            Arguments.of("Mark", 10, 45),
            Arguments.of("Luke", 19, 10),
            Arguments.of("John", 3, 16),
            Arguments.of("Acts", 4, 12),
            Arguments.of("Romans", 3, 23),
            Arguments.of("1 Corinthians", 15, 3),
            Arguments.of("1 Corinthians", 5, 21),
            Arguments.of("Galatians", 2, 16),
            Arguments.of("Ephesians", 2, 8),
            Arguments.of("Philippians", 2, 9),
            Arguments.of("Colossians", 1, 15),
            Arguments.of("1 Thessalonians", 4, 16),
            Arguments.of("2 Thessalonians", 1, 9),
            Arguments.of("1 Timothy", 2, 5),
            Arguments.of("2 Timothy", 3, 16),
            Arguments.of("Titus", 2, 11),
            Arguments.of("Philemon", 1, 16),
            Arguments.of("Hebrews", 9, 22),
            Arguments.of("James", 2, 17),
            Arguments.of("1 Peter", 2, 24),
            Arguments.of("2 Peter", 3, 9),
            Arguments.of("1 John", 4, 19),
            Arguments.of("2 John", 1, 7),
            Arguments.of("3 John", 1, 4),
            Arguments.of("Jude", 1, 24),
            Arguments.of("Revelation", 21, 4)
        );
    }

    @ParameterizedTest
    @MethodSource("referenceFactory")
    void testParse(String book, int chapter, int verse) {
        Reference reference = new Reference(book, chapter, verse);
    
        Reference duplicate = Reference.parse(reference.toString());
        Assertions.assertNotNull(duplicate);
        Assertions.assertEquals(reference, duplicate);

        Reference lowercase = Reference.parse(reference.toString().toLowerCase());
        Assertions.assertNull(lowercase);

        Reference uppercase = Reference.parse(reference.toString().toUpperCase());
        Assertions.assertNull(uppercase);
    }

    @ParameterizedTest
    @MethodSource("referenceFactory")
    void testPrecedes(String book, int chapter, int verse) {
        Reference reference = new Reference(book, chapter, verse);

        Assertions.assertTrue(ReferenceTest.FIRST_VERSE.precedes(reference));
        Assertions.assertTrue(reference.precedes(ReferenceTest.LAST_VERSE));

        Reference verseNext = new Reference(book, chapter, verse + 1);
        Assertions.assertTrue(
            reference.precedes(verseNext),
            String.format(
                "Comparing %s [%x] and %s [%x]",
                reference, reference.hashCode(), verseNext, verseNext.hashCode()
            )
        );

        Reference chapterNext = new Reference(book, chapter + 1, verse);
        Assertions.assertTrue(
            reference.precedes(chapterNext),
            String.format(
                "Comparing %s [%x] and %s [%x]",
                reference, reference.hashCode(), chapterNext, chapterNext.hashCode()
            )
        );
    }

    @ParameterizedTest
    @MethodSource("referenceFactory")
    void testSuceeds(String book, int chapter, int verse) {
        Reference reference = new Reference(book, chapter, verse);

        Assertions.assertTrue(reference.succeeds(ReferenceTest.FIRST_VERSE));
        Assertions.assertTrue(ReferenceTest.LAST_VERSE.succeeds(reference));

        Reference verseNext = new Reference(book, chapter, verse + 1);
        Assertions.assertTrue(
            verseNext.succeeds(reference),
            String.format(
                "Comparing %s [%x] and %s [%x]",
                reference, reference.hashCode(), verseNext, verseNext.hashCode()
            )
        );

        Reference chapterNext = new Reference(book, chapter + 1, verse);
        Assertions.assertTrue(
            chapterNext.succeeds(reference),
            String.format(
                "Comparing %s [%x] and %s [%x]",
                reference, reference.hashCode(), chapterNext, chapterNext.hashCode()
            )
        );
    }
}
