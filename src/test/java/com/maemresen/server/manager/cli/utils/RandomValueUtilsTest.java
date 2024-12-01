package com.maemresen.server.manager.cli.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RandomValueUtilsTest {

  @Nested
  class RandomIntBetweenTest {

    @Test
    void shouldGenerateRandomIntWithinRange() {
      int min = 1;
      int max = 5;

      for (int i = 0; i < 100; i++) {
        int randomValue = RandomValueUtils.randomIntBetween(min, max);
        assertThat(randomValue)
            .as("Generated random value should be between %d and %d", min, max)
            .isBetween(min, max);
      }
    }

    @Test
    void shouldGenerateAllPossibleValuesWithinRange() {
      int min = 1;
      int max = 5;
      Set<Integer> generatedValues = new HashSet<>();

      for (int i = 0; i < 1000; i++) {
        int randomValue = RandomValueUtils.randomIntBetween(min, max);
        generatedValues.add(randomValue);
      }

      assertThat(generatedValues)
          .as("Generated values should include all integers between %d and %d", min, max)
          .containsExactlyInAnyOrder(1, 2, 3, 4, 5);
    }

    @Test
    void shouldReturnMinWhenMinEqualsMax() {
      int min = 3;
      int max = 3;

      int randomValue = RandomValueUtils.randomIntBetween(min, max);

      assertThat(randomValue)
          .as("Generated random value should be equal to min (%d) when min equals max", min)
          .isEqualTo(min);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForInvalidRange() {
      int min = 10;
      int max = 5;

      assertThatThrownBy(() -> RandomValueUtils.randomIntBetween(min, max))
          .as("Expected IllegalArgumentException when min (%d) is greater than max (%d)", min, max)
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("bound must be positive");
    }
  }

  @Nested
  class RandomBooleanTest {
    @Test
    void shouldGenerateRandomBooleanWithZeroProbability() {
      for (int i = 0; i < 100; i++) {
        boolean randomValue = RandomValueUtils.randomBoolean(0);
        assertThat(randomValue)
            .as("Random boolean should always be false with 0% probability")
            .isFalse();
      }
    }

    @Test
    void shouldGenerateRandomBooleanWithHundredProbability() {
      for (int i = 0; i < 100; i++) {
        boolean randomValue = RandomValueUtils.randomBoolean(100);
        assertThat(randomValue)
            .as("Random boolean should always be true with 100% probability")
            .isTrue();
      }
    }

    @Test
    void shouldGenerateRandomBooleanWithFiftyPercentProbability() {
      int trueCount = 0;
      int falseCount = 0;

      for (int i = 0; i < 1000; i++) {
        if (RandomValueUtils.randomBoolean(50)) {
          trueCount++;
        } else {
          falseCount++;
        }
      }

      assertThat(trueCount)
          .as("True count should be approximately 50% of total trials")
          .isBetween(400, 600);

      assertThat(falseCount)
          .as("False count should be approximately 50% of total trials")
          .isBetween(400, 600);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForInvalidProbability() {
      assertThatThrownBy(() -> RandomValueUtils.randomBoolean(-1))
          .as("Expected IllegalArgumentException for negative probability")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Probability must be between 0 and 100");

      assertThatThrownBy(() -> RandomValueUtils.randomBoolean(101))
          .as("Expected IllegalArgumentException for probability greater than 100")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Probability must be between 0 and 100");
    }
  }
}
