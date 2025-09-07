package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class Screenshot {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH-mm-ss-SSS");

    private Screenshot() { }

    /**
     * Captures a screenshot and saves it under reports/screenshots/<testClass>/<yyyy-MM-dd>/<method>_<time>.png
     * Returns the destination File (or null if capture fails).
     */
    public static File save(WebDriver driver, String testClassName, String testMethodName) {
        try {
            String day = LocalDate.now().format(DATE);
            String filename = testMethodName + "_" + LocalTime.now().format(TIME) + ".png";

            Path dir = Path.of("test-output", "screenshots", sanitize(testClassName), day);
            Files.createDirectories(dir);

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = dir.resolve(sanitize(filename)).toFile();

            Files.copy(src.toPath(), dest.toPath());
            return dest;
        } catch (Exception ignored) {
            return null;
        }
    }

    // Replace characters that are problematic on some filesystems
    private static String sanitize(String s) {
        return s == null ? "unknown" : s.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
