package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class EZShop implements EZShopInterface {

    HashMap<Integer, User> users;
    HashMap<Integer, SaleTransactionImpl> sales;
    HashMap<Integer, ProductTypeImpl> products;
    HashMap<Integer, OrderImpl> orders;
    HashMap<Integer, BalanceOperationImpl> operations;
    User loggedInUser;

    public EZShop() {
        this.users = new HashMap<>();
        this.sales = new HashMap<>();
        this.users = new HashMap<>();
        this.products = new HashMap<>();
        this.orders = new HashMap<>();
        this.operations = new HashMap<>();
        loggedInUser = null;
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

        User newOne = new UserImpl(username, password, role);
        users.put(newOne.getId(), newOne);

        return newOne.getId();
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
            users.remove(id);
            return true;
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
            users.get(id).setRole(role);
            return true;
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
        Optional<User> optionalUser = users.values().stream().filter(u -> u.getUsername().contentEquals(username)).findFirst();

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

        if (chosen != null){
            OrderImpl newOne = new OrderImpl(chosen, quantity, pricePerUnit);
            orders.put(newOne.getOrderId(), newOne);
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

        if (chosen == null){
            return -1;
        }

        OrderImpl newOrd = new OrderImpl(chosen, quantity, pricePerUnit);
        double cost = quantity*pricePerUnit;

        //Check balance
        if ((BalanceOperationImpl.getCurrBalance(operations.values()) - cost) < 0){
            return -1;
        }

        //Create new balanceOperation
        BalanceOperationImpl newOp = new BalanceOperationImpl(-cost, "order");

        newOrd.setBalanceId(newOp.getBalanceId());
        newOrd.setStatus("payed");
        orders.put(newOrd.getOrderId(), newOrd);
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
        if (chosen.getStatus().contentEquals("issued")){

            double cost = chosen.getPricePerUnit()* chosen.getQuantity();

            //Check balance
            if ((BalanceOperationImpl.getCurrBalance(operations.values()) - cost) < 0){
                return false;
            }

            //Create new balanceOperation
            BalanceOperationImpl newOp = new BalanceOperationImpl(-cost, "order");

            //Update order
            chosen.setBalanceId(newOp.getBalanceId());
            chosen.setStatus("payed");
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
        if (chosen.getProduct().getLocation() == null){
            throw new InvalidLocationException();
        }

        if (chosen.getStatus().contentEquals("payed")){
            //Update quantity in inventory
            chosen.getProduct().setQuantity(chosen.getProduct().getQuantity() + chosen.getQuantity());
            chosen.setStatus("completed");
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
        return null;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        return false;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        return null;
    }

    @Override
    public String createCard() throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
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

        return newSale.getTicketNumber();

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
    	if (transactionId == null || transactionId <= 0 ) {
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
    	if (prod == null || prod.getQuantity() < amount ) {
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
    	return true;
   }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {

        // Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }
    	
    	// Check transactionId
    	if (transactionId == null || transactionId <= 0 ) {
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
    	if (!sale.deleteEntry(productCode)) {
    		return false;
    	}
    	prod.setQuantity(prod.getQuantity() + amount);
    	return true;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
 
        // Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }
    	
    	// Check transactionId
    	if (transactionId == null || transactionId <= 0 ) {
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
    	
    	sale.setDiscountRate(discountRate);
    	return true;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
  
        // Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }

    	// Check transactionId
    	if (transactionId == null || transactionId <= 0 ) {
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
    	
    	sale.setDiscountRate(discountRate);
    	return true;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {

        // Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }

    	// Check transactionId
    	if (transactionId == null || transactionId <= 0 ) {
    		throw new InvalidTransactionIdException();
    	}
    	
    	// Check if the transactionId exists 
    	if (!sales.containsKey(transactionId)) {
    		return -1;
    	}

    	SaleTransactionImpl sale = sales.get(transactionId);
    	return (int)sale.getPrice() / 10;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {

    	// Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }

    	// Check transactionId
    	if (transactionId == null || transactionId <= 0 ) {
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
    	
    	sale.setState("CLOSED");
    	// TODO save in memory and check the result of the operation
        return true;
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
   
    	// Delete the sale from the map
    	sales.remove(saleNumber);
    	// TODO save in memory and check the result of the operation
        return true;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
 
    	// Check user role
    	if (loggedInUser == null
    			|| (!loggedInUser.getRole().contentEquals("Administrator")
                        && !loggedInUser.getRole().contentEquals("Cashier")
                        && !loggedInUser.getRole().contentEquals("ShopManager"))
        ){
            throw new UnauthorizedException();
        }

    	// Check transactionId
    	if (transactionId == null || transactionId <= 0 ) {
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
        return null;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return false;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        return false;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        return null;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        return 0;
    }
}
