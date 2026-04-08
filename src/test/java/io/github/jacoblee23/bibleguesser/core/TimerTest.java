package io.github.jacoblee23.bibleguesser.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


public class TimerTest {
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
        Assertions.assertNull(timer.getStart());
        Assertions.assertNull(timer.getEnd());
        Assertions.assertEquals(timer.getDuration(), timer.getRemaining());

        System.setOut(out);
        timer.run();
        System.setOut(stdout);

        // Verify running state of timer
        Assertions.assertNotEquals(Duration.ZERO, timer.getRemaining());
        Assertions.assertNotNull(timer.getStart());
        Assertions.assertNotNull(timer.getEnd());
        Assertions.assertEquals(timer.getEnd(), timer.getStart().plus(timer.getDuration()));

        // Verify timer display
        Assertions.assertTrue(
            buffer.toString().contains(String.valueOf(Timer.UNCONSUMED).repeat(Timer.LINE_WIDTH))
        );
        Assertions.assertTrue(
            buffer.toString().contains(String.format("%02d:%02d.000", duration / 60, duration % 60))
        );
        buffer.reset();

        // Exhaust timer
        while (true) {
            System.setOut(out);
            Duration remaining = timer.update();
            System.setOut(stdout);

            if (remaining == Duration.ZERO) {
                break;
            }
            buffer.reset();
        }

        // Verify timer display
        Assertions.assertTrue(
            buffer.toString().contains(String.valueOf(Timer.CONSUMED).repeat(Timer.LINE_WIDTH))
        );
        Assertions.assertTrue(buffer.toString().contains("00:00.000"));
        buffer.reset();

        // Verify completed state of timer
        Assertions.assertEquals(Duration.ZERO, timer.getRemaining());
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
