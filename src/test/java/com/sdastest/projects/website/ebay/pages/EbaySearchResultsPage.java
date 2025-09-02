package com.sdastest.projects.website.ebay.pages;

import com.sdastest.helpers.PropertiesHelpers;
import com.sdastest.utils.LogUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.sdastest.keywords.WebUI.*;

public class EbaySearchResultsPage {

    // Load locators from properties file
    private By searchResultsContainer = By.cssSelector(PropertiesHelpers.getValue("search_results_container"));
    private By firstSearchResult = By.cssSelector(PropertiesHelpers.getValue("first_search_result"));
    private By firstSearchResultAlt = By.cssSelector(PropertiesHelpers.getValue("first_search_result_alt"));
    private By firstSearchResultAlt2 = By.cssSelector(PropertiesHelpers.getValue("first_search_result_alt2"));
    private By searchResultTitle = By.cssSelector(PropertiesHelpers.getValue("search_result_title"));
    private By searchResultsCount = By.cssSelector(PropertiesHelpers.getValue("search_results_count"));
    private By noResultsMessage = By.cssSelector(PropertiesHelpers.getValue("no_results_message"));

    // Additional alternative selectors
    private By firstResultFallback = By.cssSelector(".s-item:first-child a");
    private By resultsCountAlt = By.cssSelector(".srp-controls__count-heading");

    public EbaySearchResultsPage() {
        PropertiesHelpers.loadAllFiles();
    }

    public void verifySearchResultsDisplayed(String searchTerm) {
        waitForPageLoaded();
        sleep(2);
        
        // Try multiple ways to verify search results
        try {
            waitForElementVisible(searchResultsContainer, 10);
            verifyElementPresent(searchResultsContainer, "Search results container not found");
        } catch (Exception e) {
            // Fallback verification
            verifyContains(getCurrentUrl(), "sch", "Not on search results page");
        }
        
        verifyContains(getPageTitle(), searchTerm, "Search term not found in page title");
        LogUtils.info("Search results displayed for: " + searchTerm);
    }

    public EbayProductPage clickFirstSearchResult() {
        waitForPageLoaded();
        sleep(2);
        
        boolean clicked = false;
        
        // Try multiple locators for first search result
        By[] resultLocators = {firstSearchResult, firstSearchResultAlt, firstSearchResultAlt2, firstResultFallback};
        
        for (By locator : resultLocators) {
            try {
                if (checkElementExist(locator)) {
                    waitForElementClickable(locator, 5);
                    clickElement(locator);
                    LogUtils.info("Clicked on first search result using locator: " + locator);
                    clicked = true;
                    break;
                }
            } catch (Exception e) {
                LogUtils.info("Failed to click first result with locator: " + locator + " - " + e.getMessage());
                continue;
            }
        }
        
        if (!clicked) {
            throw new RuntimeException("Could not click first search result with any locator method");
        }
        
        // Wait for new tab/window and switch to it
        sleep(3);
        switchToLastWindow();
        waitForPageLoaded();
        
        return new EbayProductPage();
    }

    public int getSearchResultsCount() {
        try {
            if (checkElementExist(searchResultsCount)) {
                String countText = getTextElement(searchResultsCount);
                // Extract number from text like "2,000,000+ results for Book"
                String[] parts = countText.split(" ");
                if (parts.length > 0) {
                    String numberPart = parts[0].replace(",", "").replace("+", "");
                    return Integer.parseInt(numberPart);
                }
            }
        } catch (Exception e) {
            LogUtils.info("Could not parse search results count: " + e.getMessage());
        }
        return 0;
    }

    public void verifyNoResults() {
        waitForElementVisible(noResultsMessage, 10);
        verifyElementPresent(noResultsMessage, "No results message not found");
        LogUtils.info("No search results message verified");
    }

    public void verifyResultsContainText(String expectedText) {
        List<WebElement> resultTitles = getWebElements(searchResultTitle);
        boolean found = false;
        
        for (WebElement title : resultTitles) {
            if (getTextElement(By.xpath("//h3")).toLowerCase().contains(expectedText.toLowerCase())) {
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new AssertionError("No search results contain the text: " + expectedText);
        }
        
        LogUtils.info("Search results contain expected text: " + expectedText);
    }
}