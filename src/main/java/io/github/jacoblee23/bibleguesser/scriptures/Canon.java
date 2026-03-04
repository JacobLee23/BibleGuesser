package io.github.jacoblee23.bibleguesser.scriptures;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;


/**
 * Interfaces for parsing the testament divisions and genre subdivisions of the Biblical canon.
 */
public class Canon {
    private static Canon instance = null;
    private static final String PATH = "/canon.yml";

    private final Map<String, Map<String, List<String>>> data;

    private Canon() {
        InputStream in = getClass().getResourceAsStream(Canon.PATH);
        Yaml yaml = new Yaml();

        this.data = new LinkedHashMap<>(yaml.load(in));
    }

    @Override
    public String toString() { return this.data.toString(); }

    /**
     * Global access point to the class singleton.
     *
     * @return The single instance of the class
     */
    public static Canon getInstance() {
        if (Canon.instance == null) {
            Canon.instance = new Canon();
        }
        return Canon.instance;
    }

    /**
     * Retrieves the list of testament divisions that comprise the Biblical canon.
     *
     * @return The testament divisions of the Biblical canon
     */
    public List<String> getTestaments() {
        List<String> testaments = new LinkedList<>();
        this.data.forEach((key, value) -> { testaments.add(key); });

        return testaments;
    }

    /**
     * Retrieves the list of genre subdivisions that comprise a specified testament division of the
     * Biblical canon.
     *
     * @param testament A testament division
     * @return The genre subdivisions that comprise the specified testament
     */
    public List<String> getGenres(String testament) {
        List<String> genres = new LinkedList<>();
        this.data.get(testament).forEach((key, value) -> { genres.add(key); });

        return genres;
    }

    /**
     * Retrieves the list of genre subdivisions that comprise the Biblical canon.
     *
     * @return The genre subdivisions that comprise the Biblical canon
     */
    public List<String> getGenres() {
        List<String> genres = new LinkedList<>();
        for (String testament : this.getTestaments()) {
            genres.addAll(this.getGenres(testament));
        }
        return genres;
    }

    /**
     * Retrieves the list of books that comprise a specified genre subdivision of a specified
     * testament division of the Biblical canon.
     *
     * @param testament A testament division
     * @param genre A genre subdivision
     * @return The books that comprise the specified genre of the specified testament
     */
    public List<String> getBooks(String testament, String genre) {
        return this.data.get(testament).get(genre);
    }

    /**
     * Retrieves the list of books that comprise a specified testament division of the Biblical
     * canon.
     *
     * @param testament A testament division
     * @return The books that comprise the specified testament
     */
    public List<String> getBooks(String testament) {
        List<String> books = new LinkedList<>();
        for (String genre : this.getGenres(testament)) {
            books.addAll(this.getBooks(testament, genre));
        }
        return books;
    }

    /**
     * Retrieves the list of books that comprise the Biblical canon.
     *
     * @return The books that comprise the Biblical canon
     */
    public List<String> getBooks() {
        List<String> books = new LinkedList<>();
        for (String testament : this.getTestaments()) {
            books.addAll(this.getBooks(testament));
        }
        return books;
    }
}
