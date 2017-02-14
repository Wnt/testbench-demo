package com.vaadin.training.it;

import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.annotations.BrowserConfiguration;
import com.vaadin.testbench.annotations.RunOnHub;
import com.vaadin.testbench.parallel.Browser;
import com.vaadin.testbench.parallel.ParallelTest;
import com.vaadin.training.it.pages.ProductPage;
import com.vaadin.training.it.pages.ProductRow;
import org.junit.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Ignore
@RunOnHub
public class ProductListTests extends ParallelTest {

    @Rule
    public ScreenshotOnFailureRule screenshotOnFailureRule = new ScreenshotOnFailureRule(this, true);

    @Before
    public void setUp() throws MalformedURLException {
       /* final DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome().merge(DesiredCapabilities.firefox());
        final RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), desiredCapabilities);

        setDriver(TestBench.createDriver(driver));*/
        getDriver().get("http://localhost:8080/");
    }

    @BrowserConfiguration
    public List<DesiredCapabilities> firefoxAndChromeConfiguration() {
        List<DesiredCapabilities> list = new ArrayList<DesiredCapabilities>();
        list.add(Browser.FIREFOX.getDesiredCapabilities());
        list.add(Browser.CHROME.getDesiredCapabilities());
        return list;
    }

    @Test
    public void productListEmpty_addProduct_productAddedToList() {
        ProductPage productPage = PageFactory.initElements(getDriver(),
                ProductPage.class);

        final ProductRow productRow = new ProductRow("Foobar", "2", "3");
        productPage.addProduct(productRow);

        Assert.assertTrue(productPage.listContainsProductAsFirst(productRow));
    }

    @Test
    public void productListEmpty_addProduct_footerQuantityUpdated() {
        ProductPage productPage = PageFactory.initElements(getDriver(),
                ProductPage.class);

        productPage.addProduct(new ProductRow("Foobar", "2", "3"));

        Assert.assertEquals("Sum: 2", productPage.getQuantityFooter());
    }

    @Test
    public void productListEmpty_addProduct_footerPriceUpdated() {
        ProductPage productPage = PageFactory.initElements(getDriver(),
                ProductPage.class);

        productPage.addProduct(new ProductRow("Foobar", "2", "3"));

        Assert.assertEquals("Sum: 3", productPage.getPriceFooter());
    }

    @Test
    public void productListEmpty_add7Products_warningShown() {
        ProductPage productPage = PageFactory.initElements(getDriver(),
                ProductPage.class);

        for (int i = 0; i < 7; i++) {
            productPage.addProduct(new ProductRow("Foobar", "2", "3"));
        }

        Assert.assertTrue(productPage.isFullWarningShowing());
    }

    @Test
    public void productListEmpty_add5Products_screenshotMatches() throws IOException {
        ProductPage productPage = PageFactory.initElements(getDriver(),
                ProductPage.class);

        for (int i = 0; i < 5; i++) {
            productPage.addProduct(new ProductRow("Foobar", "2", "3"));
        }

        Assert.assertTrue(testBench().compareScreen("productlist"));
    }
}
