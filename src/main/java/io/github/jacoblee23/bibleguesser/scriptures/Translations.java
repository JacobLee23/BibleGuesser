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
    protected static final String PATH = "/translations.csv";
    protected static final String DIRECTORY = "/translations";

    private static Translations instance = null;

    private final Map<String, String> table;

    private Translations() {
        List<String[]> data;
        Reader reader = new InputStreamReader(
            getClass().getResourceAsStream(Translations.PATH), StandardCharsets.UTF_8
        );
        try (CSVReader csvReader = new CSVReader(reader)) {
            data = csvReader.readAll();
        } catch (IOException | CsvException e) {
            throw new IllegalStateException("Failed to configure record of Bible translations");
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
     */
    public static Translations getInstance() {
        if (Translations.instance == null) {
            Translations.instance = new Translations();
        }
        return Translations.instance;
    }

    /**
     * Retrieves the unabbreviated name of a Bible translation.
     *
     * @param translation A Bible translation
     * @return The unabbreviated name of the Bible translation
     */
    public String getName(Translation translation) {
        return this.table.get(translation.toString());
    }

    /**
     * Retrieves the absolute name of the resource that is the plaintext file containing the corpus
     * of Scripture that corresponds to the specified Bible translation.
     *
     * @param translation A Bible translation
     * @return The absolute name of the corresponding resource
     */
    public String getResourceName(Translation translation) {
        return String.format(
            "%s/%s.txt", Translations.DIRECTORY, translation.toString().toLowerCase()
        );
    }

    /**
     * Identifies a Bible translation.
     */
    public static enum Translation {
        AKJV("AKJV"),
        AMPC("AMPC"),
        ASV("ASV"),
        BSB("BSB"),
        CPDV("CPDV"),
        DBT("DBT"),
        DRB("DRB"),
        ERV("ERV"),
        ESV("ESV"),
        JPS("JPS"),
        KJV("KJV"),
        NASB("NASB"),
        NET("NET"),
        SLT("SLT"),
        WBT("WBT"),
        WEB("WEB"),
        WEY("WEY"),
        YLT("YLT");

        private final String abbrev;

        private Translation(String abbrev) {
            this.abbrev = abbrev;
        }

        @Override
        public String toString() {
            return this.abbrev;
        }
    }
}