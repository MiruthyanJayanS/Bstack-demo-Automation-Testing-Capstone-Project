package tests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.OrderConfirmationPage;

public class OrderConfirmationTest extends BaseTest {

  @Test(priority = 1)
  public void addItemOpenCartAndAssertQty() throws Exception {
    new LoginPage(driver).loginDemo();
    CartPage cart = new CartPage(driver);
    cart.clickAddToCart();
    cart.openCart();
    Assert.assertTrue(cart.isQtyAdded(), "Expected cart quantity > 0");
    Thread.sleep(5000);
    passShot("Cart opened with item quantity > 0", "addItemOpenCartAndAssertQty_cartWithQty");
  }

  @Test(priority = 2, dependsOnMethods = "addItemOpenCartAndAssertQty")
  public void proceedToCheckoutAndFillAddress() throws Exception {
    CartPage cart = new CartPage(driver);
    cart.proceedToCheckout();
    CheckoutPage checkout = new CheckoutPage(driver);
    checkout.fillAddress("John", "Doe", "221B Baker Street", "London", "NW1 6XE");
    checkout.clickShippingSubmit();
    Thread.sleep(5000);
    passShot("Shipping submitted", "proceedToCheckoutAndFillAddress_submitted");
  }

  @Test(priority = 3, dependsOnMethods = "proceedToCheckoutAndFillAddress")
  public void assertConfirmationAndDownloadReceipt() throws Exception {
    OrderConfirmationPage confirm = new OrderConfirmationPage(driver);

    Assert.assertTrue(confirm.isConfirmed(), "Order not confirmed"); 
    Assert.assertTrue(confirm.isReceiptVisible(Duration.ofSeconds(25)), "Receipt link missing"); 

    passShot("Order confirmation visible", "assertConfirmationAndDownloadReceipt_confirm");

    Path pdf = confirm.clickReceiptAndWaitForPdf(getDownloadDir(), Duration.ofSeconds(30));
    Assert.assertTrue(Files.exists(pdf), "Receipt not found at " + pdf);
    Assert.assertTrue(Files.size(pdf) > 0, "Receipt is empty: " + pdf);

    passShot("Receipt download triggered", "assertConfirmationAndDownloadReceipt_downloaded");
  }
}
