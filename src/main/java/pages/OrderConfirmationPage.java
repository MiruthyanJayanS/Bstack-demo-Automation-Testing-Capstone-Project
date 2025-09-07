package pages;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderConfirmationPage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  private final By confirmation = By.id("confirmation-message");
  private final By receiptLink  = By.id("downloadpdf");
  private final By contShopping = By.id("checkout-shipping-continue");

  public OrderConfirmationPage(WebDriver d) {
    this.driver = d;
    this.wait = new WebDriverWait(d, Duration.ofSeconds(15));
  }

  // Query methods (no TestNG)
  public boolean isConfirmed() {
    try {
      WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmation));
      return el.isDisplayed();
    } catch (TimeoutException e) {
      return false;
    }
  }

  public boolean isReceiptVisible(Duration timeout) {
    try {
      WebElement receipt = new WebDriverWait(driver, timeout)
          .until(ExpectedConditions.visibilityOfElementLocated(receiptLink));
      return receipt.isDisplayed();
    } catch (TimeoutException e) {
      return false;
    }
  }

  public void continueShipping() {
    driver.findElement(contShopping).click();
  }

  public Path clickReceiptAndWaitForPdf(Path downloadDir, Duration timeout) {
    WebElement link = new WebDriverWait(driver, Duration.ofSeconds(25))
        .until(ExpectedConditions.elementToBeClickable(receiptLink));
    try {
      link.click();
    } catch (Exception e) {
      ((JavascriptExecutor) driver).executeScript("arguments.click();", link);
    }

    long end = System.currentTimeMillis() + timeout.toMillis();
    Path found = null;

    while (System.currentTimeMillis() < end) {
      try (DirectoryStream<Path> stream = Files.newDirectoryStream(downloadDir, "*.pdf")) {
        for (Path p : stream) {
          String name = p.getFileName().toString();
          if (!name.toLowerCase().endsWith(".crdownload") && Files.size(p) > 0) {
            found = p;
            break;
          }
        }
      } catch (IOException ignored) { }
      if (found != null) break;
      try { Thread.sleep(300); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
    }

    if (found == null) throw new TimeoutException("Receipt PDF not downloaded in time to " + downloadDir);
    return found;
  }
}
