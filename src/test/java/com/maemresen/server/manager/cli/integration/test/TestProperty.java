package com.maemresen.server.manager.cli.integration.test;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, TYPE})
@Repeatable(TestProperties.class)
public @interface TestProperty {
  String key() default "";

  String value() default "";
}
