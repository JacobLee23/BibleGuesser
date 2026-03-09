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

    private static Pattern getLineRegEx(Translations.Translation translation) {
        return switch (translation) {
            case AKJV, ASV, BSB, CPDV, DBT, DRB, ERV, JPS, KJV, SLT, WBT, WEB, WEY, YLT -> Corpus.REGEX_A;
            case AMPC, NASB, NET -> Corpus.REGEX_B;
            case ESV -> Corpus.REGEX_C;
        };
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

    /**
     * Retrieves the zero-based index of a verse within the Scriptures specified by its citation.
     *
     * @param citation The citation referencing a verse to index
     * @return The index of the verse within the Scriptures
     */
    public int getIndex(Citation citation) {
        if (!this.counts.containsKey(citation.getBook())) {
            throw new IllegalArgumentException(
                String.format("Invalid book contained in citation: %s", citation)
            );
        }
        if (!this.counts.get(citation.getBook()).containsKey(citation.getChapter())) {
            throw new IllegalArgumentException(
                String.format("Invalid chapter number contained in citation: %s", citation)
            );
        }

        int index = -1;
        for (Map.Entry<String, Map<Integer, Integer>> bookEntry : this.counts.entrySet()) {
            for (Map.Entry<Integer, Integer> chapterEntry : bookEntry.getValue().entrySet()) {
                if (
                    bookEntry.getKey().equals(citation.getBook())
                    && chapterEntry.getKey().equals(citation.getChapter())
                ) {
                    if (citation.getVerse() <= this.nverses(bookEntry.getKey(), chapterEntry.getKey())) {
                        return index + citation.getVerse();
                    }
                    throw new IllegalArgumentException(
                        String.format("Invalid verse number contained in citation: %s", citation)
                    );
                }
                index += chapterEntry.getValue();
            }
        }
        throw new IllegalArgumentException(String.format("Invalid citation: %s", citation));
    }

    /**
     * Retrieves the citation of a verse specified by its zero-based index within the Scriptures.
     *
     * @param index The zero-based index of a verse within the Scriptures
     * @return The citation referencing the verse
     */
    public Citation getCitation(int index) {
        if (index < 0) {
            throw new IllegalArgumentException(String.format("Invalid verse index: %d", index));
        }
        int count = index + 1;

        for (Map.Entry<String, Map<Integer, Integer>> bookEntry : this.counts.entrySet()) {
            for (Map.Entry<Integer, Integer> chapterEntry : bookEntry.getValue().entrySet()) {
                if (count <= chapterEntry.getValue()) {
                    return new Citation(bookEntry.getKey(), chapterEntry.getKey(), count);
                }
                count -= chapterEntry.getValue();
            }
        }
        throw new IllegalArgumentException(String.format("Invalid verse index: %d", index));
    }

    private void parseText() {
        Canon canon = Canon.getInstance();
        Translations translations = Translations.getInstance();

        this.counts.clear();
        this.linenos.clear();
        for (String book : canon.listBooks()) {
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

                Matcher match = this.parseLine(line);
                if (match == null) {
                    continue;
                }

                Citation citation;
                try {
                    citation = this.extractCitation(match);
                } catch (IllegalArgumentException e) {
                    throw new IllegalStateException(
                        String.format(
                            "[%s] Failed to extract citation from line: %s", this.translation, line
                        )
                    );
                }

                // Record length of chapter
                Map<Integer, Integer> chapterCounts = this.counts.get(citation.getBook());
                if (chapterCounts == null) {
                    throw new IllegalArgumentException(
                        String.format(
                            "[%s] Invalid book contained in citation: %s",
                            this.translation,
                            citation
                        )
                    );
                }
                chapterCounts.put(citation.getChapter(), citation.getVerse());

                // Record line number of first verse of chapter
                if (citation.getVerse() == 1) {
                    Map<Integer, Integer> chapterLinenos = this.linenos.get(citation.getBook());
                    if (chapterLinenos == null) {
                        throw new IllegalArgumentException(
                            String.format(
                                "[%s] Invalid book contained in citation: %s",
                                this.translation,
                                citation
                            )
                        );
                    }
                    chapterLinenos.put(citation.getChapter(), lineno);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                String.format("[%s] Failed to parse corpus", this.translation)
            );
        }
    }

    private Matcher parseLine(String line) {
        Pattern regex = Corpus.getLineRegEx(this.translation);
        Matcher match = regex.matcher(line);
        if (!match.matches()) {
            return null;
        }
        return match;
    }

    private Citation extractCitation(Matcher match) {
        Canon canon = Canon.getInstance();
        Abbreviations abbreviations = Abbreviations.getInstance();

        String book, chapter, verse;
        switch (this.translation) {
            case AKJV, ASV, BSB, CPDV, DBT, DRB, ERV, JPS, KJV, SLT, WBT, WEB, WEY, YLT -> {
                book = match.group(1);
                chapter = match.group(2);
                verse = match.group(3);
                if (book.isEmpty() || chapter.isEmpty() || verse.isEmpty()) {
                    throw new IllegalArgumentException(
                        String.format("Failed to extract citation: %s", match)
                    );
                }
                if (book.equals("Psalm")) {
                    book = "Psalms";
                }
            }
            case AMPC, NASB, NET -> {
                book = match.group(2);
                chapter = match.group(3);
                verse = match.group(4);
                if (book.isEmpty() || chapter.isEmpty() || verse.isEmpty()) {
                    throw new IllegalArgumentException(
                        String.format("Failed to extract citation: %s", match)
                    );
                }
                book = canon.normalizeBook(
                    (book.equals("psalm") ? "psalms" : book), String::toLowerCase
                );
                if (book == null) {
                    throw new IllegalArgumentException(
                        String.format("Failed to extract citation: %s", match)
                    );
                }
            }
            case ESV -> {
                book = match.group(1);
                chapter = match.group(2);
                verse = match.group(3);
                if (book.isEmpty() || chapter.isEmpty() || verse.isEmpty()) {
                    throw new IllegalArgumentException(
                        String.format("Failed to extract citation: %s", match)
                    );
                }
                book = abbreviations.decode(
                    Abbreviations.System.BIBLICA, book.toUpperCase()
                );
                if (book == null) {
                    throw new IllegalArgumentException(
                        String.format("Failed to extract citation: %s", match)
                    );
                }
            }
            default -> {
                throw new IllegalArgumentException(
                    String.format("Invalid Bible translation: %s", this.translation)
                );
            }
        }
        return new Citation(book, Integer.parseInt(chapter), Integer.parseInt(verse));
    }

    private String extractText(Matcher match) {
        String text = switch (this.translation) {
            case AKJV, ASV, BSB, CPDV, DBT, DRB, ERV, JPS, KJV, SLT, WBT, WEB, WEY, YLT -> match.group(4);
            case AMPC, NASB, NET -> match.group(1);
            case ESV -> match.group(4);
        };
        if (text.isEmpty()) {
            throw new IllegalArgumentException(String.format("Failed to extract text: %s", match));
        }
        return text;
    }
}
