package io.github.jacoblee23.bibleguesser.scriptures;

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
import java.util.Set;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;


/**
 * Interfaces for encoding/decoding the names of the books of the Bible as/from abbreviations.
 */
public class Abbreviations {
    private static final String PATH = "/abbreviations.csv";

    private static Abbreviations instance = null;

    private final List<String> headers;
    private final Map<String, Map<String, String>> table;

    private Abbreviations(){
        List<String[]> data;
        try (
            Reader reader = new InputStreamReader(
                getClass().getResourceAsStream(Abbreviations.PATH), StandardCharsets.UTF_8
            );
            CSVReader csvReader = new CSVReader(reader)
        ) {
            data = csvReader.readAll();
        } catch (IOException | CsvException e) {
            throw new IllegalStateException(
                "Failed to configure record of abbreviations of the books of the Biblical canon"
            );
        }

        this.headers = new LinkedList<>();
        this.table = new LinkedHashMap<>();
        for (String[] row : data) {
            if (this.headers.isEmpty()) {
                this.headers.addAll(Arrays.asList(row));
                for (int i = 1; i < this.headers.size(); ++i) {
                    this.table.put(this.headers.get(i), new HashMap<>());
                }
            } else {
                for (int i = 1; i < this.headers.size(); ++i) {
                    this.table.get(this.headers.get(i)).put(row[0], row[i]);
                }
            }
        }
    }

    /**
     * Global access point to the class singleton.
     *
     * @return The single instance of the class
     */
    public static Abbreviations getInstance(){
        if (Abbreviations.instance == null) {
            Abbreviations.instance = new Abbreviations();
        }
        return Abbreviations.instance;
    }

    /**
     * Retrieves the abbreviations of the names of the books of the Bible according to a specified
     * abbreviation system.
     *
     * @param system A system used as an abbreviation encoder/decoder
     * @return The map used for abbreviation encoding/decoding
     */
    public Map<String, String> getMap(System system) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : this.table.get(system.toString()).entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * Encodes the name of a specified book of the Bible as its abbreviation according to a
     * specified abbreviation system.
     *
     * @param system The abbreviation system to use
     * @param name The name of the book to abbreviate
     * @return The abbreviated book name
     */
    public String encode(System system, String name) {
        Map<String, String> map = this.table.get(system.toString());
        if (map == null) {
            throw new IllegalArgumentException(
                String.format("Unknown abbreviation system: %s", system.toString())
            );
        }
        return map.get(name);
    }

    /**
     * Encodes the names of a specified set of books of the Bible as their abbreviations according
     * to a specified abbreviation system.
     *
     * @param system The abbreviation system to use
     * @param names The names of the books to abbreviate
     * @return The abbreviated book names
     */
    public Map<String, String> encode(System system, Set<String> names) {
        Map<String, String> map = this.table.get(system.toString());
        if (map == null) {
            throw new IllegalArgumentException(
                String.format("Unknown abbreviation system: %s", system.toString())
            );
        }

        Map<String, String> encodings = new HashMap<>();
        for (String name : names) {
            encodings.put(name, map.get(name));
        }
        return encodings;
    }

    /**
     * Decodes the name of a specified book of the Bible from its abbreviation according to a
     * specified abbreviation system.
     *
     * @param system The abbreviation system to use
     * @param abbrev The abbreviated book name to decode
     * @return The unabbreviated book name
     */
    public String decode(System system, String abbrev) {
        Map<String, String> map = this.table.get(system.toString());
        if (map == null) {
            throw new IllegalArgumentException(
                String.format("Unknown abbreviation system: %s", system.toString())
            );
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(abbrev)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Decoes the names of a specified set of books of the Bible from their abbreviations according
     * to a specified abbreviation system.
     *
     * @param system The abbreviation system to use
     * @param abbrevs The abbreviated book names to decode
     * @return The unabbreviated book names
     */
    public Map<String, String> decode(System system, Set<String> abbrevs) {
        Map<String, String> map = this.table.get(system.toString());
        if (map == null) {
            throw new IllegalArgumentException(
                String.format("Unknown abbreviation system: %s", system.toString())
            );
        }
        
        Map<String, String> decodings = new HashMap<>();
        for (String abbrev : abbrevs) {
            decodings.put(abbrev, null);
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (abbrevs.contains(entry.getValue())) {
                decodings.put(entry.getValue(), entry.getKey());
            }
        }
        return decodings;
    }

    /**
     * Identifies a system for encoding/decoding the names of the books of the Bible as/from
     * abbreviations.
     */
    public enum System {
        OSIS("OSIS"),
        PARATEXT("Paratext"),
        ESV("ESV"),
        BIBLICA("Biblica");

        private final String name;

        private System(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    };
}
