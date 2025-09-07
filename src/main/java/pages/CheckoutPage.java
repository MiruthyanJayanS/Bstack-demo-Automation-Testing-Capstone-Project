package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class CheckoutPage {
  private final WebDriver driver;
  private final WebDriverWait wait;
  private final By firstName = By.id("firstNameInput");
  private final By lastName  = By.id("lastNameInput");
  private final By address1  = By.id("addressLine1Input");
  private final By province  = By.id("provinceInput");
  private final By postcode  = By.id("postCodeInput");
  private final By Submitbtn = By.id("checkout-shipping-continue");

  public CheckoutPage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
  }

  public void fillAddress(String fn, String ln, String addr, String prov, String zip) {
    wait.until(ExpectedConditions.visibilityOfElementLocated(firstName)).sendKeys(fn);
    driver.findElement(lastName).sendKeys(ln);
    driver.findElement(address1).sendKeys(addr);
    driver.findElement(province).sendKeys(prov);
    driver.findElement(postcode).sendKeys(zip);
  }
  
  public void clickShippingSubmit() {
	  WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(12));
	  WebElement btn = w.until(ExpectedConditions.elementToBeClickable(Submitbtn));
	  try {
	    btn.click();
	  } catch (Exception e) {
	    ((JavascriptExecutor) driver).executeScript("arguments.click();", btn);
	  }
	}
}
