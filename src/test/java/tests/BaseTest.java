package tests;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.edge.EdgeDriver;
//import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
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
import java.nio.file.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Listeners({ExtentTestNgListener.class})
public abstract class BaseTest {
  protected WebDriver driver;
  private Path downloadDir;

  @BeforeClass(alwaysRun = true)
  @Parameters({"browser","headless","downloadDir"})
  public void baseSetup(
      @Optional("chrome") String browser,
      @Optional("false") String headless,
      @Optional("C:/Users/mirut/Capstone-Project-workspace/BstackDemoAutomation/src/main/resources") String downloadDirParam
  ) throws IOException {

    downloadDir = Paths.get(downloadDirParam);
    Files.createDirectories(downloadDir);

    boolean isHeadless = Boolean.parseBoolean(headless);

    switch (browser.toLowerCase()) {
      case "firefox": {
        FirefoxOptions fo = new FirefoxOptions();
        if (isHeadless) fo.addArguments("--headless=new");
        fo.addPreference("browser.download.dir", downloadDir.toAbsolutePath().toString());
        fo.addPreference("browser.download.folderList", 2);
        fo.addPreference("browser.download.useDownloadDir", true);
        fo.addPreference("pdfjs.disabled", true);
        fo.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf,application/octet-stream");
        driver = new FirefoxDriver(fo);
        break;
      }
//      case "edge": {
//        EdgeOptions eo = new EdgeOptions();
//        eo.setBinary("‪C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe");
//        if (isHeadless) eo.addArguments("--headless=new");
//        Map<String,Object> prefs = new HashMap<>();
//        prefs.put("download.default_directory", downloadDir.toAbsolutePath().toString());
//        prefs.put("download.prompt_for_download", false);
//        prefs.put("plugins.always_open_pdf_externally", true);
//        eo.setExperimentalOption("prefs", prefs);
//        driver = new EdgeDriver(eo);
//        break;
//      }
      default: {
        ChromeOptions co = new ChromeOptions();
        if (isHeadless) co.addArguments("--headless=new");
        Map<String,Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadDir.toAbsolutePath().toString());
        prefs.put("download.prompt_for_download", false);
        prefs.put("plugins.always_open_pdf_externally", true);
        co.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(co);
      }
    }

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

  protected Path getDownloadDir() { return downloadDir; }

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
    boolean receiptVisible = oc.isReceiptVisible(Duration.ofSeconds(25));
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
