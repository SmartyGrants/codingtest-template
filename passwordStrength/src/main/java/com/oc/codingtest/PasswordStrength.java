package com.oc.codingtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PasswordStrength {

  private static final Logger log = LoggerFactory.getLogger(PasswordStrength.class);

  public boolean isPasswordPermissible(String password, int maxAllowedRepetitionCount, int maxAllowedSequenceLength) {
    // This method accepts a password (String) and calculates two password strength parameters:
    // Repetition count and Max Sequence length
    return getMaxRepetitionCount(password) <= maxAllowedRepetitionCount
             && getMaxSequenceLen(password) <= maxAllowedSequenceLength;
  }

  /**
   * Repetition count - the number of occurrences of the *most repeated* character within the password
   *   eg1: "Melbourne" has a repetition count of 2 - for the 2 non-consecutive "e" characters.
   *   eg2: "passwords" has a repetition count of 3 - for the 3 "s" characters
   *   eg3: "lucky" has a repetition count of 1 - each character appears only once.
   *   eg4: "Elephant" has a repetition count of 1 - as the two "e" characters have different cases (ie one "E", one "e")
   * The repetition count should be case-sensitive.
   * @param password The password to get the maximum repetition count of characters from
   * @return The maximum repetition count as described above, 0 if the password is null or empty
   */
  public int getMaxRepetitionCount(String password) {
    if (password == null || password.isEmpty()) {
      log.warn("Max Repetition Count: Password is null or empty, returning 0");
      return 0;
    }

    // store the counts for each character in a map for quick access
    // If we wanted to avoid the potential re-hashing the map for performance reasons we could calculate the initial
    // capacity based on the password length but that seems like unnecessary optimisation
    HashMap<Character, Integer> charCount = new HashMap<>();
    for (char c : password.toCharArray()) {
      int count = charCount.getOrDefault(c, 0) + 1; // increase the count for the character
      charCount.put(c, count);
    }

    // find the max of all the counted values.
    return Collections.max(charCount.values());
  }

  /**
   * Max Sequence length - The length of the longest ascending/descending sequence of alphabetical or numeric characters
   * eg: "4678" and "4321" would both have sequence length of 4
   * eg2: "cdefgh" would have a sequence length of 6
   * eg3: "password123" would have a max. sequence length of 3 - for the sequence of "123".
   * eg3a: "1pass2word3" would have a max. sequence length of 0 - as there is no sequence.
   * eg3b: "passwordABC" would have a max. sequence length of 3 - for the sequence of "ABC".
   * eg4: "AbCdEf" would have a sequence length of 6, even though it is mixed case.
   * eg5: "ABC_DEF" would have a sequence length of 3, because the special character breaks the progression
   * @param password The password to get the maximum sequence length of characters from
   * @return The maximum sequence length as described above, 0 if the password is null or empty
   */
  public int getMaxSequenceLen(String password) {
    if (password == null || password.isEmpty()) {
      log.warn("Max Sequence Length: Password is null or empty, returning 0");
      return 0;
    }

    // It's possible to find the max sequence from left to right and right to left in
    // a single loop without having to reverse the password String. But that is harder to read and follow.
    // This could always be done if the absolute maximum performance was a requirement but given that passwords
    // typically are between 6 and 20 characters long, the performance impact should be negligible.
    int maxSequenceLengthLeftToRight = findMaxSequenceLengthLeftToRight(password);
    int maxSequenceLengthRightToLeft = findMaxSequenceLengthLeftToRight(reverseString(password));

    return Math.max(maxSequenceLengthLeftToRight, maxSequenceLengthRightToLeft); // determine the longer sequence
  }

  private int findMaxSequenceLengthLeftToRight(String password) {
    // no need to check the password for null or empty again as this method is
    // only called internally from where these conditions already have been checked

    int maxSeqLength = 0;
    int currentSeqLength = 0;
    char prevChar = password.charAt(0);
    for (char currentChar : password.toCharArray()) {
      if (Character.isLetterOrDigit(currentChar)) {
        // either increase the current sequence count or start a new one
        currentSeqLength = isSequentialIgnoreCase(prevChar, currentChar) ? currentSeqLength + 1 : 1;
      } else {
        currentSeqLength = 0; // reset the current sequence count as it was interrupted by a special character
      }
      maxSeqLength = Math.max(maxSeqLength, currentSeqLength); // determine the maximum sequence count so far
      prevChar = currentChar;
    }
    return maxSeqLength;
  }

  private boolean isSequentialIgnoreCase(char previous, char current) {
    // use the fact that in the ASCII table, characters have corresponding numerical values
    int diff = Character.toLowerCase(current) - Character.toLowerCase(previous);
    return diff == 1;
  }

  private String reverseString(String str) {
    StringBuilder sb = new StringBuilder(str);
    return sb.reverse().toString();
  }
}
