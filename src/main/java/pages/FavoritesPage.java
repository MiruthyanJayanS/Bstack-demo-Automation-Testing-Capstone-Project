package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FavoritesPage {

  private final WebDriver driver;
  private final WebDriverWait wait;
  
  private final By addToFavoritesBtn = By.cssSelector("button.MuiIconButton-root, .MuiIconButton-root");
  private final By heartSvg = By.cssSelector("span.MuiIconButton-label > svg.MuiSvgIcon-root.Icon");
  private final By favouritesTab = By.id("favourites");
  private final By favouritesHeader = By.xpath("//*[self::h1 or self::h2 or self::strong][normalize-space()='Favourites']");
  
  public FavoritesPage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
  }

  
  public void clickAddToFavorites() {
	  WebDriverWait drive = new WebDriverWait(driver, Duration.ofSeconds(12));
	  
	  drive.until(ExpectedConditions.visibilityOfElementLocated(heartSvg)); 

	  WebElement btn = drive.until(ExpectedConditions.elementToBeClickable(addToFavoritesBtn)); 
	  try {
	    btn.click(); 
	  } catch (Exception e1) {
	    try {
	      
	      new Actions(driver).moveToElement(btn).click().perform(); 
	    } catch (Exception e2) {
	     
	      ((JavascriptExecutor) driver).executeScript("arguments.click();", btn);
	    }
	  }
  }

  
  public void openFavouritesTab() {
    String before = driver.getCurrentUrl();
    WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(favouritesTab)); 
    tab.click();
    new WebDriverWait(driver, Duration.ofSeconds(12)).until(ExpectedConditions.or(
        ExpectedConditions.not(ExpectedConditions.urlToBe(before)),
        ExpectedConditions.urlContains("/favourites"),
        ExpectedConditions.visibilityOfElementLocated(favouritesHeader)
    )); 
  }

  public boolean isFavouritesPageVisible() {
    try {
      return new WebDriverWait(driver, Duration.ofSeconds(10))
          .until(ExpectedConditions.or(
              ExpectedConditions.urlContains("/favourites"),
              ExpectedConditions.visibilityOfElementLocated(favouritesHeader)
          ));
    } catch (TimeoutException e) {
      return false;
    }
  }
}
