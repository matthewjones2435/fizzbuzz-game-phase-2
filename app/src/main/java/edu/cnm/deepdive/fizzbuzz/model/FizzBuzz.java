package edu.cnm.deepdive.fizzbuzz.model;

/**
 * Includes several public methods and constants for computing FizzBuzz values.
 * <p> This class includes <code>static</code> methods that take <code>int</code> parameters and
 * return <code>boolean</code> and {@link String} results, indicating whether the specified
 * values are divisible by 3 (fizz) or 5 (buzz).</p>
 *
 * @author Matthew Jones &amp; Deep Dive Coding Java+Android Cohort 7.
 * @version 1.0.0
 */
public class FizzBuzz {

  /** {@link String} value indicating a number divisible by 3. */
  public static final String FIZZ = "Fizz";
  /** {@link String} value indicating a number divisible by 5. */
  public static final String BUZZ = "Buzz";

  private FizzBuzz() {}
  /**
   * Computes and returns "Fizz", "Buzz", or a {@link String} representation of <code>value</code>,
   * depending on whether <code>value</code> is divisble by 3 or 5.
   *
   * @param value integer value to be tested for divisibility by 3 or 5.
   * @return "fizz", "Buzz", or value as a String.
   */
  public static String fizzBuzzValue(int value) {
    if (value <= 0) {
      throw new IllegalArgumentException();
    }
    String result = "";
    if (isFizz(value)) {
      result += FIZZ;
    }
    if(isBuzz(value)){
      result += BUZZ;
    }
    if (result.isEmpty()){
      result += value;
    }
    return result;
  }

  /**
   * Computes and returns true if <code>value</code> is divisible by 3, otherwise false.
   * @param value integer <code>value</code> to be tested for divisibility by 3
   * @return rue or false depending on whether <code>value</code> is divisible by 3.
   */
  public static boolean isFizz(int value) {
    return value % 3 == 0;
  }

  /**
   * Computes and returns true if <code>value</code> is divisible by 5, otherwise false.
   * @param value integer <code>value</code> to be tested for divisibility by 5.
   * @return true or false depending on whether <code>value</code> is divisible by 5.
   */
  public static boolean isBuzz(int value) {
    return value % 5 == 0;
  }

}
