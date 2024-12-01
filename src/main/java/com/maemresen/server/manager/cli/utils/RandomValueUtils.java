package com.maemresen.server.manager.cli.utils;

import java.util.Random;
import lombok.experimental.UtilityClass;

/**
 * A utility class for generating random values, such as integers within a range or booleans based
 * on a probability.
 *
 * <p>This class provides static methods to perform common random value generation tasks using a
 * {@link Random} instance. The methods include generating a random integer within a specified range
 * and generating a boolean value based on a specified probability.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * int randomInt = RandomValueUtils.randomIntBetween(1, 10);
 * boolean randomBool = RandomValueUtils.randomBoolean(75);
 * }</pre>
 *
 * <p>Note: This class is annotated with {@link lombok.experimental.UtilityClass}, indicating that
 * it is a utility class and should not be instantiated.
 */
@UtilityClass
public class RandomValueUtils {
  private static final Random random = new Random();

  /**
   * Generates a random integer between the specified minimum and maximum values (inclusive).
   *
   * @param min the minimum value (inclusive)
   * @param max the maximum value (inclusive)
   * @return a random integer between {@code min} and {@code max} (inclusive)
   * @throws IllegalArgumentException if {@code min} is greater than {@code max}
   */
  public static int randomIntBetween(int min, int max) {
    return random.nextInt((max - min) + 1) + min;
  }

  /**
   * Generates a random boolean value based on the given probability.
   *
   * @param probability the probability of returning {@code true}, expressed as a percentage (0-100)
   * @return {@code true} with the specified probability, otherwise {@code false}
   * @throws IllegalArgumentException if {@code probability} is not between 0 and 100
   */
  public static boolean randomBoolean(int probability) {
    if (probability < 0 || probability > 100) {
      throw new IllegalArgumentException("Probability must be between 0 and 100");
    }
    return random.nextInt(100) < probability;
  }
}
