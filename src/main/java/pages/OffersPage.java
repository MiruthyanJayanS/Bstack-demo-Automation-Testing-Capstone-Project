package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class OffersPage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  private final By offersNav = By.cssSelector("a#offers.Navbar_link__3Blki.menu-item[href='/offers']");
  private final By emptyMsg = By.cssSelector("div.pt-6.text-2xl.font-bold.tracking-wide.text-center.text-red-50");

  public OffersPage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
  }

  public void openViaNavbar() {
    wait.until(ExpectedConditions.elementToBeClickable(offersNav)).click();
    wait.until(ExpectedConditions.urlContains("/offers"));
  }

  public boolean waitForEmptyOffersMessageVisible() {
    WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(emptyMsg));
    
    return new WebDriverWait(driver, Duration.ofSeconds(8))
        .until(d -> !el.getText().trim().isEmpty());
  }

  public boolean emptyOffersMessageVisible() {
    return !driver.findElements(emptyMsg).isEmpty();
  }

  public String emptyOffersText() {
    return driver.findElement(emptyMsg).getText().trim();
  }
}
