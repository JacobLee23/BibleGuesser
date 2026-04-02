package io.github.jacoblee23.bibleguesser.core;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


/**
 * Interfaces for creating and running a non-blocking timer.
 */
public class Timer {
    // Total character-width of display
    private static final int LINE_WIDTH = 80;

    // Characters for designating the consumed/unconsumed portions of the bar display
    private static final char CONSUMED = '\u2591';
    private static final char UNCONSUMED = '\u2588';

    // Characters for padding the time display
    private static final char PADDING = ' ';
    private static final char FILLCHAR = '-';

    // Flag to mute the timer display
    private boolean muted;

    // Total timer duration
    private final Duration duration;

    // Remaining timer duration
    private Duration remaining;

    // Timer start/end time
    private Instant start;
    private Instant end;

    /**
     * Initializes a timer with a specified duration.
     *
     * @param duration The total duration of the timer
     * @param muted Whether to mute the timer display
     */
    public Timer(Duration duration, boolean muted) {
        if (duration.getSeconds() <= 0) {
            throw new IllegalArgumentException(
                "Timer duration must be a positive integer number of seconds"
            );
        }
        this.duration = duration;
        this.remaining = Duration.from(this.duration);
        this.start = null;
        this.end = null;

        this.muted = muted;
    }

    /**
     * Initializes a timer with a specified duration.
     *
     * @param duration The total duration of the timer
     */
    public Timer(Duration duration) {
        this(duration, false);
    }

    /**
     * Initializes a timer with a specified duration.
     *
     * @param duration The total duration of the timer (s)
     * @param muted Whether to muted the timer display
     */
    public Timer(int duration, boolean muted) {
        this(Duration.ofSeconds(duration), muted);
    }

    /**
     * Initializes a timer with a specified duration.
     *
     * @param duration The total duration of the timer (s)
     */
    public Timer(int duration) {
        this(duration, false);
    }

    @Override
    public String toString() {
        return String.format(
            "%02d:%02d", this.remaining.getSeconds() / 60, this.remaining.getSeconds() % 60
        );
    }

    /**
     * Retrieves whether the timer display is muted.
     *
     * @return Whether the timer display is muted
     */
    public boolean isMuted() {
        return this.muted;
    }

    /**
     * Mutes the timer display.
     */
    public void mute() {
        this.muted = true;
    }

    /**
     * Unmutes the timer display.
     */
    public void unmute() {
        this.muted = false;
    }

    /**
     * Retrieves the total duration of the timer
     *
     * @return The total duration of the timer
     */
    public Duration getDuration() {
        return Duration.from(this.duration);
    }

    /**
     * Retrieves the remaining duration of the timer.
     *
     * @return The remaining duration of the timer
     */
    public Duration getRemaining() {
        return Duration.from(this.remaining);
    }

    /**
     * Retrieves the starting time of the timer.
     *
     * @return The starting time of the timer
     */
    public Instant getStart() {
        return (this.start == null) ? null : Instant.from(this.start);
    }

    /**
     * Retrieves the ending time of the timer.
     *
     * @return The ending time of the timer
     */
    public Instant getEnd() {
        return (this.end == null) ? null : Instant.from(this.end);
    }

    /**
     * Runs the timer.
     */
    public void run() {
        if (!(this.start == null && this.end == null)) {
            throw new IllegalStateException("Timer has not been reset to its initial state");
        }

        if (!this.muted) {
            this.display();
        }

        this.start = Instant.now();
        this.end = this.start.plus(this.duration);
    }

    /**
     * Updates the timer, checking if it has reached completion. If it has not, updates `stdout`
     * with a bar and time display of the remaining duration of the timer.
     *
     * @return Whether the timer has completed
     */
    public boolean update() {
        Instant instant = Instant.now();
        if (instant.isAfter(this.end)) {
            return true;
        }

        this.remaining = Duration.ofSeconds(instant.until(this.end, ChronoUnit.SECONDS));
        
        if (!this.muted) {
            System.out.print("\u001B[2A\r");    // Position cursor to overwrite display
            this.display();
        }

        return false;
    }

    /**
     * Resets the timer to its initial state.
     */
    public void reset() {
        this.remaining = Duration.from(this.duration);
        this.start = null;
        this.end = null;
    }

    private void display() {
        System.out.println(this.formatBar());
        System.out.println(this.formatTime());
    }

    private String formatBar() {
        float ratio = (float) this.remaining.getSeconds() / this.duration.getSeconds();
        String consumed = String.valueOf(Timer.CONSUMED).repeat(
            Math.round((1 - ratio) * Timer.LINE_WIDTH)
        );
        String unconsumed = String.valueOf(Timer.UNCONSUMED).repeat(
            Math.round(ratio * Timer.LINE_WIDTH)
        );

        return consumed + unconsumed;
    }

    private String formatTime() {
        String time = String.format("%c%s%c", Timer.PADDING, this.toString(), Timer.PADDING);
        int pad = Timer.LINE_WIDTH - time.length();
        int lpad = pad / 2;
        int rpad = pad - lpad;
        String lpadding = String.valueOf(Timer.FILLCHAR).repeat(lpad);
        String rpadding = String.valueOf(Timer.FILLCHAR).repeat(rpad);

        return lpadding + time + rpadding;
    }
}
