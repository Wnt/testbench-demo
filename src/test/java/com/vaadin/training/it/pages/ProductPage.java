package com.vaadin.training.it.pages;

import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.NotificationElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.math.BigDecimal;

public class ProductPage extends TestBenchTestCase {

    @FindBy(id = "name")
    WebElement name;

    @FindBy(id = "quantity")
    WebElement quantity;

    @FindBy(id = "price")
    WebElement price;

    @FindBy(id = "add")
    WebElement add;

    public ProductPage(WebDriver driver) {
        setDriver(driver);
    }

    public void addProduct(ProductRow p) {
        name.sendKeys(p.getName());

        quantity.sendKeys(p.getQuantity());

        price.sendKeys(p.getPrice());

        add.click();
    }

    public boolean listContainsProductAsFirst(ProductRow p) {
        final GridElement gridElement = $(GridElement.class).first();
        String name = gridElement.getCell(0, 0).getText();
        String price = gridElement.getCell(0, 1).getText();
        String quantity = gridElement.getCell(0, 2).getText();

        return p.getName().equals(name) && p.getPrice().equals(price) && p.getQuantity().equals(quantity);
    }

    public String getQuantityFooter() {
        final GridElement.GridCellElement footerCell = $(GridElement.class).first().getFooterCell(0, 2);
        return footerCell.getText();
    }

    public String getPriceFooter() {
        final GridElement.GridCellElement footerCell = $(GridElement.class).first().getFooterCell(0, 1);
        return footerCell.getText();
    }

    public boolean isFullWarningShowing() {
        return $(NotificationElement.class).exists() &&
                "humanized".equals($(NotificationElement.class).first().getType());
    }
}
