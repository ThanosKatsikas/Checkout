# Checkout - Kode Kata

A Implementation for a Supermarket checkout. 

To run Tests, clone repository and execute:
mvn test or mvn verify

Main Class has no implementation. This is not a standalone application. You can see various test cases and execution examples in:

checkout/src/test/java/com/checkoutKata/services/SuperMarketCheckoutTest.java

If we wanted this to be standalone we would need:
* A method to parse args we pass to the jar (For example we would run: checkout-1.0-SNAPSHOT.jar A B A C A A .. etc) and it would then use scanItem for each one of them to scan the items. We would need validation rules for items passed. We would also need to add some items and their prices like we do in the tests. Furthermore we would need to print more messages and interact by logging information. 
* If we wanted to make the item prices and offers dynamic we could load everything from CSV files. Again we would need to validate them as we load everything. 
    * Item price file could be in the following format (SKU, Price)
    * Offers could be (SKU, NumberOfItems, SpecialPrice).


## Design Notes:

### Consists of the following packages:
* Exceptions: Holds any sort of exceptions we’d like to register and later handle.
* Model: Holds the entities we created to model the checkout.
* Repository: Holds the classes responsible for storing and fetching entities.
* Services: Functional modules of the application. 

### Exceptions
We have two types of exceptions:
- ItemNotFoundException: We raise it when we are trying to fetch an item that doesn’t exist
- OfferNotFoundException: We raise it when we are trying to fetch an offer that doesn’t exist 

### Model
- Abstract Class Item:  Attributes are the Stock Keeping Unit and the Unit Price. In a more detailed implementation we would have different attributes for each type of item (For example we could have perishables with an expiration date), which we’d like to add other attributes but all types of items would always have the two attributes in the abstract class. We can get the SKU and the Unit Price
- Grocery Item extends Item, but doesn’t really add anything extra. 
- Abstract Class Offer: Same reasoning as the Item Class. We may want to model different types of Offer. However they all refer to an item (maybe they could refer to more than one, but it’s not currently supported) and they all would introduce some sort of discount. The Offer class has an abstract method that should calculate any reductions, a method to get the discount and a method to get the item we’re referring to. 
- MultiBuyOffer extends the Offer. Extra attributes are the number of Items we want to have in the offer and the special price for the lot. We have a method to get the number of items, and one to get the Special price. We calculate the discount when we instantiate an Offer.

### Repository
- ItemStore: An interface that if implemented should allow to store an item, get the items in stock and retrieve the price of an item given an SKU.
- SuperMarketWarehouse class: Implements the ItemStore interface. We use a HashMat that the key is the SKU and the value the item object the SKU refers to.
- OfferStore: An interface that if implemented should allow to store an offer, get an offer based on an SKU and a non implemented method that would get us a HashSet of all the offers referring to an SKU (assuming that we have more than one offer per item)
- SuperMarketOfferStore: Implements the OfferStore interface. We use a HashMap that the key is the SKU and the value is the Offer for the item the SKU refers to.

### Services
- Checkout: An interface that if implemented should allow to calculate the total, calculate any savings and calculate the amount to pay.
- SuperMarketCheckout: Implements Checkout. It requires a warehouse (It could have a ItemStore passed, but I guess it’s a specific supermarket Implementation), and the OffersService(See bellow). We hold the items scanned in a HashMap that key is the SKU and value is the number of items scanned.
    -  In this implementation if we scan an item that doesn’t exist in the warehouse we handle the exception and just add zero. 
    - We calculate savings by using the offers service to fetch us the offer and multiply the savings of the offer with the quotient of items scanned and items required for the offer.
    - Amount to pay is the total minus the calculated savings.
- OffersService: An interface that provides a method to check if we have an offer related to an SKU and a method to register offers. 
- SuperMarketOffersService: Implements OffersService interface and can check if an offer exists(only used in tests), can get an offer(will return null if not found) and register an offer. 





