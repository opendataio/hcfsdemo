package net.mbl.demo.hcfsdemo;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

public class Main {

  private final Configuration conf;

  public Main() {
    this.conf = new Configuration();
  }

  public Configuration getConf() {
    return conf;
  }

  public void cp(String src, String dest) throws IOException {
    Path inputPath = new Path(src);
    Path outputPath = new Path(dest);
    FileUtil
        .copy(inputPath.getFileSystem(conf), inputPath,
            outputPath.getFileSystem(conf), outputPath,
            false, conf);
  }

  public static void main(String[] args) throws IOException {
    Main main = new Main();
    main.cp(args[0], args[1]);
  }
}
