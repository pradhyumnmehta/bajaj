package com.bajaj.bajaDemo.service;

import com.bajaj.bajaDemo.dto.BfhlRequest;
import com.bajaj.bajaDemo.dto.BfhlResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of BfhlService.
 *
 * Business rules:
 *  - A token is a "number"  if ALL its characters are digits.
 *  - A token is an "alphabet" if ALL its characters are letters (a-z / A-Z).
 *  - Everything else is a "special character" token.
 *  - Numbers are classified as odd/even based on their numeric value.
 *  - Alphabets are returned in UPPERCASE.
 *  - sum  = sum of all numeric tokens (as integer arithmetic), returned as String.
 *  - concat_string = all alphabetical characters (from every token, in order)
 *                    collected, reversed, then alternating caps starting with
 *                    UPPER for index 0 of the reversed sequence.
 */
@Service
public class BfhlServiceImpl implements BfhlService {

    // ---- Hardcoded user details (replace with your own) ----
    private static final String USER_ID     = "Pradhyumn Mehta";
    private static final String EMAIL       = "pradhyumnmehta231355@acropolis.in";
    private static final String ROLL_NUMBER = "0827RL231050";

    @Override
    public BfhlResponse process(BfhlRequest request) {

        List<String> data = request.getData();

        List<String> oddNumbers       = new ArrayList<>();
        List<String> evenNumbers      = new ArrayList<>();
        List<String> alphabets        = new ArrayList<>();
        List<String> specialChars     = new ArrayList<>();
        long         numericSum       = 0;

        // Collect all individual alphabetical characters (in input order)
        // for the concat_string computation.
        StringBuilder allAlphaChars = new StringBuilder();

        for (String token : data) {
            if (isNumber(token)) {
                // Numbers returned as strings (requirement)
                long value = Long.parseLong(token);
                numericSum += value;
                if (value % 2 == 0) {
                    evenNumbers.add(token);
                } else {
                    oddNumbers.add(token);
                }
                // Numbers do NOT contribute to allAlphaChars

            } else if (isAlphabet(token)) {
                // Return uppercase
                alphabets.add(token.toUpperCase());
                // Collect each character for concat_string
                for (char c : token.toCharArray()) {
                    allAlphaChars.append(c);
                }

            } else {
                // Special character token
                specialChars.add(token);
            }
        }

        // Build concat_string:
        // 1. Reverse the collected alphabetical characters
        // 2. Apply alternating caps: index 0 → UPPER, index 1 → lower, ...
        String concatString = buildConcatString(allAlphaChars.toString());

        BfhlResponse response = new BfhlResponse();
        response.setSuccess(true);
        response.setUserId(USER_ID);
        response.setEmail(EMAIL);
        response.setRollNumber(ROLL_NUMBER);
        response.setOddNumbers(oddNumbers);
        response.setEvenNumbers(evenNumbers);
        response.setAlphabets(alphabets);
        response.setSpecialCharacters(specialChars);
        response.setSum(String.valueOf(numericSum));
        response.setConcatString(concatString);

        return response;
    }

    // ---- Helpers ----

    /**
     * Returns true if every character in the token is a digit (handles multi-digit numbers).
     */
    private boolean isNumber(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    /**
     * Returns true if every character in the token is a letter.
     */
    private boolean isAlphabet(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isLetter(c)) return false;
        }
        return true;
    }

    /**
     * Reverses the collected alpha characters, then applies alternating caps
     * starting with UPPERCASE at position 0.
     *
     * Example: "ayb" → reversed "bya" → "ByA"
     */
    private String buildConcatString(String alphaChars) {
        if (alphaChars.isEmpty()) return "";

        String reversed = new StringBuilder(alphaChars).reverse().toString();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (i % 2 == 0) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }
}
