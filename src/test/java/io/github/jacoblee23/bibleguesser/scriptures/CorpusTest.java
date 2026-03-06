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
}
