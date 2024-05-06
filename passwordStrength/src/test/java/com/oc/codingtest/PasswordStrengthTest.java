package com.oc.codingtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PasswordStrengthTest {

  private static final Logger log = LoggerFactory.getLogger(PasswordStrengthTest.class);

  PasswordStrength ps = new PasswordStrength();

  private static Stream<Arguments> maxRepetitionsArgProvider() {
    return Stream.of(
      Arguments.of("abcdefg", 1),
      Arguments.of("password", 2),
      Arguments.of("touchwood", 3),
      // The check should be case-sensitive
      Arguments.of("TheQuickBrownFoxJumpsOverTheLazyDog", 3),

      //Additional test cases
      Arguments.of("", 0), // empty String
      Arguments.of(null, 0), // null
      Arguments.of("1", 1), // single digit
      Arguments.of("a", 1), // single letter
      Arguments.of("11111", 5), // same digits only
      Arguments.of("aaaaa", 5), // same letters only
      Arguments.of("abababababab", 6), // same max count of different letters
      Arguments.of("Aa", 1), // same letters mixed case
      Arguments.of("[", 1), // single special character
      Arguments.of("[[[}}}}", 4), // only special characters
      Arguments.of("[1[2[3[4[", 5), // special characters with digits
      Arguments.of("[a[b[c[d[", 5) // special characters with letters
    );
  }

  @ParameterizedTest
  @DisplayName("Max character repetition tests")
  @MethodSource("com.oc.codingtest.PasswordStrengthTest#maxRepetitionsArgProvider")
  void checkingMaxRepetitionsOnly(String password, int expectedResult) {
    assertEquals(expectedResult, ps.getMaxRepetitionCount(password));
  }

  private static Stream<Arguments> maxSequenceLenArgProvider() {
    return Stream.of(
      //simple ascending
      Arguments.of("abcdef", 6),
      //simple descending
      Arguments.of("fedcba", 6),
      //numeric ascending
      Arguments.of("0123456789", 10),
      //numeric descending
      Arguments.of("9876543210", 10),
      //ascending - mixed case
      Arguments.of("ABCdef", 6),
      //descending - mixed case
      Arguments.of("fedCBA", 6),

      //Exclude/handle non-alphanumeric chars
      Arguments.of("/012345678", 9),
      Arguments.of("0123456789:", 10),
      Arguments.of("01234*567:", 5),

      //Ascending and descending mixed
      Arguments.of("23454321", 5),

      //Additional test cases
      Arguments.of("", 0), // empty String
      Arguments.of(null, 0), // null
      Arguments.of("/*_-", 0), // only special characters
      Arguments.of("_a_", 1), // single letter lowercase surrounded by special characters
      Arguments.of("_A_", 1), // single letter uppercase surrounded by special characters
      Arguments.of("_1_", 1), // single digit surrounded by special characters
      Arguments.of("_12_", 2), // digit sequence surrounded by special characters
      Arguments.of("a", 1), // single letter lowercase
      Arguments.of("A", 1), // single letter uppercase
      Arguments.of("0", 1), // single digit
      Arguments.of("BcDeDcBa", 5), // ascending/descending mixed with mixed case
      Arguments.of("123456_54321", 6), // ascending/descending digits only with ascending max
      Arguments.of("12345_654321", 6), // ascending/descending digits only with descending max
      Arguments.of("aBcDe_dCbA", 5), // ascending/descending letter only with ascending max
      Arguments.of("aBcD_eDcBa", 5), // ascending/descending letter only with descending max
      Arguments.of("@AB", 2), // uppercase sequence according to ASCII table starting with special character
      Arguments.of("YZ[", 2), // uppercase sequence according to ASCII table ending with special character
      Arguments.of("za", 1), // wrap-around shouldn't be considered sequential
      Arguments.of("123456ABCDE", 6), // digits and uppercase sequence mixed
      Arguments.of("123_abcd_56789_efghijklmno_ab_4321", 11) // multiple sequences
    );
  }

  @ParameterizedTest
  @DisplayName("Max sequence length tests")
  @MethodSource("com.oc.codingtest.PasswordStrengthTest#maxSequenceLenArgProvider")
  void checkingMaxSequenceLenOnly(String password, int expectedResult) {
    assertEquals(expectedResult, ps.getMaxSequenceLen(password));
  }

}
