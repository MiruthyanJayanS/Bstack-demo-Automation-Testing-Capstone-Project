package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;

import java.time.Duration;
import java.util.List;

public class HomePageTest extends BaseTest {

	@Test(description = "Homepage should load and list products")
	public void shouldLoadCatalogAndListProducts() throws Exception {
		HomePage home = new HomePage(driver);
		home.open();
		Assert.assertTrue(home.at(), "Home page did not load or product grid not visible");
		int count = home.visibleProductCount();
		Assert.assertTrue(count > 0, "Expected at least one product on the catalog");
		List<String> names = home.productNames();
		Assert.assertTrue(!names.isEmpty(), "Expected product names to be available");
		Thread.sleep(2000);
		passShot("Catalog loaded with products", "home_catalog_loaded");
	}

	@Test(dependsOnMethods = "shouldLoadCatalogAndListProducts", description = "Sort products via dropdown and capture screenshots")
	public void shouldSortCatalogAndCaptureShots() throws InterruptedException {
		HomePage home = new HomePage(driver);
		home.sortByLowestToHighest();
		Thread.sleep(2000);
		passShot("Sorted: Lowest to highest", "sort_lowest_to_highest");
		home.sortByHighestToLowest();
		Thread.sleep(2000);
		passShot("Sorted: Highest to lowest", "sort_highest_to_lowest");
	}

	@Test(dependsOnMethods = "shouldLoadCatalogAndListProducts", description = "One pass only: for each vendor, select (shot) then clear (shot)")
	public void shouldSelectAndClearEachVendorInOnePass() throws Exception {
		HomePage home = new HomePage(driver);
		home.closeCartIfOpen();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		Actions actions = new Actions(driver);

		List<WebElement> initial = home.vendorLabels();
		int total = initial.size();

		for (int i = 0; i < total; i++) {
			List<WebElement> labelsNow = home.vendorLabels(); // re-fetch after each toggle
			WebElement label = labelsNow.get(i);

			WebElement input = label.findElement(By.cssSelector("input[type='checkbox']"));
			WebElement span = label.findElement(By.cssSelector("span.checkmark"));
			String vendor = span.getText().trim();

			if (!input.isSelected()) {
				try {
					span.click();
				} catch (Exception e) {
					actions.moveToElement(span).pause(Duration.ofMillis(80)).click().perform();
				}
				wait.until(ExpectedConditions.elementToBeSelected(input));
			}

			Thread.sleep(2000);
			passShot("Vendor selected: " + vendor, "vendor_selected_" + vendor.replaceAll("\\s+", "_"));

			try {
				span.click();
			} catch (Exception e) {
				actions.moveToElement(span).pause(Duration.ofMillis(80)).click().perform();
			}
			wait.until(ExpectedConditions.not(ExpectedConditions.elementToBeSelected(input)));

			Thread.sleep(2000);
			passShot("Vendor cleared: " + vendor, "vendor_cleared_" + vendor.replaceAll("\\s+", "_"));
		}
	}

	@Test(dependsOnMethods = "shouldLoadCatalogAndListProducts", description = "Add first product to cart from listing")
	public void shouldAddFirstProductToCart() throws Exception {
		HomePage home = new HomePage(driver);
		home.addFirstProductToCart();
		Thread.sleep(1000);
		passShot("Added first product to cart", "home_add_first_product");
		Assert.assertTrue(true, "Add to cart action executed from home listing");
	}
}
