package io.github.jacoblee23.bibleguesser.scriptures;

import java.io.IOException;
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
    private static final String PATH = "/canon.yml";

    private static Canon instance = null;

    private final Map<String, Map<String, List<String>>> data;

    private Canon() {
        try (InputStream in = getClass().getResourceAsStream(Canon.PATH)) {
            Yaml yaml = new Yaml();
            this.data = new LinkedHashMap<>(yaml.load(in));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to configure record of Biblical canon");
        }
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
     * Lists the testament divisions that comprise the Biblical canon.
     *
     * @return The testament divisions of the Biblical canon
     */
    public List<String> listTestaments() {
        List<String> testaments = new LinkedList<>();
        this.data.forEach((key, value) -> { testaments.add(key); });

        return testaments;
    }

    /**
     * Lists the genre subdivisions that comprise a specified testament division of the Biblical
     * canon.
     *
     * @param testament A testament division
     * @return The genre subdivisions that comprise the specified testament
     */
    public List<String> listGenres(String testament) {
        List<String> genres = new LinkedList<>();
        this.data.get(testament).forEach((key, value) -> { genres.add(key); });

        return genres;
    }

    /**
     * Lists the genre subdivisions that comprise the Biblical canon.
     *
     * @return The genre subdivisions that comprise the Biblical canon
     */
    public List<String> listGenres() {
        List<String> genres = new LinkedList<>();
        for (String testament : this.listTestaments()) {
            genres.addAll(this.listGenres(testament));
        }
        return genres;
    }

    /**
     * Lists the books that comprise a specified genre subdivision of a specified testament division
     * of the Biblical canon.
     *
     * @param testament A testament division
     * @param genre A genre subdivision
     * @return The books that comprise the specified genre of the specified testament
     */
    public List<String> listBooks(String testament, String genre) {
        return this.data.get(testament).get(genre);
    }

    /**
     * Lists the books that comprise a specified testament division of the Biblical canon.
     *
     * @param testament A testament division
     * @return The books that comprise the specified testament
     */
    public List<String> listBooks(String testament) {
        List<String> books = new LinkedList<>();
        for (String genre : this.listGenres(testament)) {
            books.addAll(this.listBooks(testament, genre));
        }
        return books;
    }

    /**
     * Lists the books that comprise the Biblical canon.
     *
     * @return The books that comprise the Biblical canon
     */
    public List<String> listBooks() {
        List<String> books = new LinkedList<>();
        for (String testament : this.listTestaments()) {
            books.addAll(this.listBooks(testament));
        }
        return books;
    }
}
