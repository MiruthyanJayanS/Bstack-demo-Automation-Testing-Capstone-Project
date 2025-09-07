package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;

public class CartTest extends BaseTest {

  @Test(priority = 1)
  public void testLoginAndOpenCart() throws Exception {
    new LoginPage(driver).loginDemo();
    CartPage cart = new CartPage(driver);
    cart.openCart();
    Thread.sleep(5000);
    passShot("Cart opened", "testLoginAndOpenCart_cartOpened");
  }

  @Test(priority = 2, dependsOnMethods = "testLoginAndOpenCart")
  public void testAddItemAndAssertQuantity() throws Exception {
    CartPage cart = new CartPage(driver);
    cart.clickAddToCart();
    Assert.assertTrue(cart.isQtyAdded(), "Cart quantity expected > 0");
    Thread.sleep(5000);
    passShot("Item added, quantity > 0", "testAddItemAndAssertQuantity_qtyVisible");
  }

  @Test(priority = 3, dependsOnMethods = "testAddItemAndAssertQuantity")
  public void testProceedToCheckout() throws Exception {
    CartPage cart = new CartPage(driver);
    cart.proceedToCheckout();
    Thread.sleep(5000);
    passShot("Proceeded to checkout", "testProceedToCheckout_checkoutPage");
  }
}
