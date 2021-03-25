package net.mbl.demo.hcfsdemo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class ReadAll {

  public static void readAllFiles(FileSystem fs, FileStatus parentStatus) throws IOException {

    FileStatus[] fileStatuses = fs.listStatus(parentStatus.getPath());
    for(FileStatus status : fileStatuses) {
      if (status.isDirectory()) {
        readAllFiles(fs, status);
      } else {
        try (FSDataInputStream is = fs.open(status.getPath())) {
          System.out.println("start read " + status.getPath());
          IOUtils.copy(is, new NullOutputStream());
          System.out.println("finish read " + status.getPath());
        }
      }
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Path path = new Path(args[0]);
    FileSystem fs = path.getFileSystem(conf);
    FileStatus status = fs.getFileStatus(path);
    readAllFiles(fs, status);
  }
}
