package com.maemresen.server.manager.cli.utils.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PropertyUtils {

  public static Properties loadProperties(String resourceName) throws IOException {
    Properties props = new Properties();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
      props.load(resourceStream);
      return props;
    }
  }
}
