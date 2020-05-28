package test.org.parsejava;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author risvan.v
 * @since 2020-05-28
 */
public class FileMimeTest {
    @Test
    public void test1() throws IOException {
        Path path = new File("product.png").toPath();
        System.out.println(path);
        String mimeType = Files.probeContentType(path);
        System.out.println(mimeType);
        assertEquals(mimeType, "image/png");
    }
    @Test
    public void test2() throws IOException {
        Path path = new File("product.zip").toPath();
        System.out.println(path);
        String mimeType = Files.probeContentType(path);
        System.out.println(mimeType);
        assertNotEquals(mimeType, "image/png");
    }

}
