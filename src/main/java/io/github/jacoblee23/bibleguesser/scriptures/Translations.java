package io.github.jacoblee23.bibleguesser.scriptures;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;


/**
 * Interfaces for identifying Bible translations.
 */
public class Translations {
    private static Translations instance = null;
    private static final String PATH = "/translations.csv";

    private final Map<String, String> table;

    private Translations() throws IOException {
        List<String[]> data;
        Reader reader = new InputStreamReader(
            getClass().getResourceAsStream(Translations.PATH), StandardCharsets.UTF_8
        );
        try (CSVReader csvReader = new CSVReader(reader)) {
            data = csvReader.readAll();
        } catch (IOException | CsvException e) {
            throw new IOException(e);
        }

        this.table = new HashMap<>();
        for (String[] row : data) {
            this.table.put(row[0], row[1]);
        }
    }

    /**
     * Global access point to the class singleton.
     *
     * @return The single instance of the class
     * @throws IOException Unsuccessful read of input configuration file
     */
    public static Translations getInstance() throws IOException {
        if (Translations.instance == null) {
            Translations.instance = new Translations();
        }
        return Translations.instance;
    }

    protected Map<String, String> getTable() {
        return this.table;
    }

    /**
     * Retrieves the unabbreviated name of a Bible translation.
     *
     * @param version A Bible translation
     * @return The unabbreviated name of the Bible translation
     */
    public String getName(Version version) {
        return this.table.get(version.toString());
    }

    /**
     * Identifies a Bible translation.
     */
    public enum Version {
        AKJV("AKJV"),
        ASV("ASV"),
        BSB("BSB"),
        CPDV("CPDV"),
        DBT("DBT"),
        DRB("DRB"),
        ERV("ERV"),
        JPS("JPS"),
        KJV("KJV"),
        SLT("SLT"),
        WBT("WBT"),
        WEB("WEB"),
        YLT("YLT");

        private final String abbrev;

        private Version(String abbrev) {
            this.abbrev = abbrev;
        }

        @Override
        public String toString() {
            return this.abbrev;
        }
    }
}
