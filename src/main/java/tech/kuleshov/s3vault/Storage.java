package tech.kuleshov.s3vault;

import java.util.List;

public interface Storage {

  String get(String key);

  void set(String key, String value);

  List<String> list(String part);
}
