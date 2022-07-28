package tech.kuleshov.s3vault;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.SneakyThrows;

public class StorageS3Service implements Storage {

  private final AmazonS3 s3Client;
  private final Config config;

  public StorageS3Service(Config config) {
    this.s3Client = getClient(config);
    this.config = config;
  }

  @SneakyThrows
  @Override
  public String get(String key) {
    S3Object object = s3Client.getObject(config.getS3BucketId(), key);
    return new String(object.getObjectContent().readAllBytes(), StandardCharsets.UTF_8);
  }

  @Override
  public void set(String key, String value) {
    s3Client.putObject(config.getS3BucketId(), key, value);
  }

  @Override
  public List<String> list(String part) {
    return null;
  }

  private AmazonS3 getClient(Config config) {
    return AmazonS3ClientBuilder.standard()
        .withRegion(config.getS3Region())
        .withCredentials(
            new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(config.getS3AccessId(), config.getS3AccessKey())))
        .build();
  }
}
