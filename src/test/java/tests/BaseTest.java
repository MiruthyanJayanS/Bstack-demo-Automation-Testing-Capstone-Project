package tests;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.OrderConfirmationPage;
import reporting.ExtentTestNgListener;
import utils.Screenshot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Listeners({ExtentTestNgListener.class})
public abstract class BaseTest {

  protected WebDriver driver;
  private Path downloadDir;
  @BeforeClass(alwaysRun = true)
  @Parameters({"downloadDir"})
  public void baseSetup(@Optional("C:/Users/mirut/Capstone-Project-workspace/BstackDemoAutomation/src/main/resources") String downloadDirParam)
      throws IOException {
    
    downloadDir = Paths.get(downloadDirParam);
    Files.createDirectories(downloadDir);

    
    Map<String, Object> prefs = new HashMap<>();
    prefs.put("download.default_directory", downloadDir.toString());   
    prefs.put("download.prompt_for_download", false);
    prefs.put("plugins.always_open_pdf_externally", true);           

    ChromeOptions options = new ChromeOptions();
    options.setExperimentalOption("prefs", prefs);

    driver = new ChromeDriver(options);
    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));  
    driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(45)); 
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
    driver.manage().window().maximize();
  }

  @AfterClass(alwaysRun = true)
  public void baseTeardown() {
    if (driver != null) driver.quit();
  }

  @BeforeMethod(alwaysRun = true)
  public void beforeEach(Method m) {
    if (ExtentTestNgListener.currentTest() != null) {
      ExtentTestNgListener.currentTest().info("Starting: " + m.getName());
    }
  }

  @AfterMethod(alwaysRun = true)
  public void baseAfterEach(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
      try {
        File shot = Screenshot.save(driver, getClass().getSimpleName(), result.getMethod().getMethodName());
        if (shot != null && ExtentTestNgListener.currentTest() != null) {
          ExtentTestNgListener.currentTest().fail(
              result.getThrowable(),
              MediaEntityBuilder.createScreenCaptureFromPath(shot.getAbsolutePath()).build()
          );
        }
      } catch (Exception ignore) { }
    }
  }
  
  protected Path getDownloadDir() {
    return downloadDir;
  }

  
  protected void stepLoginDemo() {
    new LoginPage(driver).loginDemo();
  }

  protected void stepAddOneItemOpenCartAssertQty() {
    CartPage cart = new CartPage(driver);
    cart.clickAddToCart();
    cart.openCart();
    Assert.assertTrue(cart.isQtyAdded(), "Cart quantity expected > 0");
  }

  protected void stepProceedToCheckout() {
    new CartPage(driver).proceedToCheckout();
  }

  protected void stepFillAddressAndSubmit() {
    CheckoutPage cp = new CheckoutPage(driver);
    cp.fillAddress("Jackie", "Chan", "1B Gandhi Street", "Mumbai", "400051");
    cp.clickShippingSubmit();
  }

  protected void stepAssertOrderAndReceipt() {
	  OrderConfirmationPage oc = new OrderConfirmationPage(driver);
	  Assert.assertTrue(oc.isConfirmed(), "Order not confirmed"); 
	  boolean receiptVisible = oc.isReceiptVisible(java.time.Duration.ofSeconds(25));
	  Assert.assertTrue(receiptVisible, "Receipt link missing"); 
	}

  
  protected void passShot(String message, String fileNameStem) {
	  try {
	    File shot = Screenshot.save(driver, getClass().getSimpleName(), fileNameStem);
	    if (shot != null && ExtentTestNgListener.currentTest() != null) {
	      ExtentTestNgListener.currentTest().pass(
	          message,
	          MediaEntityBuilder.createScreenCaptureFromPath(shot.getAbsolutePath()).build()
	      );
	    }
	  } catch (Exception ignore) { }
	}
}
