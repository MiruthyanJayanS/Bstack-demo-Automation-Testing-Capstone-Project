package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;


import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  private final By productCard = By.cssSelector("[data-testid='product-card'], .shelf-item");
  private final By productNameInCard = By.cssSelector("[data-testid='product-name'], .shelf-item__title");
  private final By addToCartButtonInCard = By.xpath(".//button[normalize-space()='Add to cart' or normalize-space()='Add to Cart' or @title='Add to cart']");
  private final By addToCartButtonFallback = By.cssSelector("button.add-to-cart, .shelf-item__buy-btn");
  private final By cartBadge = By.cssSelector("[data-testid='cart-count'], .bag__quantity");
  private final By closeCartBtn = By.cssSelector("div.float-cart__close-btn");
  private final By sortSelect = By.cssSelector("select");
  private final By vendorsLabelsCss = By.cssSelector(".filters-available-size label");
  private final By vendorsLabelsXpath = By.xpath("//h4[contains(@class,'title')][normalize-space()='Vendors:']" +
      "/following-sibling::*[contains(@class,'filters-available') or contains(@class,'filters-available-size')]//label");

  public HomePage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
  }

  public void open() {
    driver.get("https://bstackdemo.com/");
    wait.until(ExpectedConditions.presenceOfElementLocated(productCard));
  }

  public boolean at() {
    return driver.findElements(productCard).size() > 0;
  }

  public int visibleProductCount() {
    return driver.findElements(productCard).size();
  }

  public List<String> productNames() {
    return driver.findElements(productCard).stream()
        .map(card -> card.findElement(productNameInCard).getText())
        .collect(Collectors.toList());
  }

  public void addFirstProductToCart() {
    List<WebElement> cards = driver.findElements(productCard);
    if (cards.isEmpty()) throw new NoSuchElementException("No products visible to add");
    WebElement card = cards.get(0);
    List<WebElement> buttons = card.findElements(addToCartButtonInCard);
    WebElement btn = buttons.isEmpty() ? card.findElement(addToCartButtonFallback) : buttons.get(0);
    wait.until(ExpectedConditions.elementToBeClickable(btn));
    try {
      btn.click();
    } catch (Throwable e) {
      new Actions(driver).moveToElement(btn).pause(Duration.ofMillis(150)).click().perform();
    }
  }

  public boolean cartBadgeVisible() {
    return !driver.findElements(cartBadge).isEmpty();
  }

  public int cartBadgeCountOrZero() {
    if (!cartBadgeVisible()) return 0;
    String txt = driver.findElement(cartBadge).getText().trim();
    try { return Integer.parseInt(txt); } catch (Exception ignore) { return 0; }
  }

  public void closeCartIfOpen() {
    List<WebElement> closeBtns = driver.findElements(closeCartBtn);
    if (!closeBtns.isEmpty()) {
      WebElement btn = closeBtns.get(0);
      try {
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.elementToBeClickable(btn));
        btn.click();
      } catch (Exception e) {
        new Actions(driver).moveToElement(btn).pause(Duration.ofMillis(80)).click().perform();
      }
      new WebDriverWait(driver, Duration.ofSeconds(5))
          .until(ExpectedConditions.invisibilityOfElementLocated(closeCartBtn));
    }
  }
  
  public void sortByLowestToHighest() {
	  WebElement dd = wait.until(ExpectedConditions.elementToBeClickable(sortSelect));
	  new Select(dd).selectByValue("lowestprice");
	}

	public void sortByHighestToLowest() {
	  WebElement dd = wait.until(ExpectedConditions.elementToBeClickable(sortSelect));
	  new Select(dd).selectByValue("highestprice");
	}

	public String currentSortText() {
	  WebElement dd = wait.until(ExpectedConditions.presenceOfElementLocated(sortSelect));
	  return new Select(dd).getFirstSelectedOption().getText().trim();
	}

  public List<WebElement> vendorLabels() {
    List<WebElement> labels = driver.findElements(vendorsLabelsCss);
    if (labels == null || labels.isEmpty()) {
      labels = driver.findElements(vendorsLabelsXpath);
    }
    return labels;
  }

  public void selectAllVendors() {
    closeCartIfOpen();
    WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
    List<WebElement> labels = driver.findElements(vendorsLabelsCss);
    if (labels == null || labels.isEmpty()) {
      labels = driver.findElements(vendorsLabelsXpath);
    }
    for (WebElement label : labels) {
      WebElement input = label.findElement(By.cssSelector("input[type='checkbox']"));
      WebElement span = label.findElement(By.cssSelector("span.checkmark"));
      if (!input.isSelected()) {
        try {
          span.click();
        } catch (Exception e) {
          new Actions(driver).moveToElement(span).pause(Duration.ofMillis(80)).click().perform();
        }
        w.until(ExpectedConditions.elementToBeSelected(input));
        try { Thread.sleep(5000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
      }
    }
  }

  public void selectVendor(WebElement labelEl) {
    WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement input = labelEl.findElement(By.cssSelector("input[type='checkbox']"));
    WebElement span = labelEl.findElement(By.cssSelector("span.checkmark"));
    if (!input.isSelected()) {
      try { span.click(); }
      catch (Exception e) { new Actions(driver).moveToElement(span).pause(Duration.ofMillis(80)).click().perform(); }
      w.until(ExpectedConditions.elementToBeSelected(input));
    }
  }

  public void clearAllVendors() {
    closeCartIfOpen();
    WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
    List<WebElement> labels = driver.findElements(vendorsLabelsCss);
    if (labels == null || labels.isEmpty()) {
      labels = driver.findElements(vendorsLabelsXpath);
    }
    for (WebElement label : labels) {
      WebElement input = label.findElement(By.cssSelector("input[type='checkbox']"));
      WebElement span = label.findElement(By.cssSelector("span.checkmark"));
      if (input.isSelected()) {
        try {
          span.click();
        } catch (Exception e) {
          new Actions(driver).moveToElement(span).pause(Duration.ofMillis(80)).click().perform();
        }
        w.until(ExpectedConditions.not(ExpectedConditions.elementToBeSelected(input)));
        try { Thread.sleep(5000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
      }
    }
  }

  public void clearVendor(WebElement labelEl) {
    WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement input = labelEl.findElement(By.cssSelector("input[type='checkbox']"));
    WebElement span = labelEl.findElement(By.cssSelector("span.checkmark"));
    if (input.isSelected()) {
      try { span.click(); }
      catch (Exception e) {
        new Actions(driver).moveToElement(span).pause(Duration.ofMillis(80)).click().perform();
      }
      w.until(ExpectedConditions.not(ExpectedConditions.elementToBeSelected(input)));
    }
  }
}
