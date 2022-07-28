package tech.kuleshov.s3vault;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.SneakyThrows;

public class LocalStorage implements Storage {

  @SneakyThrows
  @Override
  public String get(String key) {
    return Files.readString(getPath(key));
  }

  @SneakyThrows
  @Override
  public void set(String key, String value) {
    try (PrintWriter out = new PrintWriter(getFilename(key))) {
      out.println(value);
    }
  }

  @Override
  public List<String> list(String part) {
    return null;
  }

  private Path getPath(String key) {
    return Path.of(getFilename(key));
  }

  private String getFilename(String key) {
    return "./local/" + key;
  }
}
