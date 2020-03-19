package net.mbl.demo.hcfsdemo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessController;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.security.action.GetPropertyAction;

public class TestHcfs {

  private static final File tmpdir = new File(AccessController
      .doPrivileged(new GetPropertyAction("java.io.tmpdir")));
  private static final Path destFilePath = Paths.get(tmpdir.getAbsolutePath(),
      "hcfstest" + System.currentTimeMillis() + ".txt");
  private static final String content = "A cat will append to the end of the file";

  private Main main;
  private File file;

  @Before
  public void setUpClass() {
    try {
      this.main = new Main();
      this.file = File.createTempFile("hcfstest", ".txt");
      if (!file.exists()) {
        file.createNewFile();
      }

      FileWriter fileWritter = new FileWriter(file.getAbsolutePath(), true);
      fileWritter.write(content);
      fileWritter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @After
  public void tearDown() throws IOException {
    FileUtils.forceDelete(file);
    FileUtils.forceDelete(destFilePath.toFile());
  }

  @Test
  public void testCp() throws IOException {
    Main main = new Main();

    main.cp(file.getAbsolutePath(), destFilePath.toString());
    Assert.assertTrue(Files.exists(destFilePath));
    String destFileContent =
        FileUtils.readFileToString(destFilePath.toFile(), Charset.defaultCharset());
    Assert.assertEquals(content, destFileContent);
  }
}
