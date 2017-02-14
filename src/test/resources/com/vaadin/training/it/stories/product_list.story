Scenario: Adding products to the list
Given an empty products table
When I add <quantity> pcs of product <name> with the price <price>
Then the list contains one row of <quantity> pcs of product <name> with the price <price>

Examples:
    |name   |quantity   |price  |
    |Wrench |2          |5      |
    |Pie    |3          |12     |

Scenario: Adding products updates the quantity sum in the footer
Given an empty products table
When I add a product Potato, 2 and 1
Then the quantity footer has the value 2

Scenario: Adding products updates the price sum in the footer
Given an empty products table
When I add a product Potato, 2 and 1
Then the price footer has the value 1

Scenario: Adding more than 6 products shows a warning
Given an empty products table
When I add these products:
    |Name   |Quantity   |Price  |
    |Wrench |1          |2      |
    |Bag    |3          |4      |
    |Mug    |5          |6      |
    |TV     |7          |8      |
    |Bike   |9          |10     |
    |Car    |11         |12     |
    |House  |13         |14     |
Then the user should see a warning that the list is full
