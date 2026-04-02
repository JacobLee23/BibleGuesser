package io.github.jacoblee23.bibleguesser.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


public class TimerTest {
    // Timer accuracy (ms)
    private static final int TOLERANCE = 5;

    public static IntStream durationFactory() {
        return IntStream.of(-1, 0, 1, 2, 3);
    }

    @ParameterizedTest
    @MethodSource("durationFactory")
    void testRun(int duration) {
        Exception exception;

        if (duration <= 0) {
            exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> { new Timer(duration); }
            );
            Assertions.assertEquals(
                "Timer duration must be a positive integer number of seconds",
                exception.getMessage()
            );
            return;
        }
        Timer timer = new Timer(duration);

        PrintStream stdout = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer);

        // Verify initial state of timer
        Assertions.assertEquals(duration, timer.getDuration().getSeconds());
        Assertions.assertEquals(timer.getDuration(), timer.getRemaining());
        Assertions.assertNull(timer.getStart());
        Assertions.assertNull(timer.getEnd());

        System.setOut(out);
        timer.run();
        System.setOut(stdout);

        // Verify running state of timer
        Assertions.assertTrue(
            timer.getRemaining().getSeconds() <= timer.getDuration().getSeconds()
        );
        Assertions.assertNotNull(timer.getStart());
        Assertions.assertNotNull(timer.getEnd());
        Assertions.assertEquals(
            timer.getDuration().toSeconds(),
            timer.getStart().until(timer.getEnd(), ChronoUnit.SECONDS)
        );

        long remainder = Instant.now().until(timer.getEnd(), ChronoUnit.MILLIS);
        try {
            Thread.sleep(remainder - TimerTest.TOLERANCE);
        } catch (InterruptedException e) {
            Assertions.fail();
        }

        System.setOut(out);
        Assertions.assertFalse(timer.update());
        System.setOut(stdout);

        try {
            Thread.sleep(2 * TimerTest.TOLERANCE);
        } catch (InterruptedException e) {
            Assertions.fail();
        }

        System.setOut(out);
        Assertions.assertTrue(timer.update());
        System.setOut(stdout);

        // Verify completed state of timer
        Assertions.assertEquals(0, timer.getRemaining().getSeconds());
        Assertions.assertNotNull(timer.getStart());
        Assertions.assertNotNull(timer.getEnd());
        Assertions.assertEquals(
            timer.getDuration().toSeconds(),
            timer.getStart().until(timer.getEnd(), ChronoUnit.SECONDS)
        );
        exception = Assertions.assertThrows(
            IllegalStateException.class, () -> { timer.run(); }
        );
        Assertions.assertEquals(
            "Timer has not been reset to its initial state", exception.getMessage()
        );

        timer.reset();

        // Verify reset state of timer
        Assertions.assertEquals(timer.getDuration(), timer.getRemaining());
        Assertions.assertNull(timer.getStart());
        Assertions.assertNull(timer.getEnd());
    }
}
