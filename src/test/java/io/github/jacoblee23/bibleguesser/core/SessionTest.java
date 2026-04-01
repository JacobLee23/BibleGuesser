package io.github.jacoblee23.bibleguesser.core;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.jacoblee23.bibleguesser.scriptures.Canon;
import io.github.jacoblee23.bibleguesser.scriptures.Translations;


public class SessionTest {
    public static IntStream lengthFactory() {
        return IntStream.rangeClosed(
            Collections.min(Session.Configurations.LENGTH_VALUES) - 1,
            Collections.max(Session.Configurations.LENGTH_VALUES) + 1
        );
    }

    public static IntStream limitFactory() {
        return IntStream.rangeClosed(
            Collections.min(Session.Configurations.TLIMIT_VALUES) - 1,
            Collections.max(Session.Configurations.TLIMIT_VALUES) + 1
        );
    }

    public static Stream<Arguments> scopeFactory() {
        Canon canon = Canon.getInstance();
        return Stream.of(
            Arguments.of(List.of()),
            Arguments.of(List.of("foo")),
            Arguments.of(List.of("foo", "bar")),
            Arguments.of(List.of("foo", "bar", "baz")),
            Arguments.of(canon.listBooks()),
            Arguments.of(canon.listBooks("Old Testament")),
            Arguments.of(canon.listBooks("New Testament")),
            Arguments.of(canon.listBooks("Old Testament", "Law")),
            Arguments.of(canon.listBooks("Old Testament", "Historical Narrative")),
            Arguments.of(canon.listBooks("Old Testament", "Wisdom and Poetry")),
            Arguments.of(canon.listBooks("Old Testament", "Major Prophets")),
            Arguments.of(canon.listBooks("Old Testament", "Minor Prophets")),
            Arguments.of(canon.listBooks("New Testament", "Gospel Accounts")),
            Arguments.of(canon.listBooks("New Testament", "Early Church History")),
            Arguments.of(canon.listBooks("New Testament", "Pauline Epistles")),
            Arguments.of(canon.listBooks("New Testament", "General Epistles")),
            Arguments.of(canon.listBooks("New Testament", "Apocalyptic Literature"))
        );
    }

    public static IntStream visibilityFactory() {
        return IntStream.rangeClosed(
            Collections.min(Session.Configurations.VISIBILITY_VALUES) - 1,
            Collections.max(Session.Configurations.VISIBILITY_VALUES) + 1
        );
    }

    @ParameterizedTest
    @MethodSource("lengthFactory")
    void testConfigurationsLength(int length) {
        Session.Configurations configurations = new Session.Configurations();
        if (Session.Configurations.LENGTH_VALUES.contains(length)) {
            configurations.setLength(length);
            Assertions.assertEquals(length, configurations.getLength());
        } else {
            Exception exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    configurations.setLength(length);
                }
            );
            Assertions.assertEquals(
                String.format(
                    "Invalid session length configuration value: %d", length
                ), exception.getMessage()
            );
        }
    }

    @ParameterizedTest
    @EnumSource(Translations.Translation.class)
    void testConfigurationsTranslation(Translations.Translation translation) {
        Session.Configurations configurations = new Session.Configurations();
        configurations.setTranslation(translation);
        Assertions.assertEquals(translation, configurations.getTranslation());
    }

    @ParameterizedTest
    @MethodSource("limitFactory")
    void testConfigurationsTLimit(int tlimit) {
        Session.Configurations configurations = new Session.Configurations();
        if (Session.Configurations.TLIMIT_VALUES.contains(tlimit)) {
            configurations.setTLimit(tlimit);
            Assertions.assertEquals(tlimit, configurations.getTLimit());
        } else {
            Exception exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    configurations.setTLimit(tlimit);
                }
            );
            Assertions.assertEquals(
                String.format(
                    "Invalid time limit configuration value: %d", tlimit
                ), exception.getMessage()
            );
        }
    }

    @ParameterizedTest
    @MethodSource("scopeFactory")
    void testConfigurationsScope(List<String> scope) {
        Canon canon = Canon.getInstance();
        List<String> books = canon.listBooks();

        Session.Configurations configurations = new Session.Configurations();
        if (scope.isEmpty()) {
            Exception exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    configurations.setScope(scope);
                }
            );
            Assertions.assertEquals(
                String.format(
                    "Invalid scope configuration value: %s", scope
                ), exception.getMessage()
            );
        } else {
            if (books.containsAll(scope)) {
                configurations.setScope(scope);
                Assertions.assertEquals(scope, configurations.getScope());
            } else {
                Exception exception = Assertions.assertThrows(
                    IllegalArgumentException.class, () -> {
                        configurations.setScope(scope);
                    }
                );
                Assertions.assertEquals(
                    String.format(
                        "Invalid scope configuration value: %s", scope
                    ), exception.getMessage()
                );
            }
        }
    }

    @ParameterizedTest
    @MethodSource("visibilityFactory")
    void testConfigurationsVisibility(int visibility) {
        Session.Configurations configurations = new Session.Configurations();
        if (Session.Configurations.VISIBILITY_VALUES.contains(visibility)) {
            configurations.setVisibility(visibility);
            Assertions.assertEquals(visibility, configurations.getVisibility());
        } else {
            Exception exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    configurations.setVisibility(visibility);
                }
            );
            Assertions.assertEquals(
                String.format(
                    "Invalid visibility configuration value: %d", visibility
                ), exception.getMessage()
            );
        }
    }
}
