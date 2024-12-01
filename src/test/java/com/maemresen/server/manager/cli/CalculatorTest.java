package com.maemresen.server.manager.cli;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CalculatorTest {

  @Test
  void shouldAddNumbers() {
    Calculator calc = new Calculator();

    int number1 = 1;
    int number2 = 2;

    assertEquals(number1 + number2, calc.add(number1, number2));
  }
}
