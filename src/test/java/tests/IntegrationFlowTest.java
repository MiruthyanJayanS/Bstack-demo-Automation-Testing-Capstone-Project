package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.CartPage;
import pages.CheckoutPage;
import pages.OrderConfirmationPage;

import java.time.Duration;
import java.nio.file.Path;

public class IntegrationFlowTest extends BaseTest {

	@Test(description = "E2E: Login -> Cart -> Checkout -> Order Confirmation")
	public void endToEndUserJourney() throws InterruptedException {
		LoginPage login = new LoginPage(driver);
		login.loginDemo();
		Thread.sleep(2000);
		passShot("Logged in and landed on catalog", "it_login_ok");

		CartPage cart = new CartPage(driver);
		cart.clickAddToCart();
		cart.openCart();
		Assert.assertTrue(cart.isQtyAdded(), "Cart quantity expected > 0");
		Thread.sleep(2000);
		passShot("Cart opened with item(s)", "it_cart_ok");

		cart.proceedToCheckout();
		Thread.sleep(2000);
		passShot("Checkout page opened", "it_checkout_open");

		CheckoutPage checkout = new CheckoutPage(driver);
		checkout.fillAddress("Jackie", "Chan", "1 Gandhi Street", "Mumbai", "400051");
		passShot("Checkout address filled", "it_checkout_filled");
		checkout.clickShippingSubmit();
		Thread.sleep(2000);
		passShot("Checkout address submitted", "it_checkout_submitted");

		OrderConfirmationPage oc = new OrderConfirmationPage(driver);
		Assert.assertTrue(oc.isConfirmed(), "Order confirmation banner not visible");
		boolean receiptVisible = oc.isReceiptVisible(Duration.ofSeconds(25));
		Assert.assertTrue(receiptVisible, "Receipt link not visible");
		Thread.sleep(2000);
		passShot("Order confirmed; receipt visible", "it_order_confirmation");

		Path pdf = oc.clickReceiptAndWaitForPdf(getDownloadDir(), Duration.ofSeconds(20));
		Assert.assertNotNull(pdf, "Receipt PDF not detected in download directory");
		Thread.sleep(2000);
		passShot("Receipt PDF downloaded", "it_receipt_downloaded");
	}
}
