package com.maemresen.server.manager.cli.integration.test;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, TYPE})
public @interface TestProperties {
  TestProperty[] value();
}
