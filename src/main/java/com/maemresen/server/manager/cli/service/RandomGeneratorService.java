package com.maemresen.server.manager.cli.service;

import java.util.Random;

public class RandomGeneratorService {
  private final Random random = new Random();

  /**
   * Generates a random integer between the specified minimum and maximum values (inclusive).
   *
   * @param min the minimum value (inclusive)
   * @param max the maximum value (inclusive)
   * @return a random integer between {@code min} and {@code max} (inclusive)
   * @throws IllegalArgumentException if {@code min} is greater than {@code max}
   */
  public int randomIntBetween(int min, int max) {
    return random.nextInt((max - min) + 1) + min;
  }

  /**
   * Generates a random boolean value based on the given probability.
   *
   * @param probability the probability of returning {@code true}, expressed as a percentage (0-100)
   * @return {@code true} with the specified probability, otherwise {@code false}
   * @throws IllegalArgumentException if {@code probability} is not between 0 and 100
   */
  public boolean randomBoolean(int probability) {
    if (probability < 0 || probability > 100) {
      throw new IllegalArgumentException("Probability must be between 0 and 100");
    }
    return random.nextInt(100) < probability;
  }
}
