package io.github.jacoblee23.bibleguesser.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import io.github.jacoblee23.bibleguesser.scriptures.Canon;
import io.github.jacoblee23.bibleguesser.scriptures.Corpus;
import io.github.jacoblee23.bibleguesser.scriptures.Translations;


/**
 * Models a configurable session.
 */
public class Session {
    private final Configurations configurations;
    private final List<Round> rounds;

    /**
     * Initializes a session with the specified configurations.
     * 
     * @param configurations The setting values with which to configure the session
     */
    public Session(Configurations configurations) {
        this.configurations = configurations;
        this.rounds = new ArrayList<>();
    }

    /**
     * Initializes a session with the default setting configurations.
     */
    public Session() {
        this.configurations = new Configurations();
        this.rounds = new ArrayList<>();
    }

    /**
     * Retrieves the setting configurations of the session.
     *
     * @return The setting values with which the session has been configured
     */
    public Configurations getConfigurations() {
        return this.configurations;
    }

    /**
     * Runs the session to completion.
     */
    public void run() {
        // Load corpus of Scripture based on translation
        Corpus corpus = Corpus.getInstance(this.configurations.translation);
        int nverses = 0;
        for (String book : Session.this.configurations.scope) {
            nverses += corpus.nverses(book);
        }

        Random random = new Random();
        for (int i = 1; i <= this.configurations.length; ++i) {
            // Select target verse
            int target = random.nextInt(nverses - 1);

            Round round = new Round(i);
            this.rounds.add(round);

            round.run();
        }
    }

    /**
     * Models a set of session configurations.
     */
    static public class Configurations {
        // Constraints
        protected static final List<Integer> LENGTH_VALUES = List.of(
            1, 2, 3, 4, 5, 10, 15, 20, 25
        );
        private static final int TLIMIT_MIN = 0;
        private static final int TLIMIT_MAX = 600;
        private static final int TLIMIT_STEP = 15;
        protected static final List<Integer> TLIMIT_VALUES = IntStream.iterate(
            Configurations.TLIMIT_MIN,
            i -> i <= Configurations.TLIMIT_MAX,
            i -> i + Configurations.TLIMIT_STEP
        ).boxed().toList();
        protected static final List<Integer> VISIBILITY_VALUES = List.of(
            0, 1, 2, 3, 4, 5, 10, 15, 20, 25, 50, 75, 100
        );

        // Defaults
        protected static final int LENGTH_DEF = 5;
        protected static final Translations.Translation TRANSLATION_DEF = Translations.Translation.ESV;
        protected static final int TLIMIT_DEF = 0;
        protected static final List<String> SCOPE_DEF = Canon.getInstance().listBooks();
        protected static final int VISIBILITY_DEF = 10;

        // Values
        private int length;
        private Translations.Translation translation;
        private int tlimit;
        private List<String> scope;
        private int visibility;

        /**
         * Initializes a set of session configurations with default values.
         */
        public Configurations() {
            this.length = Configurations.LENGTH_DEF;
            this.translation = Configurations.TRANSLATION_DEF;
            this.tlimit = Configurations.TLIMIT_DEF;
            this.scope = Configurations.SCOPE_DEF;
            this.visibility = Configurations.VISIBILITY_DEF;
        }

        @Override
        public int hashCode() {
            int h = 17;

            h = 31 * h + this.length;
            h = 31 * h + Arrays.asList(Translations.Translation.values()).indexOf(this.translation);
            h = 31 * h + this.tlimit;
            h = 31 * h + this.scope.hashCode();
            h = 31 * h + this.visibility;

            return h;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Configurations)) {
                return false;
            }
            Configurations other = (Configurations) obj;

            return (
                other.length == this.length
                && other.translation == this.translation
                && other.tlimit == this.tlimit
                && other.scope.equals(this.scope)
                && other.visibility == this.visibility
            );
        }

        /**
         * Retrieves the configured session length.
         *
         * @return The configured session length
         */
        public int getLength() {
            return this.length;
        }

        /**
         * Configures the session length.
         *
         * @param length The value with which to configure the session length
         */
        public void setLength(int length) {
            if (!Configurations.LENGTH_VALUES.contains(length)) {
                throw new IllegalArgumentException(
                    String.format("Invalid session length configuration value: %d", length)
                );
            }
            this.length = length;
        }

        /**
         * Retrieves the configured biblical translation.
         *
         * @return The configured biblical translation
         */
        public Translations.Translation getTranslation() {
            return this.translation;
        }

        /**
         * Configures the biblical translation.
         *
         * @param translation The value with which to configure the biblical translation
         */
        public void setTranslation(Translations.Translation translation) {
            this.translation = translation;
        }

        /**
         * Retrieves the configured time limit.
         *
         * @return The configured time limit
         */
        public int getTLimit() {
            return this.tlimit;
        }

        /**
         * Configures the time limit.
         *
         * @param tlimit The value with which to configure the time limit
         */
        public void setTLimit(int tlimit) {
            if (!Configurations.TLIMIT_VALUES.contains(tlimit)) {
                throw new IllegalArgumentException(
                    String.format("Invalid time limit configuration value: %d", tlimit)
                );
            }
            this.tlimit = tlimit;
        }

        /**
         * Retrieves the configured scope.
         *
         * @return The configured scope
         */
        public List<String> getScope() {
            return new ArrayList<>(this.scope);
        }

        /**
         * Configures the scope.
         *
         * @param scope The value with which to configure the scope
         */
        public void setScope(List<String> scope) {
            if (scope.isEmpty()) {
                throw new IllegalArgumentException(
                    String.format("Invalid scope configuration value: %s", scope)
                );
            }

            Canon canon = Canon.getInstance();
            List<String> books = canon.listBooks();
            scope.forEach(
                (book) -> {
                    if (!books.contains(book)) {
                        throw new IllegalArgumentException(
                            String.format("Invalid scope configuration value: %s", scope)
                        );
                    }
                }
            );
            this.scope = new ArrayList<>(scope);
        }

        /**
         * Retrieves the configured visibility.
         *
         * @return The configured visibility
         */
        public int getVisibility() {
            return this.visibility;
        }

        /**
         * Configures the visibility.
         *
         * @param visibility The value with which to configure the visibility
         */
        public void setVisibility(int visibility) {
            if (!Configurations.VISIBILITY_VALUES.contains(visibility)) {
                throw new IllegalArgumentException(
                    String.format("Invalid visibility configuration value: %d", visibility)
                );
            }
            this.visibility = visibility;
        }
    }

    /**
     * Models a single round of a session.
     */
    private class Round {
        // Total character-width of display
        private static final int LINE_WIDTH = 80;

        // Characters for padding the display
        private static final char PADDING = ' ';
        private static final char FILLCHAR = '=';

        private final int number;

        private final Timer timer;

        /**
         * Initializes a single round of a session.
         *
         * @param number
         */
        public Round(int number) {
            if (number <= 0 || number > Session.this.configurations.length) {
                throw new IllegalArgumentException(
                    String.format("Invalid round number: %d", number)
                );
            }
            this.number = number;
            if (Session.this.configurations.tlimit == 0) {
                this.timer = null;
            } else {
                this.timer = new Timer(Session.this.configurations.tlimit);
            }
        }

        /**
         * Runs the round to completion.
         */
        public void run() {
            System.out.println(this.formatRound());
            if (timer != null) {
                this.timer.run();
                while (!this.timer.update()) {}
            }
        }

        private String formatRound() {
            String round = String.format(
                "%cRound %d/%d%c", Round.PADDING, this.number, Session.this.configurations.length,
                Round.PADDING
            );
            int pad = Round.LINE_WIDTH - round.length();
            int lpad = pad / 2;
            int rpad = pad - lpad;
            String lpadding = String.valueOf(Round.FILLCHAR).repeat(lpad);
            String rpadding = String.valueOf(Round.FILLCHAR).repeat(rpad);

            return lpadding + round + rpadding;
        }
    }
}
