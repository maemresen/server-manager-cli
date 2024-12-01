package com.maemresen.server.manager.cli.service;

import com.maemresen.server.manager.cli.exception.RandomServerException;
import com.maemresen.server.manager.cli.utils.ExecutionPauseUtils;
import java.time.Duration;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;

/**
 * Service to perform random actions such as introducing a random delay or throwing a random
 * exception based on a probability.
 *
 * <p>This service utilizes {@link RandomGeneratorService} to generate random values for its
 * operations.
 */
@RequiredArgsConstructor
public class RandomActionService {

  private final RandomGeneratorService randomGeneratorService = new RandomGeneratorService();

  /**
   * Causes the current thread to pause execution for a random number of seconds between the
   * specified minimum and maximum range.
   *
   * @param minimumSeconds the minimum number of seconds to wait
   * @param maximumSeconds the maximum number of seconds to wait
   * @throws InterruptedException if the thread is interrupted while sleeping
   */
  public void waitRandomSeconds(int minimumSeconds, int maximumSeconds)
      throws InterruptedException {
    int secondsToWait = randomGeneratorService.randomIntBetween(minimumSeconds, maximumSeconds);
    ExecutionPauseUtils.delay(Duration.ofSeconds(secondsToWait));
  }

  /**
   * Throws a random exception based on the specified probability.
   *
   * <p>The probability is given as a percentage (0-100), where a higher value increases the
   * likelihood of throwing the exception. The exception to be thrown is provided by the {@code
   * exceptionSupplier}.
   *
   * @param probability the probability (0-100) of throwing the exception
   * @param exceptionSupplier a supplier that provides the exception instance
   * @throws RandomServerException if the random condition matches the probability
   */
  public void throwRandomException(
      int probability, Supplier<? extends RandomServerException> exceptionSupplier) {
    if (randomGeneratorService.randomBoolean(probability)) {
      throw exceptionSupplier.get();
    }
  }
}
