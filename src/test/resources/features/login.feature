Feature: Login (smoke)

  @smoke
  Scenario: Open login page
    Given the user opens the sign in page
    Then the login page is visible

  @smoke
  Scenario: Valid login
    Given the user opens the sign in page
    When the user logs in as "demouser" with password "testingisfun99"
    Then the app navigates to the home page
