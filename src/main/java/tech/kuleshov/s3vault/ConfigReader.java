package tech.kuleshov.s3vault;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConfigReader {

  @SneakyThrows
  public Config readConfig(File file) {
    JSONParser parser = new JSONParser();

    try (Reader reader = new FileReader(file.getAbsolutePath())) {

      JSONObject object = (JSONObject) parser.parse(reader);
      String salt = (String) object.get("salt");
      String password = (String) object.get("password");
      String s3BucketId = (String) object.get("s3.bucket_id");
      String s3AccessId = (String) object.get("s3.access_id");
      String s3AccessKey = (String) object.get("s3.access_key");
      String s3Region = (String) object.get("s3.region");

      return Config.builder()
          .salt(salt)
          .password(password)
          .s3BucketId(s3BucketId)
          .s3AccessId(s3AccessId)
          .s3AccessKey(s3AccessKey)
          .s3Region(s3Region)
          .build();

    } catch (IOException | ParseException e) {
      throw e;
    }
  }
}
