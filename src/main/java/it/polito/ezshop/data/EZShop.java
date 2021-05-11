package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EZShop implements EZShopInterface {

    HashMap<Integer, UserImpl> users;
    HashMap<Integer, SaleTransactionImpl> sales;
    HashMap<Integer, ProductTypeImpl> products;
    HashMap<Integer, OrderImpl> orders;
    HashMap<Integer, BalanceOperationImpl> operations;
    HashMap<Integer, ReturnTransaction> returns;
    HashMap<Integer, CustomerImpl> customers;
    HashMap<String, LoyaltyCard> cards;
    User loggedInUser;

    public EZShop() {
        this.users = FileRead.readUsers("users.json");
        this.sales = FileRead.readSales("sales.json");
        this.products = new HashMap<>();
        this.orders = new HashMap<>();
        this.operations = new HashMap<>();
        this.returns = FileRead.readReturns("returns.json"); 
        this.customers = new HashMap<>();
        this.cards = new HashMap<>();
        loggedInUser = null;
        
        // TODO: restore references between classes
        // sales -> productType
        // returns -> productType

        // returns -> sales
        for (ReturnTransaction ret : returns.values()) {
        	ret.setTransaction(sales.get(ret.getTransaction().getTicketNumber()));
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        //Check username correctness
        if (username.isEmpty() || username == null) {
            throw new InvalidUsernameException();
        }

        //Check password correctness
        if (password.isEmpty() || password == null) {
            throw new InvalidPasswordException();
        }

        //Check if username already exists
        if (users.values().stream().anyMatch(u -> u.getUsername().contentEquals(username))) {
            return -1;
        }

        //Check role correctness
        if (role.isEmpty() || role == null
                || (!role.contentEquals("Administrator")
                && !role.contentEquals("Cashier")
                && !role.contentEquals("ShopManager"))
        ) {
            throw new InvalidRoleException();
        }

        UserImpl newOne = new UserImpl(username, password, role);
        users.put(newOne.getId(), newOne);
        if (FileWrite.writeUsers("users.json", users)){
            return newOne.getId();
        }

        //db unreachable
        users.remove(newOne.getId());
        return -1;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

        //Check access rights
        if (loggedInUser == null || !loggedInUser.getRole().contentEquals("Administrator")) {
            throw new UnauthorizedException();
        }

        //Check id correctness
        if (id == null || id <= 0) {
            throw new InvalidUserIdException();
        }

        //Remove if present
        if (users.containsKey(id)) {
            UserImpl eliminated = users.get(id);
            users.remove(id);
            if (FileWrite.writeUsers("users.json", users))
                return true;

            //reinsert it if something goes wrong with the db
            users.put(eliminated.getId(), eliminated);
        }

        return false;
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {

        //Check access rights
        if (loggedInUser == null || !loggedInUser.getRole().contentEquals("Administrator")) {
            throw new UnauthorizedException();
        }

        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

        //Check access rights
        if (loggedInUser == null || !loggedInUser.getRole().contentEquals("Administrator")) {
            throw new UnauthorizedException();
        }

        //Check id correctness
        if (id == null || id <= 0) {
            throw new InvalidUserIdException();
        }

        //Return it if present
        if (users.containsKey(id)) {
            return users.get(id);
        }

        return null;
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {

        //Check access rights
        if (loggedInUser == null || !loggedInUser.getRole().contentEquals("Administrator")) {
            throw new UnauthorizedException();
        }

        //Check id correctness
        if (id == null || id <= 0) {
            throw new InvalidUserIdException();
        }

        //Check role correctness
        if (role.isEmpty() || role == null
                || (!role.contentEquals("Administrator")
                && !role.contentEquals("Cashier")
                && !role.contentEquals("ShopManager"))
        ) {
            throw new InvalidRoleException();
        }

        //Set if present
        if (users.containsKey(id)) {
            String precRole = users.get(id).getRole();
            users.get(id).setRole(role);

            if (FileWrite.writeUsers("users.json", users))
                return true;

            //return to the previous state if something goes wrong
            users.get(id).setRole(precRole);
        }

        return false;
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {

        //Check username correctness
        if (username.isEmpty() || username == null) {
            throw new InvalidUsernameException();
        }

        //Check password correctness
        if (password.isEmpty() || password == null) {
            throw new InvalidPasswordException();
        }

        //Retrieving user with that username
        Optional<UserImpl> optionalUser = users.values().stream().filter(u -> u.getUsername().contentEquals(username)).findFirst();

        //Checking if username and password match
        if (optionalUser.isPresent() && optionalUser.get().getPassword().contentEquals(password)) {
            loggedInUser = optionalUser.get();
        } else {
            loggedInUser = null;
        }

        return loggedInUser;
    }

    @Override
    public boolean logout() {

        //Check if there is a logged user
        if (loggedInUser == null) {
            return false;
        }

        loggedInUser = null;
        return true;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {

        //Check access rights
        if (loggedInUser == null ||
                (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))) {
            throw new UnauthorizedException();
        }

        //Check description correctness
        if (description.isEmpty() || description == null) {
            throw new InvalidProductDescriptionException();
        }

        //Check productCode correctness
        if (productCode.isEmpty() || productCode == null || !ProductTypeImpl.checkBarCode(productCode)) {
            throw new InvalidProductCodeException();
        }

        //Check price correctness
        if (pricePerUnit <= 0) {
            throw new InvalidPricePerUnitException();
        }

        //Check if barcode already exists
        if (products.values().stream().anyMatch(p -> p.getBarCode().contentEquals(productCode))) {
            return -1;
        }

        ProductTypeImpl newOne = new ProductTypeImpl(productCode, description, pricePerUnit, note);
        products.put(newOne.getId(), newOne);
        // TODO save in memory and check the result of the operation
        return newOne.getId();

    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {

        //Check access rights
        if (loggedInUser == null ||
                (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))) {
            throw new UnauthorizedException();
        }

        //Check description correctness
        if (newDescription.isEmpty() || newDescription == null) {
            throw new InvalidProductDescriptionException();
        }

        //Check productCode correctness
        if (newCode.isEmpty() || newCode == null || !ProductTypeImpl.checkBarCode(newCode)) {
            throw new InvalidProductCodeException();
        }

        //Check price correctness
        if (newPrice <= 0) {
            throw new InvalidPricePerUnitException();
        }

        //Check id correctness
        if (id == null || id <= 0) {
            throw new InvalidProductIdException();
        }

        //Check if product exists
        if (products.containsKey(id)) {
            ProductTypeImpl chosen = products.get(id);
            if (!chosen.getBarCode().contentEquals(newCode)) {
                //The new barcode differs from the original one thus we need to check if it is still unique.
                if (products.values().stream().anyMatch(p -> p.getBarCode().contentEquals(newCode)))
                    return false;
            }
            chosen.setBarCode(newCode);
            chosen.setNote(newNote);
            chosen.setProductDescription(newDescription);
            chosen.setPricePerUnit(newPrice);
            // TODO save in memory and check the result of the operation
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {

        //Check access rights
        if (loggedInUser == null ||
                (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))) {
            throw new UnauthorizedException();
        }

        //Check id correctness
        if (id == null || id <= 0) {
            throw new InvalidProductIdException();
        }

        if (products.containsKey(id)) {
            products.remove(id);
            // TODO save in memory and check the result of the operation
            return true;
        }

        return false;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {

        //Check access rights
        if (loggedInUser == null) {
            throw new UnauthorizedException();
        }

        return new ArrayList<>(products.values());

    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {

        //Check access rights
        if (loggedInUser == null ||
                (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))) {
            throw new UnauthorizedException();
        }

        //Check productCode correctness
        if (barCode.isEmpty() || barCode == null || !ProductTypeImpl.checkBarCode(barCode)) {
            throw new InvalidProductCodeException();
        }

        //Retrieve the correct one
        Optional<ProductTypeImpl> productOptional = products.values().stream()
                .filter(p -> p.getBarCode().contentEquals(barCode)).findFirst();

        return productOptional.orElse(null);

    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {

        //Check access rights
        if (loggedInUser == null ||
                (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))) {
            throw new UnauthorizedException();
        }

        //null description treated as empty string
        if (description == null)
            description = "";

        String finalVal = description;
        return products.values().stream().filter(p -> p.getProductDescription().contains(finalVal)).
                collect(Collectors.toList());
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {

        //Check access rights
        if (loggedInUser == null ||
                (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))) {
            throw new UnauthorizedException();
        }

        //Check id correctness
        if (productId == null || productId <= 0) {
            throw new InvalidProductIdException();
        }

        if (products.containsKey(productId)) {
            ProductTypeImpl chosen = products.get(productId);
            if ((chosen.getQuantity() + toBeAdded) < 0 || chosen.getLocation() == null) {
                return false;
            }
            chosen.setQuantity(chosen.getQuantity() + toBeAdded);
            // TODO save in memory and check the result of the operation
            return true;
        }

        return false;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {

        //Check access rights
        if (loggedInUser == null ||
                (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))) {
            throw new UnauthorizedException();
        }

        //Check id correctness
        if (productId == null || productId <= 0) {
            throw new InvalidProductIdException();
        }

        //Check newPos correctness
        if (!newPos.matches(".+-.+-.+") && !newPos.isEmpty()) {
            throw new InvalidLocationException();
        }

        if (products.containsKey(productId)) {
            ProductTypeImpl chosen = products.get(productId);
            if (!chosen.getLocation().contentEquals(newPos)) {
                //The new location differs from the original one thus we need to check if it is still unique.
                if (products.values().stream().anyMatch(p -> p.getLocation().contentEquals(newPos)))
                    return false;
            }
            chosen.setLocation(newPos);
            // TODO save in memory and check the result of the operation
            return true;
        }

        return false;

    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {

        //Check access rights
        if (loggedInUser == null ||
                (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))) {
            throw new UnauthorizedException();
        }

        //Check price correctness
        if (pricePerUnit <= 0) {
            throw new InvalidPricePerUnitException();
        }

        //Check quantity correctness
        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }

        //Check correctness of barcode and retrieve the correct one if exists
        ProductType chosen = this.getProductTypeByBarCode(productCode);

        if (chosen != null) {
            OrderImpl newOne = new OrderImpl(chosen, quantity, pricePerUnit);
            orders.put(newOne.getOrderId(), newOne);
            // TODO save in memory and check the result of the operation
            return newOne.getOrderId();
        }

        return -1;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {

        //Check access rights
        if (loggedInUser == null ||
                (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))) {
            throw new UnauthorizedException();
        }

        //Check price correctness
        if (pricePerUnit <= 0) {
            throw new InvalidPricePerUnitException();
        }

        //Check quantity correctness
        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }

        //Check correctness of barcode and retrieve the correct one if exists
        ProductType chosen = this.getProductTypeByBarCode(productCode);

        if (chosen == null) {
            return -1;
        }

        OrderImpl newOrd = new OrderImpl(chosen, quantity, pricePerUnit);
        double cost = quantity * pricePerUnit;

        //Check balance
        if ((this.computeBalance() - cost) < 0) {
            return -1;
        }

        //Create new balanceOperation
        BalanceOperationImpl newOp = new BalanceOperationImpl(-cost, "order");

        newOrd.setBalanceId(newOp.getBalanceId());
        newOrd.setStatus("payed");
        orders.put(newOrd.getOrderId(), newOrd);
        // TODO save in memory and check the result of the operation
        return newOrd.getOrderId();
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {

        //Check access rights
        if (loggedInUser == null ||
                (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))) {
            throw new UnauthorizedException();
        }

        //Check id correctness
        if (orderId == null || orderId <= 0) {
            throw new InvalidOrderIdException();
        }

        //Check presence of the specified order
        if (!orders.containsKey(orderId)) {
            return false;
        }

        OrderImpl chosen = orders.get(orderId);
        if (chosen.getStatus().contentEquals("issued")) {

            double cost = chosen.getPricePerUnit() * chosen.getQuantity();

            //Check balance
            if ((this.computeBalance() - cost) < 0) {
                return false;
            }

            //Create new balanceOperation
            BalanceOperationImpl newOp = new BalanceOperationImpl(-cost, "order");

            //Update order
            chosen.setBalanceId(newOp.getBalanceId());
            chosen.setStatus("payed");
            // TODO save in memory and check the result of the operation
            return true;
        } else return chosen.getStatus().contentEquals("payed");
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {

        //Check access rights
        if (loggedInUser == null ||
                (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))) {
            throw new UnauthorizedException();
        }

        //Check id correctness
        if (orderId == null || orderId <= 0) {
            throw new InvalidOrderIdException();
        }

        //Check presence of the specified order
        if (!orders.containsKey(orderId)) {
            return false;
        }

        OrderImpl chosen = orders.get(orderId);

        //Check location of product
        if (chosen.getProduct().getLocation() == null) {
            throw new InvalidLocationException();
        }

        if (chosen.getStatus().contentEquals("payed")) {
            //Update quantity in inventory
            chosen.getProduct().setQuantity(chosen.getProduct().getQuantity() + chosen.getQuantity());
            chosen.setStatus("completed");
            // TODO save in memory and check the result of the operation
            return true;
        } else return chosen.getStatus().contentEquals("completed");
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {

        //Check access rights
        if (loggedInUser == null ||
                (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))) {
            throw new UnauthorizedException();
        }

        return new ArrayList<>(orders.values());
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        //Check customer name correctness
        if (customerName.isEmpty() || customerName == null) {
            throw new InvalidCustomerNameException();
        }

        //Check if customer name already exists
        if (customers.values().stream().anyMatch(u -> u.getCustomerName().contentEquals(customerName))) {
            return -1;
        }

        CustomerImpl newOne = new CustomerImpl(customerName);
        customers.put(newOne.getId(), newOne);
        // TODO save in memory and check the result of the operation
        return newOne.getId();
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        //Check customer name correctness
        if (newCustomerName.isEmpty() || newCustomerName == null) {
            throw new InvalidCustomerNameException();
        }

        //Check id correctness
        if (id <= 0 || id == null) {
            throw new InvalidCustomerIdException();
        }

        //Check card correctness
        if (!newCustomerCard.matches("^[0-9]{10}$") && !newCustomerCard.isEmpty() && newCustomerCard != null) {
            throw new InvalidCustomerCardException();
        }

        if (customers.containsKey(id)) {
            CustomerImpl chosen = customers.get(id);

            if (!chosen.getCustomerName().contentEquals(newCustomerName)) {
                //The new customer name differs from the original one thus we need to check if it is still unique.
                if (customers.values().stream().anyMatch(p -> p.getCustomerName().contentEquals(newCustomerName)))
                    return false;
            }

            chosen.setCustomerName(newCustomerName);

            if (newCustomerCard.matches("^[0-9]{10}$")) {
                if (cards.containsKey(newCustomerCard) && cards.get(newCustomerCard).getCustomer() == null) {
                    //Double reference
                    chosen.setCard(cards.get(newCustomerCard));
                    cards.get(newCustomerCard).setCustomer(chosen);
                }
            } else if (newCustomerCard.isEmpty()) {
                //Double reference
                chosen.getCard().setCustomer(null);
                chosen.setCustomerCard(null);
            }//null case does not modify anything

            // TODO save in memory and check the result of the operation
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        //Check id correctness
        if (id <= 0 || id == null) {
            throw new InvalidCustomerIdException();
        }

        if (customers.containsKey(id)) {
            //Remove reference to loyalty card
            if (customers.get(id).getCard() != null) {
                customers.get(id).getCard().setCustomer(null);
            }
            customers.remove(id);
            // TODO save in memory and check the result of the operation
            return true;
        }

        return false;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        //Check id correctness
        if (id <= 0 || id == null) {
            throw new InvalidCustomerIdException();
        }

        if (customers.containsKey(id)) {
            return customers.get(id);
        }

        return null;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        return new ArrayList<>(customers.values());
    }

    @Override
    public String createCard() throws UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        LoyaltyCard newOne = new LoyaltyCard();
        cards.put(newOne.getCardId(), newOne);
        // TODO save in memory and check the result of the operation
        return newOne.getCardId();
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        //Check id correctness
        if (customerId <= 0 || customerId == null) {
            throw new InvalidCustomerIdException();
        }

        //Check card correctness
        if (!customerCard.matches("^[0-9]{10}$") && !customerCard.isEmpty() && customerCard != null) {
            throw new InvalidCustomerCardException();
        }

        if (customers.containsKey(customerId)
                && cards.containsKey(customerCard)
                && cards.get(customerCard).getCustomer() == null) {

            //Customer has already a card, detach it
            if (customers.get(customerId).getCard() != null) {
                customers.get(customerId).getCard().setCustomer(null);
            }

            //Double reference
            customers.get(customerId).setCard(cards.get(customerCard));
            cards.get(customerCard).setCustomer(customers.get(customerId));
            // TODO save in memory and check the result of the operation
            return true;
        }
        return false;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        //Check card correctness
        if (!customerCard.matches("^[0-9]{10}$") && !customerCard.isEmpty() && customerCard != null) {
            throw new InvalidCustomerCardException();
        }

        if (cards.containsKey(customerCard)){
            if ((cards.get(customerCard).getPoints() + pointsToBeAdded) < 0){
                return false;
            }
            cards.get(customerCard).setPoints(cards.get(customerCard).getPoints()+pointsToBeAdded);
            // TODO save in memory and check the result of the operation
            return true;
        }
        return false;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        SaleTransactionImpl newSale = new SaleTransactionImpl();
        sales.put(newSale.getTicketNumber(), newSale);

        // store changes in persistent memory
        if (!FileWrite.writeSales("sales.json", sales)) {
        	// error: restore previous state 
        	sales.remove(newSale.getTicketNumber());
        	return -1;
        }
        else {
        	return newSale.getTicketNumber();
        }

    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        // Check transactionId
        if (transactionId == null || transactionId <= 0) {
            throw new InvalidTransactionIdException();
        }

        // Check productCode
        if (productCode == null || productCode.isEmpty() || !ProductTypeImpl.checkBarCode(productCode)) {
            throw new InvalidProductCodeException();
        }

        // Check amount
        if (amount < 0) {
            throw new InvalidQuantityException();
        }

        // Check if the product exists and the quantity is enough
        ProductType prod = getProductTypeByBarCode(productCode);
        if (prod == null || prod.getQuantity() < amount) {
            return false;
        }

        // Check if the transactionId identifies a started transaction
        if (!sales.containsKey(transactionId)) {
            return false;
        }
        SaleTransactionImpl sale = sales.get(transactionId);

        // Check if the transactionId identifies an open transaction
        if (!sale.getState().contentEquals("OPEN")) {
            return false;
        }

        // Add the product to the sale transaction and decrease the amount of product available on the shelves
        // If this operation goes wrong, return false
        if (!sale.addEntry(new TicketEntryImpl(prod, amount))) {
            return false;
        }
        prod.setQuantity(prod.getQuantity() - amount);

        // Store changes in persistent memory
        if (!FileWrite.writeSales("sales.json", sales) || !FileWrite.writeProducts("products.json", products)) {
        	// error: restore previous state
        	prod.setQuantity(prod.getQuantity() + amount);
        	sale.deleteEntry(prod.getBarCode());
        	return false;
        }
        else {
        	return true;
        }
    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        // Check transactionId
        if (transactionId == null || transactionId <= 0) {
            throw new InvalidTransactionIdException();
        }

        // Check productCode
        if (productCode == null || productCode.isEmpty() || !ProductTypeImpl.checkBarCode(productCode)) {
            throw new InvalidProductCodeException();
        }

        // Check amount
        if (amount < 0) {
            throw new InvalidQuantityException();
        }

        // Check if the product exists
        ProductType prod = getProductTypeByBarCode(productCode);
        if (prod == null) {
            return false;
        }

        // Check if the transactionId identifies a started transaction
        if (!sales.containsKey(transactionId)) {
            return false;
        }
        SaleTransactionImpl sale = sales.get(transactionId);

        // Check if the transactionId identifies an open transaction
        if (!sale.getState().contentEquals("OPEN")) {
            return false;
        }

        // Delete the product from the sale transaction and increase the amount of product available on the shelves
        // If this operation goes wrong, return false
        TicketEntry eliminated = sale.deleteEntry(productCode);
        if (eliminated == null) {
            return false;
        }
        prod.setQuantity(prod.getQuantity() + amount);

        // Store changes in persistent memory
        if (!FileWrite.writeSales("sales.json", sales) || !FileWrite.writeProducts("products.json", products)) {
        	// Error: restore previous state
        	sale.addEntry(eliminated);
        	prod.setQuantity(prod.getQuantity() - amount);
        	return false;
        }
        else {
        	return true;
        }
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        // Check transactionId
        if (transactionId == null || transactionId <= 0) {
            throw new InvalidTransactionIdException();
        }

        // Check productCode
        if (productCode == null || productCode.isEmpty() || !ProductTypeImpl.checkBarCode(productCode)) {
            throw new InvalidProductCodeException();
        }

        // Check discountRate
        if (discountRate < 0.0 || discountRate >= 1.0) {
            throw new InvalidDiscountRateException();
        }

        // Check if the product exists
        ProductType prod = getProductTypeByBarCode(productCode);
        if (prod == null) {
            return false;
        }

        // Check if the transactionId identifies a started transaction
        if (!sales.containsKey(transactionId)) {
            return false;
        }
        SaleTransactionImpl sale = sales.get(transactionId);

        // Check if the transactionId identifies an open transaction
        if (!sale.getState().contentEquals("OPEN")) {
            return false;
        }

        double previousDiscountRate = 0.0;
        TicketEntry target = null;

        // Check the product and apply the discountRate
        for (TicketEntry entry : sale.getEntries()) {
        	if (entry.getBarCode().contentEquals(productCode)) {
        		previousDiscountRate = entry.getDiscountRate();
        		target = entry;
        		entry.setDiscountRate(discountRate);
        	}
        }
        // Product not found in the transaction
        if (target == null) {
        	return false;
        }

        // Store changes in persistent memory
        if (!FileWrite.writeSales("sales.json", sales)) {
        	// Error : restore previous state
        	target.setDiscountRate(previousDiscountRate);
        	return false;
        }
        else {
        	return true;
        }
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        // Check transactionId
        if (transactionId == null || transactionId <= 0) {
            throw new InvalidTransactionIdException();
        }

        // Check discountRate
        if (discountRate < 0.0 || discountRate >= 1.0) {
            throw new InvalidDiscountRateException();
        }


        // Check if the transactionId identifies a started transaction
        if (!sales.containsKey(transactionId)) {
            return false;
        }
        SaleTransactionImpl sale = sales.get(transactionId);

        // Check if the transactionId identifies an open or closed but not payed transaction
        if (!sale.getState().contentEquals("OPEN") && !sale.getState().contentEquals("CLOSED")) {
            return false;
        }

        double previousDiscountRate = sale.getDiscountRate();
        sale.setDiscountRate(discountRate);
 
        // Store changes in persistent memory
        if (!FileWrite.writeSales("sales.json", sales)) {
        	// Error : restore previous state
        	sale.setDiscountRate(previousDiscountRate);
        	return false;
        }
        else {
        	return true;
        }
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        // Check transactionId
        if (transactionId == null || transactionId <= 0) {
            throw new InvalidTransactionIdException();
        }

        // Check if the transactionId exists
        if (!sales.containsKey(transactionId)) {
            return -1;
        }

        SaleTransactionImpl sale = sales.get(transactionId);

        // Store changes in persistent memory
        if (!FileWrite.writeSales("sales.json", sales)) {
        	return -1;
        }
        else {
        	return (int) sale.getPrice() / 10;
        }

    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        // Check transactionId
        if (transactionId == null || transactionId <= 0) {
            throw new InvalidTransactionIdException();
        }

        // Check if the transactionId exists
        if (!sales.containsKey(transactionId)) {
            return false;
        }
        SaleTransactionImpl sale = sales.get(transactionId);

        // Check if the transactionId identifies an already closed transaction
        if (sale.getState().contentEquals("CLOSED")) {
            return false;
        }

        String previousState = sale.getState();
        sale.setState("CLOSED");

        // Store changes in persistent memory
        if (!FileWrite.writeSales("sales.json", sales)) {
        	// Error: restore previous state
        	sale.setState(previousState);
        	return false;
        }
        else {
        	return true; 
        }
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {

    	// Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }

    	// Check transactionId
    	if (saleNumber == null || saleNumber <= 0 ) {
    		throw new InvalidTransactionIdException();
    	}
    		
    	// Check if the transactionId exists 
    	if (!sales.containsKey(saleNumber)) {
    		return false;
    	}
    	
    	SaleTransactionImpl sale = sales.get(saleNumber);
     	// Check if the transactionId identifies an already payed transaction
    	if (sale.getState().contentEquals("PAYED")) {
    		return false;
    	}
  
    	// Restore quantities in the inventory for every product
    	for (TicketEntry entry: sale.getEntries()) {
    		for (ProductType p : products.values()) {
    			if (entry.getBarCode().contentEquals(p.getBarCode())) {
    				p.setQuantity(p.getQuantity() + entry.getAmount());
    			}
    		}
    	}

    	// Delete the sale from the map
    	sales.remove(saleNumber);

        // Store changes in persistent memory
        if (!FileWrite.writeSales("sales.json", sales) || !FileWrite.writeProducts("products.json", products)) {
        	// Error : restore previous state
       		for (TicketEntry entry: sale.getEntries()) {
       			for (ProductType p : products.values()) {
       				if (entry.getBarCode().contentEquals(p.getBarCode())) {
       					p.setQuantity(p.getQuantity() - entry.getAmount());
       				}
       			}
       		}
        	sales.put(sale.getTicketNumber(), sale);
        	return false;
        }
        else {
        	return true; 
        }
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        // Check transactionId
        if (transactionId == null || transactionId <= 0) {
            throw new InvalidTransactionIdException();
        }

        // Check if the transactionId exists
        if (!sales.containsKey(transactionId)) {
            return null;
        }
        SaleTransactionImpl sale = sales.get(transactionId);

        // Check if the transactionId identifies a closed transaction
        if (!sale.getState().contentEquals("CLOSED")) {
            return null;
        }

        return sale;
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {

        // Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }

    	// Check saleNumber 
    	if (saleNumber == null || saleNumber <= 0 ) {
    		throw new InvalidTransactionIdException();
    	}
    		

    	// Check if the transaction with saleNumber exists 
    	if (!sales.containsKey(saleNumber)) {
    		return -1;
    	}
    	SaleTransactionImpl sale = sales.get(saleNumber);
    	
    	// Check if the saleNumber identifies a payed transaction
    	if (!sale.getState().contentEquals("PAYED")) {
    		return -1;
    	}
    	
    	ReturnTransaction newRet = new ReturnTransaction(sale);
    	returns.put(newRet.getReturnID(), newRet);
    	
    	// Store changes in persistent memory
    	if (!FileWrite.writeReturns("returns.json", returns)) {
    		// Error: restore previous state
    		returns.remove(newRet.getReturnID());
    		return -1;
    	}
    	else {
    		return newRet.getReturnID();
    	}
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {

    	// Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }

    	// Check returnId  
    	if (returnId == null || returnId <= 0 ) {
    		throw new InvalidTransactionIdException();
    	}

   		// Check productCode
    	if (productCode == null || productCode.isEmpty() || !ProductTypeImpl.checkBarCode(productCode)) {
    		throw new InvalidProductCodeException();
    	}
    	
    	// Check amount
    	if (amount <= 0) {
    		throw new InvalidQuantityException();
    	}
    	
    	// Check if the product exists 
    	ProductType prod = getProductTypeByBarCode(productCode);
    	if (prod == null) {
    		return false;
    	}
    	
    	// Check if the return transaction exists
    	if (!returns.containsKey(returnId)) {
    		return false;
    	}
    	ReturnTransaction ret = returns.get(returnId);
    	
    	// Check if the product to be returned is in the Sale Transaction
    	TicketEntry entrySold = null;
    	for (TicketEntry e : ret.getTransaction().getEntries()) {
    		if (e.getBarCode().contentEquals(productCode)) {
    			entrySold = e;
    		}
    	}
    	if (entrySold == null) {
    		return false;
    	}

    	// Check if the amount to return is higher than the one sold
    	if (amount > entrySold.getAmount()) {
    		return false;
    	}
    	
    	// Add the product in the return transaction
    	// Set its discount rate equal to the one when it was sold
    	ret.addEntry(new TicketEntryImpl(prod, amount, entrySold.getDiscountRate())); 

    	// Note: this method doesn't update the productType quantity 
        // Store changes in persistent memory
    	if (!FileWrite.writeReturns("returns.json", returns)) {
    		// Error: restore previous state
    		ret.deleteEntry(productCode);
    		return false;
    	}
    	else {
    		return true; 
    	}
    }

    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {

        // Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }

    	// Check returnId  
    	if (returnId == null || returnId <= 0 ) {
    		throw new InvalidTransactionIdException();
    	}

    	// Check if the return transaction exists
    	if (!returns.containsKey(returnId)) {
    		return false;
    	}
    	ReturnTransaction ret = returns.get(returnId);
    	
    	// Check if the return transaction is open
    	if (!ret.getState().contentEquals("OPEN")) {
    		return false;
    	}
    	
    	if (commit) {

    		// Increase the product quantity available in the shelves
    		for (TicketEntry e : ret.getProducts()) {
    			ProductTypeImpl prod = null;
    			for (ProductTypeImpl p : products.values()) {
    				if(p.getBarCode().equals(e.getBarCode())) {
    					prod = p;
    				}
    			}
    			if (prod == null) {
    				return false;
    			}
    			prod.setQuantity(prod.getQuantity() + e.getAmount());
    		}

    		// Update the sale transaction status: decrease the number of units sold by the number of returned one
    		for (TicketEntry sold : ret.getTransaction().getEntries()) {
    			for (TicketEntry returned : ret.getProducts() ) {
    				if (sold.getBarCode().contentEquals(returned.getBarCode())) {
    					sold.setAmount(sold.getAmount() - returned.getAmount());
    				}
    			}
    		}

    		// Recompute the final price of the sale transaction
    		ret.getTransaction().setPrice(ret.getTransaction().computeCost());
    	}
    	ret.setState("CLOSED");
    	ret.setCommit(commit);

        // Store changes in persistent memory
    	if (!FileWrite.writeReturns("returns.json", returns) || !FileWrite.writeProducts("products.json", products) || !FileWrite.writeSales("sales.json", sales)) {
    		// Error: restore previous state
    		if (commit) {
    			// Decrease the product quantity available in the shelves
    		for (TicketEntry e : ret.getProducts()) {
    			ProductTypeImpl prod = null;
    			for (ProductTypeImpl p : products.values()) {
    				if(p.getBarCode().equals(e.getBarCode())) {
    					prod = p;
    				}
    			}
    			if (prod == null) {
    				return false;
    			}
    			prod.setQuantity(prod.getQuantity() - e.getAmount());
    		}

    		// Update the sale transaction status: increase the number of units sold by the number of returned one
    		for (TicketEntry sold : ret.getTransaction().getEntries()) {
    			for (TicketEntry returned : ret.getProducts() ) {
    				if (sold.getBarCode().contentEquals(returned.getBarCode())) {
    					sold.setAmount(sold.getAmount() + returned.getAmount());
    				}
    			}
    		}

    		// Recompute the final price of the sale transaction
    		ret.getTransaction().setPrice(ret.getTransaction().computeCost());

    		}
    		ret.setState("OPEN");
    		ret.setCommit(false);
    		return false;
    	}
    	else {
    		return true; 
    	}

    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
 
        // Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }

    	// Check returnId  
    	if (returnId == null || returnId <= 0 ) {
    		throw new InvalidTransactionIdException();
    	}

    	// Check if the return transaction exists
    	if (!returns.containsKey(returnId)) {
    		return false;
    	}
    	ReturnTransaction ret = returns.get(returnId);
    	
    	// Check if the return transaction has been closed, if it's open or payed return false
    	if (!ret.getState().contentEquals("CLOSED")) {
    		return false;
    	}
    	
    	// Restore the quantity of product available on the shelves and the quantity of product sold in the connected sale transaction
    	if (ret.isCommit()) {
    		// Decrease the product quantity available in the shelves
    		for (TicketEntry e : ret.getProducts()) {
    			ProductTypeImpl prod = null;
    			for (ProductTypeImpl p : products.values()) {
    				if(p.getBarCode().equals(e.getBarCode())) {
    					prod = p;
    				}
    			}
    			if (prod == null) {
    				return false;
    			}
    			prod.setQuantity(prod.getQuantity() - e.getAmount());
    		}

    		// Update the sale transaction status: increase the number of units sold by the number of returned one
    		for (TicketEntry sold : ret.getTransaction().getEntries()) {
    			for (TicketEntry returned : ret.getProducts() ) {
    				if (sold.getBarCode().contentEquals(returned.getBarCode())) {
    					sold.setAmount(sold.getAmount() + returned.getAmount());
    				}
    			}
    		}

    		// Recompute the final price of the sale transaction
    		ret.getTransaction().setPrice(ret.getTransaction().computeCost());

    		}

    	// Delete the return transaction from the map
    	returns.remove(returnId);

        // Store changes in persistent memory
    	if (!FileWrite.writeReturns("returns.json", returns) || !FileWrite.writeProducts("products.json", products) || !FileWrite.writeSales("sales.json", sales)) {
    		// Error: restore previous state
    		if (ret.isCommit()) {
    		// Increase the product quantity available in the shelves
    		for (TicketEntry e : ret.getProducts()) {
    			ProductTypeImpl prod = null;
    			for (ProductTypeImpl p : products.values()) {
    				if(p.getBarCode().equals(e.getBarCode())) {
    					prod = p;
    				}
    			}
    			if (prod == null) {
    				return false;
    			}
    			prod.setQuantity(prod.getQuantity() + e.getAmount());
    		}

    		// Update the sale transaction status: decrease the number of units sold by the number of returned one
    		for (TicketEntry sold : ret.getTransaction().getEntries()) {
    			for (TicketEntry returned : ret.getProducts() ) {
    				if (sold.getBarCode().contentEquals(returned.getBarCode())) {
    					sold.setAmount(sold.getAmount() - returned.getAmount());
    				}
    			}
    		}

    		// Recompute the final price of the sale transaction
    		ret.getTransaction().setPrice(ret.getTransaction().computeCost());

    		}
    		return false;
    	}
    	else {
    		return true; 
    	}


    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {

        // Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }
 
    	// Check cash
    	if (cash <= 0) {
    		throw new InvalidPaymentException();
    	}

        // Check ticketNumber, that is the id of the sale transaction 
        if (ticketNumber == null || ticketNumber <= 0) {
            throw new InvalidTransactionIdException();
        }

        // Check if the ticketNumber identifies a sale transaction that exists
        if (!sales.containsKey(ticketNumber)) {
            return -1;
        }
        SaleTransactionImpl sale = sales.get(ticketNumber);
        
        // Check if the sale transaction is closed
        if (!sale.getState().contentEquals("CLOSED")) {
        	return -1;
        }

        double price = sale.getPrice();
        double rest = cash - price;

        if (rest < 0) {
        	return -1;
        }
        else {
        	// Update sale fields
        	sale.setPaymentType("cash");
        	sale.setState("PAYED");

        	// Create new balance operation, to record this sale in the balance
        	BalanceOperationImpl newOp = new BalanceOperationImpl(price, "sale");
        	operations.put(newOp.getBalanceId(), newOp);
        	// TODO persistence
        	return rest;
        }
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

    	// Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }
    	
    	// Check creditCard
    	if (creditCard == null || creditCard.isEmpty() || !CreditCard.checkValidity(creditCard)) {
    		throw new InvalidCreditCardException();
    	}
   
        // Check ticketNumber, that is the id of the sale transaction 
        if (ticketNumber == null || ticketNumber <= 0) {
            throw new InvalidTransactionIdException();
        }

    	// Read the list of credit cards from the file
    	List<CreditCard> creditCardsList = FileRead.readCreditCards("CreditCards.txt");
    	
    	// Search the CreditCard in the list
    	CreditCard cCard = null;
    	for (CreditCard c : creditCardsList) {
    		if (c.getNumber().contentEquals(creditCard)) {
    			cCard = c;
    		}
    	}

    	// Credit card not registered in the credit card circuit
    	if (cCard == null) {
    		return false;
    	}
    	
        // Check if the ticketNumber identifies a sale transaction that exists
        if (!sales.containsKey(ticketNumber)) {
            return false;
        }
        SaleTransactionImpl sale = sales.get(ticketNumber);
        
        // Check if the sale transaction is closed
        if (!sale.getState().contentEquals("CLOSED")) {
        	return false;
        }

        double price = sale.getPrice();

        // Check if the credit card has enough money
    	if (cCard.getBalance() < price) {
    		return false;
    	}
    	
    	// Update credit card balance
    	cCard.setBalance(cCard.getBalance() - price);

        // Update sale fields
        sale.setPaymentType("creditCard");
        sale.setState("PAYED");
        sale.setCreditCard(cCard);
        
      	// Create new balance operation, to record this sale in the balance
      	BalanceOperationImpl newOp = new BalanceOperationImpl(price, "sale");
       	operations.put(newOp.getBalanceId(), newOp);

       	// TODO persistence
       	return true;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {

    	// Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }
    	
        // Check returnId
        if (returnId == null || returnId <= 0) {
            throw new InvalidTransactionIdException();
        }

        // Check if the return transaction exists 
        if (!returns.containsKey(returnId)) {
            return -1;
        }
        ReturnTransaction ret = returns.get(returnId);
        
        // Check if the return transaction is closed
        if (!ret.getState().contentEquals("CLOSED")) {
        	return -1;
        }

        double amount = ret.getValue();

        // Update return transaction fields
        ret.setState("PAYED");

        // Create new balance operation, to record this return in the balance
       	BalanceOperationImpl newOp = new BalanceOperationImpl(amount, "return");
       	operations.put(newOp.getBalanceId(), newOp);
       	// TODO persistence
    	return amount;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

    	// Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }
    	
        // Check returnId
        if (returnId == null || returnId <= 0) {
            throw new InvalidTransactionIdException();
        }
    	
    	// Check creditCard
    	if (creditCard == null || creditCard.isEmpty() || !CreditCard.checkValidity(creditCard)) {
    		throw new InvalidCreditCardException();
    	}
 
        // Check if the return transaction exists 
        if (!returns.containsKey(returnId)) {
            return -1;
        }
        ReturnTransaction ret = returns.get(returnId);
        
        // Check if the return transaction is closed
        if (!ret.getState().contentEquals("CLOSED")) {
        	return -1;
        }

        double amount = ret.getValue();

    	// Read the list of credit cards from the file
    	List<CreditCard> creditCardsList= FileRead.readCreditCards("CreditCards.txt");
    	
    	// Search the CreditCard in the list
    	CreditCard cCard = null;
    	for (CreditCard c : creditCardsList) {
    		if (c.getNumber().contentEquals(creditCard)) {
    			cCard = c;
    		}
    	}

    	// Credit card not registered in the credit card circuit
    	if (cCard == null) {
    		return -1;
    	}
    	
    	// Update credit card balance
    	cCard.setBalance(cCard.getBalance() + amount);

        // Update return transaction fields
        ret.setState("PAYED");

        // Create new balance operation, to record this return in the balance
       	BalanceOperationImpl newOp = new BalanceOperationImpl(amount, "return");
       	operations.put(newOp.getBalanceId(), newOp);
       	// TODO persistence
    	return amount;

    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        if ((this.computeBalance() +toBeAdded ) >= 0){
            BalanceOperationImpl newOne = new BalanceOperationImpl(toBeAdded, (toBeAdded>=0)?"credit":"debit");
            operations.put(newOne.getBalanceId(), newOne);
            // TODO save in memory and check the result of the operation
            return true;
        }

        return false;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        LocalDate start = from, end = to;

        if (from == null){ start = LocalDate.MIN;}
        if (to == null){ end = LocalDate.MAX;}
        if (start.isAfter(end)){
            LocalDate temp = start;
            start = end;
            end = temp;
        }

        LocalDate finalStart = start;
        LocalDate finalEnd = end;
        return operations.values().stream().
                filter(o -> (o.getDate().isAfter(finalStart) && o.getDate().isBefore(finalEnd)) ).
                collect(Collectors.toList());

    }

    @Override
    public double computeBalance() throws UnauthorizedException {

        // Check user role
        if (loggedInUser == null
                || (!loggedInUser.getRole().contentEquals("Administrator")
                && !loggedInUser.getRole().contentEquals("Cashier")
                && !loggedInUser.getRole().contentEquals("ShopManager"))
        ) {
            throw new UnauthorizedException();
        }

        return operations.values().stream().mapToDouble(BalanceOperation::getMoney).sum();
    }
}
