Feature: ping

  Background:
    * url baseUrl

  Scenario: Verify ping api returns 'hello appli info'
    Given path 'ping'
    When method get
    Then status 200
    And match response == 'hello appli info'