package tech.kuleshov.s3vault;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Config {

  private String salt;
  private String password;
  private String s3BucketId;
  private String s3AccessId;
  private String s3AccessKey;
  private String s3Region;
}
