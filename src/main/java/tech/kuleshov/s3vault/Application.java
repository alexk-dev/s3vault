package tech.kuleshov.s3vault;

import picocli.CommandLine;

public class Application {

  public static void main(String[] args) {
    ConfigReader configReader = new ConfigReader();
    int exitCode = new CommandLine(new CommandLineHandler(configReader)).execute(args);
    System.exit(exitCode);
  }
}
