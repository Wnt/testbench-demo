package com.vaadin.training.it.steps;

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.parallel.setup.RemoteDriver;
import com.vaadin.testbench.parallel.setup.SetupDriver;
import com.vaadin.training.it.JBehaveTableMapper;
import com.vaadin.training.it.pages.ProductPage;
import com.vaadin.training.it.pages.ProductRow;
import org.jbehave.core.annotations.*;
import org.jbehave.core.model.ExamplesTable;
import org.junit.Assert;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.PageFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ProductListSteps extends TestBenchTestCase {

    private ProductPage productPage;

    @BeforeScenario(uponType = ScenarioType.ANY)
    public void beforeScenario() throws MalformedURLException {
        final String browser = System.getProperty("browser");

        Assert.assertNotNull(browser);

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities(browser, "", Platform.ANY);

        final RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), desiredCapabilities);

        setDriver(TestBench.createDriver(driver));
        getDriver().get("http://localhost:8080/");
    }

    @AfterScenario(uponType = ScenarioType.ANY)
    public void tearDown() {
        getDriver().quit();
    }

    @Given("an empty products table")
    public void givenAnEmptyProductsTable() {
        productPage = PageFactory.initElements(getDriver(), ProductPage.class);
    }

    @When("I add $quantity pcs of product $name with the price $price")
    @Alias("I add a product $name, $quantity and $price")
    public void whenIAddPcsOfProductWithThePrice(@Named("quantity") String quantity,
                                                 @Named("name") String name,
                                                 @Named("price") String price) {
        productPage.addProduct(new ProductRow(name, quantity, price));
    }

    @Then("the list contains one row of $quantity pcs of product $name with the price $price")
    public void thenTheListContainsOneRowOfPcsOfProductWithThePrice(String quantity, String name, String price) {
        Assert.assertTrue(productPage.listContainsProductAsFirst(new ProductRow(name, quantity, price)));
    }

    @Then("the quantity footer has the value $value")
    public void thenTheQuantityFooterHasTheValue(String value) {
        Assert.assertEquals("Sum: " + value, productPage.getQuantityFooter());
    }

    @Then("the price footer has the value $value")
    public void thenThePriceFooterHasTheValue(String value) {
        Assert.assertEquals("Sum: " + value, productPage.getPriceFooter());
    }

    @When("I add these products:$products")
    public void whenIAddTheseProducts(ExamplesTable table) {
        final List<ProductRow> products = JBehaveTableMapper.tableToBean(table, ProductRow.class);
        for(ProductRow product: products) {
            productPage.addProduct(product);
        }
    }

    @Then("the user should see a warning that the list is full")
    public void thenTheUserShouldSeeAWarningThatTheListIsFull() {
        Assert.assertTrue(productPage.isFullWarningShowing());
    }
}
