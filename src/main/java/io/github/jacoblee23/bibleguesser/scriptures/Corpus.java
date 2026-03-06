package io.github.jacoblee23.bibleguesser.scriptures;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Interfaces for parsing the corpus of Scripture.
 */
public class Corpus {
    private static final Map<Translations.Translation, Corpus> INSTANCES = new HashMap<>();

    // Regular expression for parsing copies of the following translations:
    //  AKJV, ASV, BSB, CPDV, DBT, DRB, ERV, JPS, KJV, SLT, WBT, WEB, WEY, YLT
    private static final Pattern REGEX_A = Pattern.compile("^([\\w\\s]+) (\\d+):(\\d+)\t(.*)$");
    
    // Regular expression for parsing copies of the following translations:
    //  AMPC, NASB, NET
    private static final Pattern REGEX_B = Pattern.compile("^(.*) -- ([\\w\\s]+) (\\d+):(\\d+)$");

    // Regular expression for parsing copies of the following translations:
    //  ESV
    private static final Pattern REGEX_C = Pattern.compile("^(\\w{3}) (\\d+):(\\d+) (.*)$");

    private final Translations.Translation translation;
    private final Map<String, Map<Integer, Integer>> counts;
    private final Map<String, Map<Integer, Integer>> linenos;

    private Corpus(Translations.Translation translation) {
        this.translation = translation;

        this.counts = new LinkedHashMap<>();
        this.linenos = new LinkedHashMap<>();
        
        this.parseText();
    }

    /**
     * Global access point to the class multiton.
     *
     * @param translation A Bible translation
     * @return The instance of the class corresponding to the specified translation
     */
    public static Corpus getInstance(Translations.Translation translation) {
        Corpus corpus = Corpus.INSTANCES.get(translation);
        if (corpus == null) {
            corpus = new Corpus(translation);
            Corpus.INSTANCES.put(translation, corpus);
        }
        return corpus;
    }

    /**
     * Retrieves the Bible translation corresponding to the corpus.
     *
     * @return The Bible translation corresponding to the corpus
     */
    public Translations.Translation getTranslation() {
        return this.translation;
    }

    /**
     * Retrieves the number of chapters that comprise the Bible.
     *
     * @return The number of chapters in the Bible
     */
    public int nchapters() {
        int total = 0;
        for (Map<Integer, Integer> chapters : this.counts.values()) {
            total += chapters.size();
        }
        return total;
    }

    /**
     * Retrieves the number of chapters that comprise a specified book of the Bible.
     *
     * @param book A book of the Bible
     * @return The number of chapters in the specified book
     */
    public int nchapters(String book) {
        Map<Integer, Integer> chapters = this.counts.get(book);
        if (chapters == null) {
            throw new IllegalArgumentException(
                String.format("[%s] Unrecognized book: %s", this.translation, book)
            );
        }
        return chapters.size();
    }

    /**
     * Retrieves the number of verses that comprise the Bible.
     *
     * @return The number of verses in the Bible.
     */
    public int nverses() {
        int total = 0;
        for (Map<Integer, Integer> chapters : this.counts.values()) {
            for (Integer count : chapters.values()) {
                total += count;
            }
        }
        return total;
    }

    /**
     * Retrieves the number of verses that comprise a specific book of the Bible.
     *
     * @param book A book of the Bible
     * @return The number of verses in the specified book
     */
    public int nverses(String book) {
        Map<Integer, Integer> chapters = this.counts.get(book);
        if (chapters == null) {
            throw new IllegalArgumentException(
                String.format("[%s] Unrecognized book: %s", this.translation, book)
            );
        }

        int total = 0;
        for (int count : chapters.values()) {
            total += count;
        }
        return total;
    }

    /**
     * Retrieves the number of verses that comprise a specified chapter of the Bible.
     *
     * @param book A book of the Bible
     * @param chapter A chapter number
     * @return The number of verses in the specified chapter
     */
    public int nverses(String book, int chapter) {
        Map<Integer, Integer> chapters = this.counts.get(book);
        if (chapters == null) {
            throw new IllegalArgumentException(
                String.format("[%s] Unrecognized book: %s", this.translation, book)
            );
        }

        Integer count = chapters.get(chapter);
        if (count == null) {
            throw new IllegalArgumentException(
                String.format(
                    "[%s] Unrecognized chapter: %s %d", this.translation, book, chapter
                )
            );
        }

        return count;
    }

    private void parseText() {
        Canon canon = Canon.getInstance();
        Abbreviations abbreviations = Abbreviations.getInstance();
        Translations translations = Translations.getInstance();

        Map<String, String> lowercase = new HashMap<>();

        this.counts.clear();
        this.linenos.clear();
        for (String book : canon.listBooks()) {
            lowercase.put(book.toLowerCase(), book);

            this.counts.put(book, new HashMap<>());
            this.linenos.put(book, new HashMap<>());
        }

        String path = translations.getResourceName(this.translation);
        try (
            InputStream in = getClass().getResourceAsStream(path);
            Reader reader = new InputStreamReader(in);
            BufferedReader buffer = new BufferedReader(reader)
        ) {
            String line;
            int lineno = 0;
            while ((line = buffer.readLine()) != null) {
                lineno += 1;

                Pattern regex = switch (this.translation) {
                    case AKJV, ASV, BSB, CPDV, DBT, DRB, ERV, JPS, KJV, SLT, WBT, WEB, WEY, YLT -> Corpus.REGEX_A;
                    case AMPC, NASB, NET -> Corpus.REGEX_B;
                    case ESV -> Corpus.REGEX_C;
                };
                Matcher match = regex.matcher(line);
                if (!match.matches()) {
                    continue;
                }

                String book;
                int chapter;
                int verse;
                switch (this.translation) {
                    case AKJV, ASV, BSB, CPDV, DBT, DRB, ERV, JPS, KJV, SLT, WBT, WEB, WEY, YLT -> {
                        book = match.group(1);
                        if (book.isEmpty()) {
                            throw new IllegalStateException(
                                String.format(
                                    "[%s] Failed to parse reference from line: %s",
                                    this.translation, line
                                )
                            );
                        }
                        if (book.equals("Psalm")) {
                            book = "Psalms";
                        }
                        chapter = Integer.parseInt(match.group(2));
                        verse = Integer.parseInt(match.group(3));
                    }
                    case AMPC, NASB, NET -> {
                        book = match.group(2);
                        if (book.isEmpty()) {
                            throw new IllegalStateException(
                                String.format(
                                    "[%s] Failed to parse reference from line: %s",
                                    this.translation, line
                                )
                            );
                        }
                        if (book.equals("psalm")) {
                            book = "psalms";
                        }
                        book = lowercase.get(book);
                        chapter = Integer.parseInt(match.group(3));
                        verse = Integer.parseInt(match.group(4));
                    }
                    case ESV -> {
                        book = match.group(1);
                        if (book.isEmpty()) {
                            throw new IllegalStateException(
                                String.format(
                                    "[%s] Failed to parse reference from line: %s",
                                    this.translation, line
                                )
                            );
                        }
                        book = abbreviations.decode(
                            Abbreviations.System.BIBLICA, book.toUpperCase()
                        );
                        if (book == null) {
                            throw new IllegalStateException(
                                String.format(
                                    "[%s] Failed to parse reference from line: %s",
                                    this.translation, line
                                )
                            );
                        }
                        chapter = Integer.parseInt(match.group(2));
                        verse = Integer.parseInt(match.group(3));
                    }
                    default -> {
                        throw new IllegalArgumentException(
                            String.format("Unknown Bible translation: %s", this.translation)
                        );
                    }
                }

                // Record length of chapter
                Map<Integer, Integer> chapterCounts = this.counts.get(book);
                if (chapterCounts == null) {
                    throw new IllegalArgumentException(
                        String.format("[%s] Unknown book: %s", this.translation, book)
                    );
                }
                chapterCounts.put(chapter, verse);

                // Record line number of first verse of chapter
                if (verse == 1) {
                    Map<Integer, Integer> chapterLinenos = this.linenos.get(book);
                    if (chapterLinenos == null) {
                        throw new IllegalArgumentException(
                            String.format("[%s] Unknown book: %s", this.translation, book)
                        );
                    }
                    chapterLinenos.put(chapter, lineno);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                String.format(
                    "Failed to parse copy of %s translation", this.translation
                )
            );
        }
    }
}
