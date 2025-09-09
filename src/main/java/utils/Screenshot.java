package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Capabilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class Screenshot {
  private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd-MM-yyyy");
  private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH-mm-ss-SSS");
  private Screenshot() {}

  // Keeps your existing viewport capture (page only)
  public static File saveViewport(WebDriver driver, String testClassName, String testMethodName) {
    try {
      String day = LocalDate.now().format(DATE);
      String file = testMethodName + "_" + LocalTime.now().format(TIME) + ".png";
      Path dir = Path.of("test-output","screenshots", browserId(driver), sanitize(testClassName), day);
      Files.createDirectories(dir);
      File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      File dest = dir.resolve(sanitize(file)).toFile();
      Files.copy(src.toPath(), dest.toPath());
      return dest;
    } catch (Exception e) { return null; }
  }

  // NEW: Full screen (entire monitor)
  public static File saveFullScreen(WebDriver driver, String testClassName, String testMethodName) {
    try {
      String day = LocalDate.now().format(DATE);
      String file = testMethodName + "_" + LocalTime.now().format(TIME) + "_FULL.png";
      Path dir = Path.of("test-output","screenshots", browserId(driver), sanitize(testClassName), day);
      Files.createDirectories(dir);

      Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
      BufferedImage img = new Robot().createScreenCapture(screen);
      File dest = dir.resolve(sanitize(file)).toFile();
      ImageIO.write(img, "png", dest);
      return dest;
    } catch (Exception e) { return null; }
  }

  // Optional: Browser window only (robot + window bounds)
  public static File saveBrowserWindow(WebDriver driver, String testClassName, String testMethodName) {
    try {
      String day = LocalDate.now().format(DATE);
      String file = testMethodName + "_" + LocalTime.now().format(TIME) + "_WINDOW.png";
      Path dir = Path.of("test-output","screenshots", browserId(driver), sanitize(testClassName), day);
      Files.createDirectories(dir);

      org.openqa.selenium.Point p = driver.manage().window().getPosition();
      org.openqa.selenium.Dimension s = driver.manage().window().getSize();

      Rectangle rect = new Rectangle(p.getX(), p.getY(), s.getWidth(), s.getHeight());
      BufferedImage img = new Robot().createScreenCapture(rect);
      File dest = dir.resolve(sanitize(file)).toFile();
      ImageIO.write(img, "png", dest);
      return dest;
    } catch (Exception e) { return null; }
  }

  private static String browserId(WebDriver driver) {
    try {
      Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
      String name = caps.getBrowserName();
      String version = caps.getBrowserVersion();
      return sanitize((name == null ? "unknown" : name) + "_" + (version == null ? "" : version));
    } catch (Exception e) {
      return "unknown";
    }
  }

  private static String sanitize(String s) {
    return s == null ? "unknown" : s.replaceAll("[^a-zA-Z0-9._-]", "_");
  }
}
