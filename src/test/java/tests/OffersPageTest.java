package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.LoginPage;
import pages.OffersPage;

public class OffersPageTest extends BaseTest {

	@Test(description = "Offers page: verify empty-state message after login")
	public void shouldShowNoPromotionalOffersMessage() {
		new LoginPage(driver).loginDemo();
		OffersPage offers = new OffersPage(driver);
		offers.openViaNavbar();
		passShot("Offers page opened", "offers_nav_opened");

		Assert.assertTrue(offers.waitForEmptyOffersMessageVisible(),
				"Offers message element did not become visible with text");

		String expected = "Sorry we do not have any promotional offers in your city.";
		Assert.assertEquals(offers.emptyOffersText(), expected, "Offers message did not match the expected text");

		passShot("Offers empty-state message visible", "offers_empty_state");
	}
}
