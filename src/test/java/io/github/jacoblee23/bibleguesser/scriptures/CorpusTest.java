package io.github.jacoblee23.bibleguesser.scriptures;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;


public class CorpusTest {
    // Total number of chapters in the Bible
    private static final int NCHAPTERS_TOTAL = 1189;

    // Total number of chapters in the Old Testament of the Bible
    private static final int NCHAPTERS_OT = 929;

    // Total number of chapters in the New Testament of the Bible
    private static final int NCHAPTERS_NT = 260;

    // Total number of verses in Biblical canon
    private static final int NVERSES_TOTAL = 31102;

    // Total number of verses in Old Testament of Biblical canon
    private static final int NVERSES_OT = 23145;

    // Total number of verses in New Testament of Biblical canon
    private static final int NVERSES_NT = 7957;

    // Indices of bookend verses of the Bible
    private static final int IDX_OTSTART = 0;
    private static final int IDX_OTEND = CorpusTest.NVERSES_OT - 1;
    private static final int IDX_NTSTART = CorpusTest.NVERSES_OT;
    private static final int IDX_NTEND = CorpusTest.NVERSES_TOTAL - 1;

    // Indices of midpoint verses of the Bible
    private static final int IDX_MID = (CorpusTest.NVERSES_TOTAL + 1) / 2 - 1;
    private static final int IDX_OTMID = (CorpusTest.NVERSES_OT + 1) / 2 - 1;
    private static final int IDX_NTMID = CorpusTest.NVERSES_OT + (CorpusTest.NVERSES_NT + 1) / 2 - 1;

    // Bookened verses of the Bible
    private static final Citation OTSTART = new Citation("Genesis", 1, 1);
    private static final Citation OTEND = new Citation("Malachi", 4, 6);
    private static final Citation NTSTART = new Citation("Matthew", 1, 1);
    private static final Citation NTEND = new Citation("Revelation", 22, 21);

    // Midpoint verses of the Bible
    private static final Citation MID = new Citation("Psalms", 103, 1);
    private static final Citation OTMID = new Citation("2 Chronicles", 18, 30);
    private static final Citation NTMID = new Citation("Acts", 7, 7);

    public static Stream<Arguments> verseFactory() {
        return Stream.of(
            Arguments.of(Translations.Translation.AKJV, "For God so loved the world, that he gave his only begotten Son, that whoever believes in him should not perish, but have everlasting life."),
            Arguments.of(Translations.Translation.AMPC, "For God so greatly loved and dearly prized the world that He [even] gave up His only begotten ( unique) Son, so that whoever believes in (trusts in, clings to, relies on) Him shall not perish (come to destruction, be lost) but have eternal (everlasting) life."),
            Arguments.of(Translations.Translation.ASV, "For God so loved the world, that he gave his only begotten Son, that whosoever believeth on him should not perish, but have eternal life."),
            Arguments.of(Translations.Translation.BSB, "For God so loved the world that He gave His one and only Son, that everyone who believes in Him shall not perish but have eternal life."),
            Arguments.of(Translations.Translation.CPDV, "For God so loved the world that he gave his only-begotten Son, so that all who believe in him may not perish, but may have eternal life."),
            Arguments.of(Translations.Translation.DBT, "For God so loved the world, that he gave his only-begotten Son, that whosoever believes on him may not perish, but have life eternal."),
            Arguments.of(Translations.Translation.DRB, "For God so loved the world, as to give his only begotten Son; that whosoever believeth in him, may not perish, but may have life everlasting."),
            Arguments.of(Translations.Translation.ERV, "For God so loved the world, that he gave his only begotten Son, that whosoever believeth on him should not perish, but have eternal life."),
            Arguments.of(Translations.Translation.ESV, "For God so loved the world, that he gave his only Son, that whoever believes in him should not perish but have eternal life."),
            Arguments.of(Translations.Translation.JPS, "For so greatly did God love the world that He gave His only Son, that every one who trusts in Him may not perish but may have the Life of Ages."),
            Arguments.of(Translations.Translation.KJV, "For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life."),
            Arguments.of(Translations.Translation.NASB, "\"For God so loved the world, that He gave His only begotten Son, that whoever believes in Him shall not perish, but have eternal life."),
            Arguments.of(Translations.Translation.NET, "For this is the way God loved the world: He gave his one and only Son, so that everyone who believes in him will not perish but have eternal life."),
            Arguments.of(Translations.Translation.SLT, "For God so loved the world, that he gave his only born Son, that every one believing in him perish not, but have eternal life."),
            Arguments.of(Translations.Translation.WBT, "For God so loved the world, that he gave his only-begotten Son, that whoever believeth in him, should not perish, but have everlasting life."),
            Arguments.of(Translations.Translation.WEB, "For God so loved the world, that he gave his one and only Son, that whoever believes in him should not perish, but have eternal life."),
            Arguments.of(Translations.Translation.WEY, "For so greatly did God love the world that He gave His only Son, that every one who trusts in Him may not perish but may have the Life of Ages."),
            Arguments.of(Translations.Translation.YLT, "for God did so love the world, that His Son—the only begotten—He gave, that every one who is believing in him may not perish, but may have life age-during.")
        );
    }

    @ParameterizedTest
    @EnumSource(Translations.Translation.class)
    void testGetTranslation(Translations.Translation translation) {
        Corpus corpus = Corpus.getInstance(translation);
        Assertions.assertEquals(translation, corpus.getTranslation(), translation.toString());
    }

    @ParameterizedTest
    @EnumSource(Translations.Translation.class)
    void testNChapters(Translations.Translation translation) {
        Exception exception;

        Canon canon = Canon.getInstance();
        Corpus corpus = Corpus.getInstance(translation);

        exception = Assertions.assertThrows(
            IllegalArgumentException.class, () -> {
                corpus.nchapters("foobar");
            }, translation.toString()
        );
        Assertions.assertEquals(
            String.format(
                "[%s] Unrecognized book: foobar", translation
            ), exception.getMessage(), translation.toString()
        );

        Assertions.assertEquals(CorpusTest.NCHAPTERS_TOTAL, corpus.nchapters(), translation.toString());

        int lengthOT = 0;
        for (String book : canon.listBooks("Old Testament")) {
            lengthOT += corpus.nchapters(book);
        }
        Assertions.assertEquals(CorpusTest.NCHAPTERS_OT, lengthOT, translation.toString());

        int lengthNT = 0;
        for (String book : canon.listBooks("New Testament")) {
            lengthNT += corpus.nchapters(book);
        }
        Assertions.assertEquals(CorpusTest.NCHAPTERS_NT, lengthNT, translation.toString());
    }

    @ParameterizedTest
    @EnumSource(Translations.Translation.class)
    void testNVerses(Translations.Translation translation) {
        Exception exception;

        Canon canon = Canon.getInstance();
        Corpus corpus = Corpus.getInstance(translation);

        exception = Assertions.assertThrows(
            IllegalArgumentException.class, () -> {
                corpus.nverses("foobar");
            }, translation.toString()
        );
        Assertions.assertEquals(
            String.format(
                "[%s] Unrecognized book: foobar", translation
            ), exception.getMessage(), translation.toString()
        );

        exception = Assertions.assertThrows(
            IllegalArgumentException.class, () -> {
                corpus.nverses("foobar", 1);
            }, translation.toString()
        );
        Assertions.assertEquals(
            String.format(
                "[%s] Unrecognized book: foobar", translation
            ), exception.getMessage(), translation.toString()
        );

        for (String book : canon.listBooks()) {
            exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    corpus.nverses(book, 0);
                }, translation.toString()
            );
            Assertions.assertEquals(
                String.format(
                    "[%s] Unrecognized chapter: %s 0", translation, book
                ), exception.getMessage(), translation.toString()
            );
        }

        Assertions.assertEquals(27, corpus.nverses("Exodus", 15), translation.toString());
        Assertions.assertEquals(52, corpus.nverses("Deuteronomy", 32), translation.toString());
        Assertions.assertEquals(31, corpus.nverses("Judges", 5), translation.toString());
        Assertions.assertEquals(36, corpus.nverses("1 Samuel", 2), translation.toString());
        Assertions.assertEquals(51, corpus.nverses("2 Samuel", 22), translation.toString());
        Assertions.assertEquals(28, corpus.nverses("Job", 28), translation.toString());
        Assertions.assertEquals(6, corpus.nverses("Psalms", 23), translation.toString());
        Assertions.assertEquals(19, corpus.nverses("Psalms", 51), translation.toString());
        Assertions.assertEquals(12, corpus.nverses("Isaiah", 53), translation.toString());
        Assertions.assertEquals(80, corpus.nverses("Luke", 1), translation.toString());

        if (
            translation == Translations.Translation.AMPC
            || translation == Translations.Translation.ESV
        ) {
            Assertions.assertEquals(15, corpus.nverses("3 John", 1), translation.toString());
        } else {
            Assertions.assertEquals(14, corpus.nverses("3 John", 1), translation.toString());
        }

        if (
            translation == Translations.Translation.AMPC
            || translation == Translations.Translation.ESV
        ) {
            Assertions.assertEquals(CorpusTest.NVERSES_TOTAL + 1, corpus.nverses(), translation.toString());
        } else {
            Assertions.assertEquals(CorpusTest.NVERSES_TOTAL, corpus.nverses(), translation.toString());
        }

        int lengthOT = 0;
        for (String book : canon.listBooks("Old Testament")) {
            lengthOT += corpus.nverses(book);
        }
        Assertions.assertEquals(CorpusTest.NVERSES_OT, lengthOT, translation.toString());

        int lengthNT = 0;
        for (String book : canon.listBooks("New Testament")) {
            lengthNT += corpus.nverses(book);
        }
        if (
            translation == Translations.Translation.AMPC
            || translation == Translations.Translation.ESV
        ) {
            Assertions.assertEquals(CorpusTest.NVERSES_NT + 1, lengthNT, translation.toString());
        } else {
            Assertions.assertEquals(CorpusTest.NVERSES_NT, lengthNT, translation.toString());
        }
    }

    @ParameterizedTest
    @EnumSource(Translations.Translation.class)
    void testGetIndex(Translations.Translation translation) {
        Exception exception;

        Corpus corpus = Corpus.getInstance(translation);

        // Invalid chapters contained in citation
        Citation invalidChapter = new Citation("Psalms", corpus.nchapters("Psalms") + 1, 1);
        exception = Assertions.assertThrows(
            IllegalArgumentException.class, () -> {
                corpus.getIndex(invalidChapter);
            }, translation.toString()
        );
        Assertions.assertEquals(
            String.format(
                "Invalid chapter number contained in citation: %s", invalidChapter
            ), exception.getMessage(), translation.toString()
        );

        // Invalid verses contained in citation
        Citation invalidVerse = new Citation("Psalms", 1, corpus.nverses("Psalms", 1) + 1);
        exception = Assertions.assertThrows(
            IllegalArgumentException.class, () -> {
                corpus.getIndex(invalidVerse);
            }, translation.toString()
        );
        Assertions.assertEquals(
            String.format(
                "Invalid verse number contained in citation: %s", invalidVerse
            ), exception.getMessage(), translation.toString()
        );

        // Bookend verses of Bible
        Assertions.assertEquals(
            CorpusTest.IDX_OTSTART, corpus.getIndex(CorpusTest.OTSTART), translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.IDX_OTEND, corpus.getIndex(CorpusTest.OTEND), translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.IDX_NTSTART, corpus.getIndex(CorpusTest.NTSTART), translation.toString()
        );
        if (
            translation == Translations.Translation.AMPC
            || translation == Translations.Translation.ESV
        ) {
            Assertions.assertEquals(
                CorpusTest.IDX_NTEND + 1, corpus.getIndex(CorpusTest.NTEND), translation.toString()
            );
        } else {
            Assertions.assertEquals(
                CorpusTest.IDX_NTEND, corpus.getIndex(CorpusTest.NTEND), translation.toString()
            );
        }

        // Midpoint verses of Bible
        Assertions.assertEquals(
            CorpusTest.IDX_MID, corpus.getIndex(CorpusTest.MID), translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.IDX_OTMID, corpus.getIndex(CorpusTest.OTMID), translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.IDX_NTMID, corpus.getIndex(CorpusTest.NTMID), translation.toString()
        );
    }

    @ParameterizedTest
    @EnumSource(Translations.Translation.class)
    void testGetCitation(Translations.Translation translation) {
        Exception exception;

        Corpus corpus = Corpus.getInstance(translation);

        // Lower bound of valid index range exceeded
        exception = Assertions.assertThrows(
            IllegalArgumentException.class, () -> {
                corpus.getCitation(-1);
            }, translation.toString()
        );
        Assertions.assertEquals(
            "Invalid verse index: -1", exception.getMessage(), translation.toString()
        );

        // Upper bound of valid index range exceeded
        if (
            translation == Translations.Translation.AMPC
            || translation == Translations.Translation.ESV
        ) {
            exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    corpus.getCitation((CorpusTest.IDX_NTEND + 1) + 1);
                }, translation.toString()
            );
            Assertions.assertEquals(
                String.format(
                    "Invalid verse index: %d", (CorpusTest.IDX_NTEND + 1) + 1
                ), exception.getMessage(), translation.toString()
            );
        } else {
            exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    corpus.getCitation(CorpusTest.IDX_NTEND + 1);
                }, translation.toString()
            );
            Assertions.assertEquals(
                String.format(
                    "Invalid verse index: %d", CorpusTest.IDX_NTEND + 1
                ), exception.getMessage(), translation.toString()
            );
        }

        // Bookend verses of Bible
        Assertions.assertEquals(
            CorpusTest.OTSTART, corpus.getCitation(CorpusTest.IDX_OTSTART), translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.OTEND, corpus.getCitation(CorpusTest.IDX_OTEND), translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.NTSTART, corpus.getCitation(CorpusTest.IDX_NTSTART), translation.toString()
        );
        if (
            translation == Translations.Translation.AMPC
            || translation == Translations.Translation.ESV
        ) {
            Assertions.assertEquals(
                CorpusTest.NTEND,
                corpus.getCitation(CorpusTest.IDX_NTEND + 1),
                translation.toString()
            );
        } else {
            Assertions.assertEquals(
                CorpusTest.NTEND,
                corpus.getCitation(CorpusTest.IDX_NTEND),
                translation.toString()
            );
        }

        // Midpoint verses of Bible
        Assertions.assertEquals(
            CorpusTest.MID, corpus.getCitation(CorpusTest.IDX_MID), translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.OTMID, corpus.getCitation(CorpusTest.IDX_OTMID), translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.NTMID, corpus.getCitation(CorpusTest.IDX_NTMID), translation.toString()
        );
    }

    @ParameterizedTest
    @MethodSource("verseFactory")
    void testGetText(Translations.Translation translation, String text) {
        Exception exception;

        Corpus corpus = Corpus.getInstance(translation);

        // Upper bound of valid index range exceeded
        if (
            translation == Translations.Translation.AMPC
            || translation == Translations.Translation.ESV
        ) {
            exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    corpus.getText((CorpusTest.IDX_NTEND + 1) + 1);
                }, translation.toString()
            );
            Assertions.assertEquals(
                String.format(
                    "Invalid verse index: %d", (CorpusTest.IDX_NTEND + 1) + 1
                ), exception.getMessage(), translation.toString()
            );
        } else {
            exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    corpus.getText(CorpusTest.IDX_NTEND + 1);
                }, translation.toString()
            );
            Assertions.assertEquals(
                String.format(
                    "Invalid verse index: %d", CorpusTest.IDX_NTEND + 1
                ), exception.getMessage(), translation.toString()
            );
        }

        Assertions.assertNotNull(corpus.getText(CorpusTest.IDX_OTSTART), translation.toString());
        Assertions.assertNotNull(corpus.getText(CorpusTest.IDX_OTEND), translation.toString());
        Assertions.assertNotNull(corpus.getText(CorpusTest.IDX_NTSTART), translation.toString());
        Assertions.assertNotNull(corpus.getText(CorpusTest.IDX_NTEND), translation.toString());
        Assertions.assertNotNull(corpus.getText(CorpusTest.IDX_MID), translation.toString());
        Assertions.assertNotNull(corpus.getText(CorpusTest.IDX_OTMID), translation.toString());
        Assertions.assertNotNull(corpus.getText(CorpusTest.IDX_NTMID), translation.toString());

        Assertions.assertEquals(
            text, corpus.getText(new Citation("John", 3, 16)), translation.toString()
        );
    }
}
