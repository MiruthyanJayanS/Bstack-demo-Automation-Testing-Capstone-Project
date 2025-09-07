package pages;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;
public class LoginPage {
	
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    private final By signinRoot = By.cssSelector("main, #root, body");
    private final By signinForm = By.cssSelector("form");
    private final By rsInputs = By.cssSelector("[role='combobox'] input[type='text'], input[id^='react-select-'][id$='-input']");
    private final By submitInForm = By.cssSelector("form button[type='submit'], form [data-testid='login-btn']");
    private final By invalidMsg = By.xpath("//*[contains(.,'Invalid Username') or contains(.,'Invalid Password')][self::div or self::span or self::p]");
    //@SuppressWarnings("unused")
    //private final By dashboardHeader = By.cssSelector("header, [data-testid='header']");
    private final By productCard = By.cssSelector("[data-testid='product-card'], .shelf-item");
    
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
  

    public void openSignin() {
        driver.get("https://bstackdemo.com/signin");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/signin"),
                ExpectedConditions.presenceOfElementLocated(signinRoot)
        ));
        wait.until(ExpectedConditions.presenceOfElementLocated(signinForm));
        softEnsureFirstInput();
    }
    
    public void loginDemo() {
  	  openSignin();
  	  chooseUsername("demouser");
  	  choosePassword("testingisfun99");
  	  submit();
  	  waitForDashboard();
  	}
    
    public boolean at() {
        return driver.getCurrentUrl().contains("/signin") && !driver.findElements(signinForm).isEmpty();
    }
    
    public boolean isOnSignin() {
        return driver.getCurrentUrl().contains("/signin");
    }
    
    public boolean isLoggedInUrl() {
        return driver.getCurrentUrl().contains("bstackdemo.com") && !driver.findElements(productCard).isEmpty();
    }
    
    public void waitForDashboard() {
        new WebDriverWait(driver, Duration.ofSeconds(12))
                .until(ExpectedConditions.presenceOfElementLocated(productCard));
    }
    
    public void waitToStayOnSignin() {
        new WebDriverWait(driver, Duration.ofSeconds(8))
                .until(ExpectedConditions.presenceOfElementLocated(signinForm));
    }
    
    private void softEnsureFirstInput() {
        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(rsInputs, 0));
        } catch (TimeoutException ignore) {
            new Actions(driver).pause(Duration.ofMillis(100)).sendKeys(Keys.TAB).perform();
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 1);");
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.numberOfElementsToBeMoreThan(rsInputs, 0));
        }
    }
    
    private WebElement usernameInput() {
        List<WebElement> inputs = driver.findElements(rsInputs);
        if (!inputs.isEmpty()) return inputs.get(0);
        List<WebElement> combos = driver.findElements(By.cssSelector("[role='combobox']"));
        if (!combos.isEmpty()) {
            combos.get(0).click();
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.numberOfElementsToBeMoreThan(rsInputs, 0));
            inputs = driver.findElements(rsInputs);
        }
        if (inputs.isEmpty()) throw new NoSuchElementException("Username input not found");
        return inputs.get(0);
    }
    
    private WebElement passwordInput() {
        List<WebElement> inputs = driver.findElements(rsInputs);
        if (inputs.size() >= 2) return inputs.get(1);
        List<WebElement> combos = driver.findElements(By.cssSelector("[role='combobox']"));
        if (combos.size() >= 2) {
            combos.get(1).click();
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(d -> d.findElements(rsInputs).size() >= 2);
            inputs = driver.findElements(rsInputs);
        }
        if (inputs.size() < 2) throw new NoSuchElementException("Password input not found");
        return inputs.get(1);
    }
    
    public void chooseUsername(String username) {
        WebElement inp = usernameInput();
        inp.sendKeys(username);
        inp.sendKeys(Keys.ENTER);
    }
    
    public void choosePassword(String password) {
        WebElement inp = passwordInput();
        inp.sendKeys(password);
        inp.sendKeys(Keys.ENTER);
    }
    
    public void submit() {
        WebElement form = driver.findElement(signinForm);
        WebElement btn = form.findElement(submitInForm);
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        } catch (JavascriptException ignore) { }
        try {
            btn.click();
            return;
        } catch (ElementClickInterceptedException e) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                return;
            } catch (JavascriptException ignore) { }
            new Actions(driver).moveToElement(btn).pause(Duration.ofMillis(100)).click().perform();
            return;
        } catch (Throwable t) {
            throw t;
        }
    }
    
    public void waitForInvalidMessage() {
        new WebDriverWait(driver, Duration.ofSeconds(8))
            .until(ExpectedConditions.visibilityOfElementLocated(invalidMsg));
    }
}