package io.github.jacoblee23.bibleguesser.scriptures;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


public class CanonTest {
    @Test
    void testGetTestaments() {
        Canon canon = Canon.getInstance();

        List<String> testaments = Arrays.asList("Old Testament", "New Testament");        
        assertEquals(testaments, canon.getTestaments());
    }

    @Test
    void testGetGenres() {
        Canon canon = Canon.getInstance();

        List<String> genres = Arrays.asList(
            // Old Testament
            "Law",
            "Historical Narrative",
            "Wisdom and Poetry",
            "Major Prophets",
            "Minor Prophets",

            // New Testament
            "Gospel Accounts",
            "Early Church History",
            "Pauline Epistles",
            "General Epistles",
            "Apocalyptic Literature"
        );
        assertEquals(genres, canon.getGenres());
    }

    @Test
    void testGetBooks() {
        Canon canon = Canon.getInstance();

        List<String> books = Arrays.asList(
            /* Old Testament */

            // Law
            "Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy",

            // Historical Narrative
            "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings",
            "1 Chronicles", "2 Chronicles", "Ezra", "Nehemiah", "Esther",

            // Wisdom and Prophecy
            "Job", "Psalms", "Proverbs", "Ecclesiastes", "Song of Solomon",

            // Major Prophets
            "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel",

            // Minor Prophets
            "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah",
            "Haggai", "Zechariah", "Malachi",

            /* New Tesetament */

            // Gospel Accounts
            "Matthew", "Mark", "Luke", "John",

            // Early Church History
            "Acts",

            // Pauline Epistles
            "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians",
            "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus",
            "Philemon",

            // General Epistles
            "Hebrews", "James", "1 Peter", "2 Peter", "1 John", "2 John", "3 John", "Jude",

            // Apocalyptic Literature
            "Revelation"
        );
        assertEquals(books, canon.getBooks());
    }
}
