package stepdefinitions;

import io.cucumber.java.*;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import pages.LoginPage;


import java.time.Duration;

public class LoginSteps {

  private static WebDriver driver;
  private static WebDriverWait wait;
  private static LoginPage login;

  @BeforeAll
  public static void beforeAll() {
    driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
    driver.manage().window().maximize();
    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    login = new LoginPage(driver);
  }

  @AfterAll
  public static void afterAll() {
    if (driver != null) driver.quit();
  }

  @Given("the user opens the sign in page")
  public void the_user_opens_the_sign_in_page() {
    login.openSignin();
    Assert.assertTrue(login.at(), "Sign in not visible");
  }

  @Then("the login page is visible")
  public void the_login_page_is_visible() {
    Assert.assertTrue(login.at(), "Sign in not visible");
  }

  @When("the user logs in as {string} with password {string}")
  public void the_user_logs_in_as_with_password(String user, String pass) {
    if (user != null && !user.isEmpty()) login.chooseUsername(user);
    if (pass != null && !pass.isEmpty()) login.choosePassword(pass);
    login.submit();
  }

  @Then("the app navigates to the home page")
  public void the_app_navigates_to_the_home_page() {
    wait.until(d -> login.isLoggedInUrl());
    Assert.assertTrue(login.isLoggedInUrl(), "Not navigated after login");
  }

  @Then("the app stays on the sign in page")
  public void the_app_stays_on_the_sign_in_page() {
    wait.until(ExpectedConditions.urlContains("/signin"));
    Assert.assertTrue(login.isOnSignin(), "Should stay on Sign In");
  }
}
