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
    int count = 1;

    String upperPassword = password.toUpperCase(); // turns it to upper-case because it's meant to be case insensitive
   
    Map<Character, Integer> charCount = new HashMap<>(); // hash map stores characters and how often they appear
    for (char c : upperPassword.toCharArray()) {
        if (charCount.containsKey(c)) {
            
            charCount.put(c, charCount.get(c) + 1);
        } else {
            
            charCount.put(c, 1);
        }
    }

    //gets the character with highest corresponding Integer and returns it as int;
    for (int values : charCount.values()){ 
        if (values > count){
            count = values;
        }
    }

    return count;
}

  /**
   * Max Sequence length - The length of the longest ascending/descending sequence of alphabetical or numeric characters
   * eg: "4678" and "4321" would both have sequence length of 4
   * eg2: "cdefgh" would have a sequence length of 6
   * eg3: "password123" would have a max. sequence length of 3 - for the sequence of "123".
   * eg3a: "1pass2word3" would have a max. sequence length of 0 - as there is no sequence.
   * eg3b: "passwordABC" would have a max. sequence length of 3 - for the sequence of "ABC".
   * eg4: "AbCdEf" would have a sequence length of 6, even though it is mixed case.
   *
   * Check the supplied password.  Return true if the repetition count and sequence length are below or equal to the
   * specified maximum.  Otherwise, return false.
   * @param password
   * @return
   */
  public int getMaxSequenceLen(String password) {

    StringBuilder sb = new StringBuilder(); // filter out non alpha numeric characters
    for (int i = 0; i < password.length(); i++) {
        char c = password.charAt(i);
        if (Character.isLetterOrDigit(c)) {
            sb.append(c);
        }
    }
    String upperPassword = sb.toString().toUpperCase(); // make it uppercase as it's meant to be case insensitive

    int max = 0; // to be returned
    int tracker = 1; // tracks sequences
    
    for (int i = 0; i < upperPassword.length() - 1; i++) {
        if (upperPassword.charAt(i) + 1 == upperPassword.charAt(i + 1) || upperPassword.charAt(i) - 1 == upperPassword.charAt(i + 1)) {
            tracker++;
            max = Math.max(max, tracker); // checks to see which is larger to be returned
        } else {
            tracker = 1; // resets sequence when loop ends
        }
    }
    
    
    return max;

  }
}