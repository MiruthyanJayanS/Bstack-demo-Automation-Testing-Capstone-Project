package tests;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.LoginPage;
import reporting.ExtentTestNgListener;
import utils.Screenshot;

import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

@Listeners({ExtentTestNgListener.class})
public class LoginTest {

    private WebDriver driver;
    private LoginPage login;
    private Properties data;
    @SuppressWarnings("unused")
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() throws Exception {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        data = new Properties();
        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("testdata/login.properties")) {
            if (in != null) data.load(in);
        }
        login = new LoginPage(driver);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @AfterMethod(alwaysRun = true)
    public void afterEach(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String cls = getClass().getSimpleName();
            String name = result.getMethod().getMethodName() + "_error";
            try {
                File shot = Screenshot.save(driver, cls, name);
                if (shot != null && ExtentTestNgListener.currentTest() != null) {
                    ExtentTestNgListener.currentTest().fail(
                        result.getThrowable(),
                        MediaEntityBuilder.createScreenCaptureFromPath(shot.getAbsolutePath()).build()
                    );
                }
            } catch (Exception ignore) { }
        }
    }

    @Test
    public void openLoginPage() {
        ExtentTestNgListener.currentTest().info("Opening sign-in page");
        login.openSignin();
        Assert.assertTrue(login.at(), "Sign in not visible");
        File opened = Screenshot.save(driver, getClass().getSimpleName(), "openLoginPage");
        if (opened != null && ExtentTestNgListener.currentTest() != null) {
            ExtentTestNgListener.currentTest().pass(
                "Sign-in visible",
                MediaEntityBuilder.createScreenCaptureFromPath(opened.getAbsolutePath()).build()
            );
        }
    }

    @Test(dependsOnMethods = "openLoginPage")
    public void validLogin() {
        ExtentTestNgListener.currentTest().info("Open sign-in");
        login.openSignin();
        ExtentTestNgListener.currentTest().info("Enter username");
        login.chooseUsername(data.getProperty("username", "demouser"));
        ExtentTestNgListener.currentTest().info("Enter password");
        login.choosePassword(data.getProperty("password", "testingisfun99"));
        ExtentTestNgListener.currentTest().info("Click submit");
        login.submit();

        login.waitForDashboard();
        Assert.assertTrue(login.isLoggedInUrl(), "Not navigated after login");

        File dashboard = Screenshot.save(driver, getClass().getSimpleName(), "validLogin_dashboard");
        if (dashboard != null && ExtentTestNgListener.currentTest() != null) {
            ExtentTestNgListener.currentTest().pass(
                "Dashboard",
                MediaEntityBuilder.createScreenCaptureFromPath(dashboard.getAbsolutePath()).build()
            );
        }
    }

    @Test(dependsOnMethods = "openLoginPage")
    public void invalidPassword() {
        ExtentTestNgListener.currentTest().info("Attempt invalid password");
        login.openSignin();
        login.chooseUsername(data.getProperty("username", "demouser"));
        login.choosePassword("wrongpass");
        login.submit();

        login.waitForInvalidMessage();
        Assert.assertTrue(login.isOnSignin(), "Should stay on Sign In");

        File invalidPass = Screenshot.save(driver, getClass().getSimpleName(), "invalidPassPassword_invalidPassBanner");
        if (invalidPass != null && ExtentTestNgListener.currentTest() != null) {
            ExtentTestNgListener.currentTest().info(
                "Invalid banner shown",
                MediaEntityBuilder.createScreenCaptureFromPath(invalidPass.getAbsolutePath()).build()
            );
        }
    }

    @Test(dependsOnMethods = "openLoginPage")
    public void emptyUsername() {
        ExtentTestNgListener.currentTest().info("Attempt empty username");
        login.openSignin();
        login.choosePassword(data.getProperty("password", "testingisfun99"));
        login.submit();

        login.waitForInvalidMessage();
        Assert.assertTrue(login.isOnSignin(), "Should stay on Sign In");

        File invalidUserName = Screenshot.save(driver, getClass().getSimpleName(), "emptyUsername_invalidBanner");
        if (invalidUserName != null && ExtentTestNgListener.currentTest() != null) {
            ExtentTestNgListener.currentTest().info(
                "Invalid banner shown",
                MediaEntityBuilder.createScreenCaptureFromPath(invalidUserName.getAbsolutePath()).build()
            );
        }
    }

    @Test(dependsOnMethods = "openLoginPage")
    public void emptyPassword() {
        ExtentTestNgListener.currentTest().info("Attempt empty password");
        login.openSignin();
        login.chooseUsername(data.getProperty("username", "demouser"));
        login.submit();

        login.waitForInvalidMessage();
        Assert.assertTrue(login.isOnSignin(), "Should stay on Sign In");

        File emptyPass = Screenshot.save(driver, getClass().getSimpleName(), "emptyPassword_invalidBanner");
        if (emptyPass != null && ExtentTestNgListener.currentTest() != null) {
            ExtentTestNgListener.currentTest().info(
                "Invalid banner shown",
                MediaEntityBuilder.createScreenCaptureFromPath(emptyPass.getAbsolutePath()).build()
            );
        }
    }
}
