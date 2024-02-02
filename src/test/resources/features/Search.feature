Feature: Search for a stainless work table

  @task
  Scenario: Search for a keyword, add an item to cart, and clear the cart
    Given  I navigate to the Home Page for Webstaurant
    And    I click on the search field
    And    I enter in the search keywords "stainless work table"
    And    I click the search button
    Then   I verify each search results title contains the word table
    And    I add the last item found to my cart
    Then   I remove the item from my cart
    And    I confirm that I want to empty my cart
    And    I confirm that my cart is empty
