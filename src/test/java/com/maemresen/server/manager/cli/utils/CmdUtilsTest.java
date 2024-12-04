package com.maemresen.server.manager.cli.utils;

import static org.assertj.core.api.Assertions.*;

import com.maemresen.server.manager.cli.exception.EnumValueParseException;
import java.util.Optional;
import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CmdUtilsTest {

  @Nested
  class GetBooleanParameterTests {

    @Test
    void shouldReturnTrueWhenParameterExists() {
      CommandLine cmd = Mockito.mock(CommandLine.class);
      Mockito.when(cmd.hasOption("test-boolean")).thenReturn(true);

      Optional<Boolean> result = CmdUtils.getBooleanParameter(cmd, "test-boolean");

      assertThat(result).contains(true);
    }

    @Test
    void shouldReturnEmptyWhenParameterDoesNotExist() {
      CommandLine cmd = Mockito.mock(CommandLine.class);
      Mockito.when(cmd.hasOption("test-boolean")).thenReturn(false);

      Optional<Boolean> result = CmdUtils.getBooleanParameter(cmd, "test-boolean");

      assertThat(result).isEmpty();
    }
  }

  @Nested
  class GetEnumParameterTests {

    private enum TestEnum {
      OPTION_ONE,
      OPTION_TWO
    }

    @Test
    void shouldReturnEnumValueWhenParameterExists() throws EnumValueParseException {
      CommandLine cmd = Mockito.mock(CommandLine.class);
      Mockito.when(cmd.hasOption("test-enum")).thenReturn(true);
      Mockito.when(cmd.getOptionValue("test-enum")).thenReturn("option_one");

      Optional<TestEnum> result = CmdUtils.getEnumParameter(cmd, "test-enum", TestEnum.class);

      assertThat(result).contains(TestEnum.OPTION_ONE);
    }

    @Test
    void shouldThrowExceptionForInvalidEnumValue() {
      CommandLine cmd = Mockito.mock(CommandLine.class);
      Mockito.when(cmd.hasOption("test-enum")).thenReturn(true);
      Mockito.when(cmd.getOptionValue("test-enum")).thenReturn("invalid_option");

      assertThatThrownBy(() -> CmdUtils.getEnumParameter(cmd, "test-enum", TestEnum.class))
          .isInstanceOf(EnumValueParseException.class)
          .hasMessageContaining("Failed to parse enum value for option: test-enum.");
    }

    @Test
    void shouldReturnEmptyWhenParameterDoesNotExist() throws EnumValueParseException {
      CommandLine cmd = Mockito.mock(CommandLine.class);
      Mockito.when(cmd.hasOption("test-enum")).thenReturn(false);

      Optional<TestEnum> result = CmdUtils.getEnumParameter(cmd, "test-enum", TestEnum.class);

      assertThat(result).isEmpty();
    }
  }

  @Nested
  class GetParameterTests {

    @Test
    void shouldMapParameterWhenItExists() {
      CommandLine cmd = Mockito.mock(CommandLine.class);
      Mockito.when(cmd.hasOption("test-param")).thenReturn(true);
      Mockito.when(cmd.getOptionValue("test-param")).thenReturn("123");

      Optional<Integer> result = CmdUtils.getParameter(cmd, "test-param", Integer::valueOf);

      assertThat(result).contains(123);
    }

    @Test
    void shouldThrowExceptionForInvalidMapping() {
      CommandLine cmd = Mockito.mock(CommandLine.class);
      Mockito.when(cmd.hasOption("test-param")).thenReturn(true);
      Mockito.when(cmd.getOptionValue("test-param")).thenReturn("invalid");

      assertThatThrownBy(() -> CmdUtils.getParameter(cmd, "test-param", Integer::valueOf))
          .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void shouldReturnEmptyWhenParameterDoesNotExist() {
      CommandLine cmd = Mockito.mock(CommandLine.class);
      Mockito.when(cmd.hasOption("test-param")).thenReturn(false);

      Optional<Integer> result = CmdUtils.getParameter(cmd, "test-param", Integer::valueOf);

      assertThat(result).isEmpty();
    }
  }
}
