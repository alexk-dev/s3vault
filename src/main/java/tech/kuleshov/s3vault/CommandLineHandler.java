package tech.kuleshov.s3vault;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "s3vault.sh", mixinStandardHelpOptions = true, synopsisSubcommandLabel = "[KEY]")
class CommandLineHandler implements Callable<Integer> {

  private final ConfigReader configReader;
  private Storage storage;
  private Config config;
  private EncryptionService encryptionService;

  public CommandLineHandler(ConfigReader configReader) {
    this.configReader = configReader;
  }

  @Parameters(index = "0", description = "get, set, list")
  private Action action;

  @Parameters(index = "1", description = "secret key")
  private String key;

  @Option(
      names = {"-f", "--file"},
      description = "source|target filename",
      required = false)
  private File file;

  @Option(
      names = {"-c", "--config"},
      description = "config; default is config.json",
      required = false)
  private File configFile = new File("config.json");

  @Option(
      names = {"-d", "--data"},
      description = "data",
      required = false)
  private String data;

  @Override
  public Integer call() throws Exception {
    this.config = configReader.readConfig(configFile);
    this.encryptionService = new EncryptionService(config);
    this.storage = new StorageS3Service(config);

    if (file != null && Action.set.equals(action)) {
      byte[] fileContents = Files.readAllBytes(file.toPath());
      data = new String(fileContents, StandardCharsets.UTF_8);
    }

    String result = null;
    switch (action) {
      case set:
        data = encryptionService.encrypt(data);
        storage.set(key, data);
        break;
      case get:
        result = storage.get(key);
        result = encryptionService.decrypt(result);
        break;
      default:
    }

    if (result != null) {
      if (Action.get.equals(action) && file != null) {
        try (PrintWriter out = new PrintWriter(file.getAbsolutePath())) {
          out.print(result);
        }
      } else {
        System.out.println(result);
      }
    }
    return 0;
  }
}
