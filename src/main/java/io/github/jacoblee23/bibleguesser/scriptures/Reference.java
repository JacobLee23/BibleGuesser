package io.github.jacoblee23.bibleguesser.scriptures;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Models a Biblical reference.
 */
public class Reference {
    private static final Pattern REGEX = Pattern.compile(
        String.format("^(%s) (\\d+):(\\d+)$", String.join("|", Reference.listBooks()))
    );

    private final String book;
    private final int chapter;
    private final int verse;

    // Index of the book of reference within the Biblical canon
    private final int index;

    /**
     * Constructs a Biblical reference comprising the specified book, chapter, and verse.
     *
     * @param book The name of the book of the reference
     * @param chapter The chapter number of the reference
     * @param verse The verse number of the reference
     */
    public Reference(String book, int chapter, int verse) {
        List<String> books = Reference.listBooks();
        this.index = books.indexOf(book);
        if (this.index == -1) {
            throw new IllegalArgumentException(
                String.format("Unknown book: %s", book)
            );
        }
        this.book = book;

        if (!(chapter > 0 && chapter <= 0xFF)) {
            throw new IllegalArgumentException(
                String.format("Invalid chapter number referenced: %d", chapter)
            );
        }
        this.chapter = chapter;

        if (!(verse > 0 && verse <= 0xFF)) {
            throw new IllegalArgumentException(
                String.format("Invalid verse number referenced: %d", verse)
            );
        }
        this.verse = verse;
    }

    public static Reference parse(String reference) {
        Matcher match = Reference.REGEX.matcher(reference);
        if (!match.matches()) {
            return null;
        }

        String book = match.group(1);
        int chapter = Integer.parseInt(match.group(2));
        int verse = Integer.parseInt(match.group(3));

        return new Reference(book, chapter, verse);
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
        if (obj.getClass() != getClass()) {
            return false;
        }
        Reference other = (Reference) obj;

        return (
            this.book.equals(other.book)
            && this.chapter == other.chapter
            && this.verse == other.verse
        );
    }

    @Override
    public String toString() {
        return String.format("%s %d:%d", this.book, this.chapter, this.verse);
    }

    /**
     * Retrieves the name of the book of the reference.
     *
     * @return The name of the book of the reference
     */
    public String getBook() {
        if (this.book.equals("Psalm")) {
            return "Psalms";
        }
        return this.book;
    }

    /**
     * Retrieves the chapter number of the reference.
     *
     * @return The chapter number of the reference
     */
    public int getChapter() {
        return this.chapter;
    }

    /**
     * Retrieves the verse number of the reference.
     *
     * @return The verse number of the reference
     */
    public int getVerse() {
        return this.verse;
    }

    /**
     * Determines whether the reference precedes another specified reference.
     *
     * @param other Another reference against which to compare the reference
     * @return Whether the reference precedes the other specified reference
     */
    public boolean precedes(Reference other) {
        return this.hashCode() < other.hashCode();
    }

    /**
     * Determines whether the reference succeeds another specified reference.
     *
     * @param other Another reference against which to compare the reference
     * @return Whether the reference precedes the other specified reference
     */
    public boolean succeeds(Reference other) {
        return this.hashCode() > other.hashCode();
    }

    protected static List<String> listBooks() {
        Canon canon = Canon.getInstance();
        List<String> books = canon.getBooks();
        books.set(books.indexOf("Psalms"), "Psalm");

        return books;
    }
}
