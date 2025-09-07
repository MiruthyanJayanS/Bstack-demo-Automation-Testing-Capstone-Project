package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class CartPage {
  private final WebDriver driver;
  @SuppressWarnings("unused")
  private final WebDriverWait wait;

  private final By cartIcon = By.xpath("//*[self::button or self::a or self::span or self::div][contains(@class,'bag') or @data-testid='cart']");
  private final By addToCartBtn = By.xpath("//div[contains(@class,'shelf-item__buy-btn') and normalize-space(.)='Add to cart']");
  private final By cartQty = By.xpath("//p[contains(@class,'desc') and contains(normalize-space(.),'Quantity:')]");
  private final By drawerRoot = By.xpath("//*[contains(@class,'float-cart__content') or @data-testid='cart-drawer']");
  private final By checkoutBtn = By.xpath("//div[contains(@class,'buy-btn') and normalize-space(.)='Checkout']");
  private final By shippingLegend = By.xpath("//legend[contains(@class,'form-legend') and contains(@class,'optimizedCheckout-headingSecondary') and normalize-space(.)='Shipping Address']");

  public CartPage(WebDriver d) {
    this.driver = d;
    this.wait = new WebDriverWait(d, Duration.ofSeconds(12));
  }

  public void clickAddToCart() {
    new WebDriverWait(driver, Duration.ofSeconds(12))
        .until(ExpectedConditions.elementToBeClickable(addToCartBtn))
        .click();
  }

  public void openCart() {
    WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(12));
    WebElement icon = w.until(ExpectedConditions.visibilityOfElementLocated(cartIcon)); 
    try {
      w.until(ExpectedConditions.elementToBeClickable(icon)).click(); 
    } catch (Exception e) {
      ((JavascriptExecutor) driver).executeScript("arguments.click();", icon); 
    }
    w.until(ExpectedConditions.visibilityOfElementLocated(drawerRoot)); 
  }

  public boolean isQtyAdded() {
    WebElement qtyEl = new WebDriverWait(driver, Duration.ofSeconds(10))
        .until(ExpectedConditions.visibilityOfElementLocated(cartQty)); 
    String text = qtyEl.getText();
    String digits = text.replaceAll("\\D+", "");
    int qty = digits.isEmpty() ? 0 : Integer.parseInt(digits);
    return qty > 0;
  }

  public void proceedToCheckout() {
    WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(12));
    WebElement drawer = w.until(ExpectedConditions.visibilityOfElementLocated(drawerRoot)); 

    
    WebElement btn = w.until(ExpectedConditions.refreshed(
        ExpectedConditions.elementToBeClickable(checkoutBtn))); 

    String beforeUrl = driver.getCurrentUrl();

    try {
      btn.click();
    } catch (Exception e) {
      ((JavascriptExecutor) driver).executeScript("arguments.click();", btn); 
    }

    try {
      new WebDriverWait(driver, Duration.ofSeconds(5))
          .until(ExpectedConditions.stalenessOf(drawer));
    } catch (Exception ignored) { }

    new WebDriverWait(driver, Duration.ofSeconds(12))
        .until(ExpectedConditions.or(
            ExpectedConditions.not(ExpectedConditions.urlToBe(beforeUrl)),   
            ExpectedConditions.urlContains("checkout"),                     
            ExpectedConditions.visibilityOfElementLocated(shippingLegend)    
        ));
  }
}
