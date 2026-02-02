package io.github.jacoblee23.bibleguesser.canon;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;


/**
 * Interfaces for encoding/decoding the names of the books of the Bible as/from abbreviations.
 */
public class Abbreviations {
    private static Abbreviations instance = null;
    private static final String PATH = "/abbreviations.csv";

    private final List<String> headers;
    private final Map<String, Map<String, String>> table;

    private Abbreviations() throws IOException {
        List<String[]> data;
        Reader reader = new InputStreamReader(
            getClass().getResourceAsStream(Abbreviations.PATH), StandardCharsets.UTF_8
        );
        try (CSVReader csvReader = new CSVReader(reader)) {
            data = csvReader.readAll();
        } catch (IOException | CsvException e) {
            throw new IOException(e);
        }

        this.headers = new LinkedList<>();
        this.table = new LinkedHashMap<>();
        for (String[] row : data) {
            if (this.headers.isEmpty()) {
                this.headers.addAll(Arrays.asList(row));
            } else {
                HashMap<String, String> entry = new HashMap<>();
                for (int i = 1; i < this.headers.size(); ++i) {
                    entry.put(this.headers.get(i), row[i]);
                }
                this.table.put(row[0], entry);
            }
        }
    }

    /**
     * Global access point to the class singleton.
     *
     * @return The single instance of the class
     * @throws IOException Unsuccessful input file read
     */
    public static Abbreviations getInstance() throws IOException {
        if (Abbreviations.instance == null) {
            Abbreviations.instance = new Abbreviations();
        }
        return Abbreviations.instance;
    }

    protected List<String> getHeaders() {
        return this.headers;
    }

    protected Map<String, Map<String, String>> getTable() {
        return this.table;
    }

    /**
     * Retrieves the abbreviations of the names of the books of the Bible according to a specified
     * abbreviation system.
     *
     * @param system A system used as an abbreviation encoder/decoder
     * @return The mapping used for abbreviation encoding/decoding
     */
    public Map<String, String> getAbbreviations(System system) {
        LinkedHashMap<String, String> entries = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : this.table.entrySet()) {
            entries.put(entry.getKey(), entry.getValue().get(system.getName()));
        }
        return entries;
    }

    /**
     * Encodes the name of a book of the Bible as an abbreviation according to a specified
     * abbreviation system.
     *
     * @param name The name of the book to abbreviate
     * @param system The system to use as an abbreviation encoder
     * @return The abbreviated book name
     */
    public String encode(String name, System system) {
        return this.table.get(name).get(system.getName());
    }

    /**
     * Decodes the name of a book of the Bible from an abbreviation.
     *
     * @param abbr The abbreviated book name to decode
     * @return The name of the book represented by the abbreviation
     */
    public String decode(String abbr) {
        for (Map.Entry<String, Map<String, String>> entry : this.table.entrySet()) {
            for (System system : System.values()) {
                if (entry.getValue().get(system.getName()).equals(abbr)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    /**
     * Decodes the name of a book of the Bible from an abbreviation according to a specified
     * abbreviation system.
     *
     * @param abbr The abbreviated book name to decode
     * @param system The system to use as an abbreviation decoder
     * @return The name of the book represented by the abbreviation
     */
    public String decode(String abbr, System system) {
        for (Map.Entry<String, Map<String, String>> entry : this.table.entrySet()) {
            if (entry.getValue().get(system.getName()).equals(abbr)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Identifies a system for encoding/decoding the names of the books of the Bible as/from
     * abbreviations.
     */
    public enum System {
        OSIS("OSIS"),
        PARATEXT("Paratext"),
        ESV("ESV");

        private final String name;

        private System(String name) {
            this.name = name;
        }

        /**
         * Represents the abbreviation system name as a string.
         *
         * @return The string representation of the abbreviation system name
         */
        public String getName() {
            return this.name;
        }
    };
}
