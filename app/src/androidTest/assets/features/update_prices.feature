Feature: Updates prices from API

  Background:
    Given I have a list of at least one symbol

  Scenario: Pull to refresh
    When I swipe down on the list
    Then the list moves down and I see text explaining refresh
    When I let go
    Then the list stays down
    And refresh indicator appears
    And (within a few seconds), list moves back up
    And prices in list are updated

  Scenario: Automatic refresh
    Given I have auto refresh turned on
    When the timer goes off every X minutes
    Then I see a small indicator in app bar that refresh is running
    And within a few seconds, prices in list are updated