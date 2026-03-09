package io.github.jacoblee23.bibleguesser.scriptures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;


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

    // Bookened verses of the Bible
    private static final Citation OT_START = new Citation("Genesis", 1, 1);
    private static final Citation OT_END = new Citation("Malachi", 4, 6);
    private static final Citation NT_START = new Citation("Matthew", 1, 1);
    private static final Citation NT_END = new Citation("Revelation", 22, 21);

    // Midpoint verses of the Bible
    private static final Citation MIDPOINT = new Citation("Psalms", 103, 1);
    private static final Citation OT_MIDPOINT = new Citation("2 Chronicles", 18, 30);
    private static final Citation NT_MIDPOINT = new Citation("Acts", 7, 7);

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
            0, corpus.getIndex(CorpusTest.OT_START), translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.NVERSES_OT - 1, corpus.getIndex(CorpusTest.OT_END), translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.NVERSES_OT, corpus.getIndex(CorpusTest.NT_START), translation.toString()
        );
        if (
            translation == Translations.Translation.AMPC
            || translation == Translations.Translation.ESV
        ) {
            Assertions.assertEquals(
                CorpusTest.NVERSES_TOTAL,
                corpus.getIndex(CorpusTest.NT_END),
                translation.toString()
            );
        } else {
            Assertions.assertEquals(
                CorpusTest.NVERSES_TOTAL - 1,
                corpus.getIndex(CorpusTest.NT_END),
                translation.toString()
            );
        }

        // Midpoint verses of Bible
        Assertions.assertEquals(
            (CorpusTest.NVERSES_TOTAL + 1) / 2 - 1,
            corpus.getIndex(CorpusTest.MIDPOINT),
            translation.toString()
        );
        Assertions.assertEquals(
            (CorpusTest.NVERSES_OT + 1) / 2 - 1,
            corpus.getIndex(CorpusTest.OT_MIDPOINT),
            translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.NVERSES_OT + (CorpusTest.NVERSES_NT + 1) / 2 - 1,
            corpus.getIndex(CorpusTest.NT_MIDPOINT),
            translation.toString()
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
                    corpus.getCitation(CorpusTest.NVERSES_TOTAL + 1);
                }, translation.toString()
            );
            Assertions.assertEquals(
                String.format(
                    "Invalid verse index: %d", CorpusTest.NVERSES_TOTAL + 1
                ), exception.getMessage(), translation.toString()
            );
        } else {
            exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    corpus.getCitation(CorpusTest.NVERSES_TOTAL);
                }, translation.toString()
            );
            Assertions.assertEquals(
                String.format(
                    "Invalid verse index: %d", CorpusTest.NVERSES_TOTAL
                ), exception.getMessage(), translation.toString()
            );
        }

        // Bookend verses of Bible
        Assertions.assertEquals(
            CorpusTest.OT_START, corpus.getCitation(0), translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.OT_END, corpus.getCitation(CorpusTest.NVERSES_OT - 1), translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.NT_START, corpus.getCitation(CorpusTest.NVERSES_OT), translation.toString()
        );
        if (
            translation == Translations.Translation.AMPC
            || translation == Translations.Translation.ESV
        ) {
            Assertions.assertEquals(
                CorpusTest.NT_END,
                corpus.getCitation(CorpusTest.NVERSES_TOTAL),
                translation.toString()
            );
        } else {
            Assertions.assertEquals(
                CorpusTest.NT_END,
                corpus.getCitation(CorpusTest.NVERSES_TOTAL - 1),
                translation.toString()
            );
        }

        // Midpoint verses of Bible
        Assertions.assertEquals(
            CorpusTest.MIDPOINT,
            corpus.getCitation((CorpusTest.NVERSES_TOTAL + 1) / 2 - 1),
            translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.OT_MIDPOINT,
            corpus.getCitation((CorpusTest.NVERSES_OT + 1) / 2 - 1),
            translation.toString()
        );
        Assertions.assertEquals(
            CorpusTest.NT_MIDPOINT,
            corpus.getCitation(CorpusTest.NVERSES_OT + (CorpusTest.NVERSES_NT + 1) / 2 - 1),
            translation.toString()
        );
    }
}
