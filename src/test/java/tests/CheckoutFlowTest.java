package tests;

import org.testng.annotations.Test;

public class CheckoutFlowTest extends BaseTest {

  @Test(priority = 1)
  public void testLogin() throws Exception {
    stepLoginDemo();
    Thread.sleep(5000);
    passShot("Logged in", "testLogin_loggedIn");
  }

  @Test(priority = 2, dependsOnMethods = "testLogin")
  public void testAddToCart() throws Exception {
    stepAddOneItemOpenCartAssertQty();
    Thread.sleep(5000);
    passShot("Item added and cart quantity > 0", "testAddToCart_qtyVisible");
  }

  @Test(priority = 3, dependsOnMethods = "testAddToCart")
  public void testProceedToCheckout() throws Exception {
    stepProceedToCheckout();
    Thread.sleep(5000);
    passShot("Proceeded to checkout", "testProceedToCheckout_checkoutPage");
  }

  @Test(priority = 4, dependsOnMethods = "testProceedToCheckout")
  public void testFillAddressAndSubmit() throws Exception {
    stepFillAddressAndSubmit();
    Thread.sleep(5000);
    passShot("Shipping submitted", "testFillAddressAndSubmit_submitted");
  }

  @Test(priority = 5, dependsOnMethods = "testFillAddressAndSubmit")
  public void testOrderConfirmationAndReceipt() throws Exception {
    stepAssertOrderAndReceipt();
    Thread.sleep(5000);
    passShot("Order confirmed and receipt visible", "testOrderConfirmationAndReceipt_confirm");
  }
}
