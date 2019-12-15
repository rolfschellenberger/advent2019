package nl.rolf.advent2019.day4;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class Day4 {

    @Scheduled(fixedDelay = 5000)
    public void run() throws IOException {
        final int start = 357253;
        final int end = 892942;
        System.out.println("Day4.1: " + getPasswordCount(start, end));
        System.out.println("Day4.2: " + getPasswordCount2(start, end));
    }

    private int getPasswordCount(final int start, final int end) {
        int result = 0;
        for (int i = start; i <= end; i++) {
            if (isPassword(i)) {
                result++;
            }
        }
        return result;
    }

    boolean isPassword(final int i) {
        final char[] digits = String.valueOf(i).toCharArray();

        // Length of 6
        if (digits.length != 6) {
            return false;
        }

        // Inspect digits
        final Set<Character> uniqueDigits = new HashSet<>();
        char last = digits[0];
        for (char digit : digits) {
            // Are the digits sorted?
            if (digit < last) {
                return false;
            }
            last = digit;
            uniqueDigits.add(digit);
        }

        // At least 1 double digit?
        return uniqueDigits.size() < digits.length;
    }

    private int getPasswordCount2(final int start, final int end) {
        int result = 0;
        for (int i = start; i <= end; i++) {
            if (isPassword2(i)) {
                result++;
            }
        }
        return result;
    }

    boolean isPassword2(final int i) {
        final char[] digits = String.valueOf(i).toCharArray();

        // Length of 6
        if (digits.length != 6) {
            return false;
        }

        // Inspect digits
        final Map<Character, Integer> uniqueDigits = new HashMap<>();
        char last = digits[0];
        for (char digit : digits) {
            // Are the digits sorted?
            if (digit < last) {
                return false;
            }
            last = digit;

            // Count the digits.
            final int count = uniqueDigits.getOrDefault(digit, 0);
            uniqueDigits.put(digit, count + 1);
        }

        // At least 1 double digit?
        if (uniqueDigits.size() >= digits.length) {
            return false;
        }

        // Even double digits.
        boolean foundSingleDoubleDigit = false;
        for (Map.Entry<Character, Integer> pair : uniqueDigits.entrySet()) {
            final int count = pair.getValue();
            if (count == 2) {
                foundSingleDoubleDigit = true;
            }
        }

        // All good.
        return foundSingleDoubleDigit;
    }
}
