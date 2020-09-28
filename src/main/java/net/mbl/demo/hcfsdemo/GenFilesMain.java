package net.mbl.demo.hcfsdemo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.stream.LongStream;

public class GenFilesMain {

  private final Configuration conf;

  public GenFilesMain() {
    this.conf = new Configuration();
  }

  public Configuration getConf() {
    return conf;
  }

  public void cp(String src, String dest, long fileCount, long dirCount) throws IOException {
    Path inputPath = new Path(src);
    Path outputPath = new Path(dest);
    FileSystem fs = outputPath.getFileSystem(conf);
    LongStream.range(0, dirCount).parallel().forEach(i -> {
      Path dirPath = new Path(outputPath, "dir" + i);
      try {
        fs.mkdirs(dirPath);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    LongStream.range(0, dirCount * fileCount).parallel().forEach(j -> {
      try {
        FileUtil
            .copy(inputPath.getFileSystem(conf), inputPath,
                outputPath.getFileSystem(conf),
                new Path(outputPath, "dir" + (j / fileCount) + "/file" + j),
                false, conf);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  public static void main(String[] args) throws IOException {
    GenFilesMain main = new GenFilesMain();
    main.cp(args[0], args[1], Long.parseLong(args[2]), Long.parseLong(args[3]));
  }
}
