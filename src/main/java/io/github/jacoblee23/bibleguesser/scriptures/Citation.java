package io.github.jacoblee23.bibleguesser.scriptures;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Interfaces for parsing biblical citations.
 */
public class Citation {
    private static final String FORMAT = "%s %d:%d";
    private static final Pattern REGEX = Pattern.compile("^([\\w\\s]+) (\\d+):(\\d+)$");

    private final String book;
    private final int chapter;
    private final int verse;

    // Index of the book referenced by the citation within the Biblical canon
    private final int index;

    /**
     * Constructs a biblical citation comprising the specified book, chapter, and verse.
     *
     * @param book The name of the book referenced by the citation
     * @param chapter The chapter number referenced by the citation
     * @param verse The verse number referenced by the citation
     */
    public Citation(String book, int chapter, int verse) {
        Canon canon = Canon.getInstance();
        List<String> books = canon.listBooks();
        this.index = books.indexOf(book);
        if (this.index == -1) {
            throw new IllegalArgumentException(
                String.format("Unrecognized book: %s", book)
            );
        }
        this.book = book;

        if (chapter <= 0 || chapter > 0xFF) {
            throw new IllegalArgumentException(
                String.format("Invalid chapter number: %d", chapter)
            );
        }
        this.chapter = chapter;

        if (verse <= 0 || verse > 0xFF) {
            throw new IllegalArgumentException(
                String.format("Invalid verse number: %d", verse)
            );
        }
        this.verse = verse;
    }

    public static Citation parse(String reference) {
        Matcher match = Citation.REGEX.matcher(reference);
        if (!match.matches()) {
            return null;
        }

        String book = match.group(1);
        int chapter = Integer.parseInt(match.group(2));
        int verse = Integer.parseInt(match.group(3));

        return new Citation((book.equals("Psalm") ? "Psalms" : book), chapter, verse);
    }

    @Override
    public int hashCode() {
        // Maximum book index:      65  (0x41)
        // Maximum chapter number:  150 (0x8C)
        // Maximum verse number:    176 (0xB0)

        return (this.index << 16) | (this.chapter << 8) | this.verse;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Citation other = (Citation) obj;

        return this.hashCode() == other.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
            Citation.FORMAT, (
                this.book.equals("Psalms") ? "Psalm" : this.book
            ), this.chapter, this.verse
        );
    }

    /**
     * Retrieves the name of the book referenced by the citation.
     *
     * @return The name of the book referenced by the citation
     */
    public String getBook() {
        return this.book;
    }

    /**
     * Retrieves the chapter number referenced by the citation.
     *
     * @return The chapter number referenced by the citation
     */
    public int getChapter() {
        return this.chapter;
    }

    /**
     * Retrieves the verse number referenced by the citation.
     *
     * @return The verse number referenced by the citation
     */
    public int getVerse() {
        return this.verse;
    }
}
