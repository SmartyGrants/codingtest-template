package com.oc.codingtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import static java.util.stream.Collectors.*;


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
   * @param password
   * @return
   */
  public int getMaxRepetitionCount(String password) {
    // Initialization and checks for password
    HashMap<Character, Integer> hashMap = new HashMap<>();
    if (password == null || password.isEmpty()){
      return 0;
    }
    for (char c : password.toCharArray()) {
      if (hashMap.containsKey(c)) {
          // If the character is already in the hashMap, increment its value
          hashMap.put(c, hashMap.get(c) + 1);
      } else {
          // If the character is not in the hashMap, add it with a count of 1
            hashMap.put(c, 1);
          }
    }
    // Returning the max repeated value
    return Collections.max(hashMap.values());
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
   * Check the supplied password.  Return true if the repetition count and sequence length are below or equal to the
   * specified maximum.  Otherwise, return false.
   * @param password
   * @return
   */
  public int getMaxSequenceLen(String password) {
    // Initialization and checks for password
    int longestLength = 0, currentAscLength = 0, currentDescLength = 0;
    if (password == null || password.isEmpty()){
      return 0;
    }
    // Create an IntStream of characters (represented as ASCII integers)
    int[] arr = password.chars().map(ch -> {
      char c = (char) ch;
      if (Character.isLetterOrDigit(c)) {
        // If the character is a letter or digit, convert it to lowercase and return its ASCII value
        return Character.toLowerCase(c);
      } else {
        // If the character is a special character return 0
        return 0;
      }
    }).toArray();
    // Iterate through the array to find max sequence in asc or desc order
    for (int i = 1; i < arr.length; i++) {
      int difference = arr[i] - arr[i - 1];
      // use a switch case to increment the counters
      switch (difference) {
        case 1:
          currentAscLength++;
          currentDescLength = 0;
          break;
        case -1:
          currentDescLength++;
          currentAscLength = 0;
          break;
        default:
          currentAscLength = 0;
          currentDescLength = 0;
      }
      // find the max sequence and return it
      longestLength = Math.max(longestLength, Math.max(currentAscLength, currentDescLength));
    }
    return longestLength != 0 ? longestLength + 1 : longestLength;
  }
}
