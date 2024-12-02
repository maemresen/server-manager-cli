package com.maemresen.server.manager.cli.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EnvUtilsTest {

  @Nested
  class GetEnvTests {

    @Test
    void shouldReturnDefaultValueWhenEnvironmentVariableDoesNotExist() {
      String key = "NON_EXISTENT_ENV";
      String defaultValue = "default";

      String result = EnvUtils.getEnv(key, defaultValue);

      assertThat(result).isEqualTo(defaultValue);
    }

    @Test
    void shouldReturnNullWhenDefaultValueIsNullAndEnvironmentVariableDoesNotExist() {
      String key = "NON_EXISTENT_ENV";

      String result = EnvUtils.getEnv(key, null);

      assertThat(result).isNull();
    }
  }
}
