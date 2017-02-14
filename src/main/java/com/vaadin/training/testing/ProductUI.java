package com.vaadin.training.testing;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;
import java.math.BigDecimal;

/**
 * Show a list of products and their sum in the footer.
 * If the sums are too large, a text is displayed instead.
 */
@Theme("valo")
public class ProductUI extends UI {


    private Grid grid;
    private TextField name;
    private TextField quantity;
    private TextField price;
    private Button add;

    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
    }

    private void buildLayout() {
        VerticalLayout mainlayout = new VerticalLayout();
        mainlayout.setWidth("100%");
        mainlayout.setMargin(true);

        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.setMargin(true);

        fields.addComponents(name, price, quantity, add);
        fields.setComponentAlignment(add, Alignment.BOTTOM_RIGHT);

        mainlayout.addComponents(fields, grid);
        mainlayout.setComponentAlignment(fields, Alignment.TOP_CENTER);
        mainlayout.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);

        setContent(mainlayout);

        name.focus();
    }

    private void configureComponents() {
        grid = new Grid();
        grid.setWidth("750px");
        BeanItemContainer<Product> container = new BeanItemContainer<Product>(Product.class);

        grid.setContainerDataSource(container);
        grid.setId("grid");
        final Grid.FooterRow footerRow = grid.appendFooterRow();

        name = new TextField("Name");
        name.setNullRepresentation("");
        name.setId("name");
        quantity = new TextField("Quantity");
        quantity.setNullRepresentation("");
        quantity.setId("quantity");
        price = new TextField("Price");
        price.setNullRepresentation("");
        price.setId("price");

        BeanFieldGroup<Product> group = new BeanFieldGroup<>(Product.class);
        group.setItemDataSource(new Product());
        group.bind(name, "name");
        group.bind(quantity, "quantity");
        group.bind(price, "price");

        add = new Button("Add product");
        add.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        add.setId("add");
        add.addClickListener((clickEvent) -> {

            if (container.size() >= 6) {
                Notification.show("Product list full", Notification.Type.HUMANIZED_MESSAGE);
            } else {
                try {
                    group.commit();
                    container.addBean(group.getItemDataSource().getBean());
                    group.setItemDataSource(new Product());
                    updateFooterValues();
                    name.focus();

                } catch (FieldGroup.CommitException e) {
                    Notification.show("Error adding product", Notification.Type.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateFooterValues() {
        int quantitySum = 0;
        BigDecimal priceSum = BigDecimal.ZERO;
        for (Object id : grid.getContainerDataSource().getItemIds()) {
            final Product product = (Product) id;
            quantitySum += product.getQuantity();
            priceSum = priceSum.add(product.getPrice());
        }

        final Grid.FooterRow footerRow = grid.getFooterRow(0);

        final Grid.FooterCell quantityFooter = footerRow.getCell("quantity");
        String quantityString = quantitySum > 100 ? "+100" : Integer.toString(quantitySum);
        quantityFooter.setText("Sum: " + quantityString);

        final Grid.FooterCell priceFooter = footerRow.getCell("price");
        String priceString = priceSum.compareTo(new BigDecimal(1000)) > 0 ? "+1000" : priceSum.toString();
        priceFooter.setText("Sum: " + priceString);
    }

    @WebServlet(urlPatterns = {"/*"}, name = "ProductServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ProductUI.class, productionMode = false,widgetset="com.vaadin.training.Widgetset")
    public static class MyUIServlet extends VaadinServlet {
    }
}
