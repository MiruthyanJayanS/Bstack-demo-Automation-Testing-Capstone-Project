package tests;

import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;

import java.io.InputStream;
import java.util.Properties;

public class LoginTest extends BaseTest {

  private LoginPage login;
  private String user;
  private String pwd;

  @BeforeClass(alwaysRun = true)
  public void loadData() throws Exception {
    Properties p = new Properties();
    try (InputStream in = Thread.currentThread()
        .getContextClassLoader()
        .getResourceAsStream("testdata/login.properties")) {
      if (in != null) p.load(in);
    }
    user = p.getProperty("username", "demouser");
    pwd  = p.getProperty("password", "testingisfun99");
  }

  @BeforeMethod(alwaysRun = true)
  public void initPage() {
    login = new LoginPage(driver);
  }

  @Test
  public void openLoginPage() {
    login.openSignin();
    Assert.assertTrue(login.at(), "Sign in not visible");
    passShot("Sign-in visible", "openLoginPage");
  }

  @Test(dependsOnMethods = "openLoginPage")
  public void validLogin() {
    login.loginDemo();
    Assert.assertTrue(login.isLoggedInUrl(), "Not navigated after login");
    passShot("Dashboard after valid login", "validLogin_dashboard");
  }

  @Test(dependsOnMethods = "openLoginPage")
  public void invalidPassword() {
    login.openSignin();
    login.chooseUsername(user);
    login.choosePassword("wrongpass");
    login.submit();
    login.waitForInvalidMessage();
    Assert.assertTrue(login.isOnSignin(), "Should stay on Sign In");
    passShot("Invalid password banner shown", "invalidPassword_banner");
  }

  @Test(dependsOnMethods = "openLoginPage")
  public void emptyUsername() {
    login.openSignin();
    login.choosePassword(pwd);
    login.submit();
    login.waitForInvalidMessage();
    Assert.assertTrue(login.isOnSignin(), "Should stay on Sign In");
    passShot("Empty username banner shown", "emptyUsername_banner");
  }

  @Test(dependsOnMethods = "openLoginPage")
  public void emptyPassword() {
    login.openSignin();
    login.chooseUsername(user);
    login.submit();
    login.waitForInvalidMessage();
    Assert.assertTrue(login.isOnSignin(), "Should stay on Sign In");
    passShot("Empty password banner shown", "emptyPassword_banner");
  }
}
