package com.maemresen.server.manager.cli.utils;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import com.maemresen.server.manager.cli.exception.RandomServerException;
import java.time.Duration;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class RandomActionHelperTest {

  @Test
  void shouldWaitRandomSecondsWithinRange() throws InterruptedException {
    int minimumSeconds = 1;
    int maximumSeconds = 5;
    int randomSeconds = 3;

    try (MockedStatic<RandomValueUtils> randomUtilsMock = mockStatic(RandomValueUtils.class);
        MockedStatic<ExecutionPauseUtils> pauseUtilsMock = mockStatic(ExecutionPauseUtils.class)) {

      randomUtilsMock
          .when(() -> RandomValueUtils.randomIntBetween(minimumSeconds, maximumSeconds))
          .thenReturn(randomSeconds);

      RandomActionHelper.waitRandomSeconds(minimumSeconds, maximumSeconds);

      pauseUtilsMock.verify(
          () -> ExecutionPauseUtils.delay(Duration.ofSeconds(randomSeconds)), times(1));
    }
  }

  @Test
  void shouldThrowRandomExceptionWhenProbabilityIsMet() {
    int probability = 50;
    RandomServerException expectedException = new RandomServerException("Test exception");

    try (MockedStatic<RandomValueUtils> randomUtilsMock = mockStatic(RandomValueUtils.class)) {
      randomUtilsMock.when(() -> RandomValueUtils.randomBoolean(probability)).thenReturn(true);

      Supplier<RandomServerException> exceptionSupplier = () -> expectedException;

      assertThatThrownBy(
              () -> RandomActionHelper.throwRandomException(probability, exceptionSupplier))
          .isInstanceOf(RandomServerException.class)
          .isEqualTo(expectedException);
    }
  }

  @Test
  void shouldNotThrowRandomExceptionWhenProbabilityIsNotMet() {
    int probability = 50;

    try (MockedStatic<RandomValueUtils> randomUtilsMock = mockStatic(RandomValueUtils.class)) {
      randomUtilsMock.when(() -> RandomValueUtils.randomBoolean(probability)).thenReturn(false);

      Supplier<RandomServerException> exceptionSupplier =
          () -> new RandomServerException("Test exception");

      assertThatCode(() -> RandomActionHelper.throwRandomException(probability, exceptionSupplier))
          .doesNotThrowAnyException();
    }
  }
}
