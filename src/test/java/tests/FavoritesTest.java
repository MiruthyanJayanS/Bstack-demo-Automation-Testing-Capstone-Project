package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.FavoritesPage;
import pages.LoginPage;

public class FavoritesTest extends BaseTest {

  @Test(priority = 1)
  public void testAddFavorite() throws Exception {
    new LoginPage(driver).loginDemo();
    FavoritesPage fav = new FavoritesPage(driver);
    fav.clickAddToFavorites();
    Thread.sleep(5000);
    passShot("Heart clicked (added to favorites)", "testAddFavorite_heartClicked");
  }

  @Test(priority = 2, dependsOnMethods = "testAddFavorite")
  public void testOpenFavouritesAndAssert() throws Exception {
    FavoritesPage fav = new FavoritesPage(driver);
    fav.openFavouritesTab();
    Assert.assertTrue(fav.isFavouritesPageVisible(), "Favourites page not visible after navigation");
    Thread.sleep(3000);
    passShot("Favourites tab open and visible", "testOpenFavouritesAndAssert_tabOpen");
  }
}
