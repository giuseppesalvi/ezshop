package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.ProductTypeImpl;
import it.polito.ezshop.model.SaleTransactionImpl;
import it.polito.ezshop.model.TicketEntryImpl;
import it.polito.ezshop.model.UserImpl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class EZShop implements EZShopInterface {

    HashMap<Integer, User> users;
<<<<<<< HEAD
    HashMap<Integer, SaleTransactionImpl> sales; 
=======
>>>>>>> branch 'code-and-unit-test' of git@git-softeng.polito.it:se-2021/group-23/ezshop.git
    User loggedInUser;

    public EZShop() {
        this.users = new HashMap<Integer, User>();
<<<<<<< HEAD
        this.sales = new HashMap<Integer, SaleTransactionImpl>();
=======
        loggedInUser = null;
>>>>>>> branch 'code-and-unit-test' of git@git-softeng.polito.it:se-2021/group-23/ezshop.git
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
        if (password.isEmpty() || password == null){
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
        ){
            throw new InvalidRoleException();
        }

        User newOne = new UserImpl(username, password, role);
        users.put(newOne.getId(), newOne);

        return newOne.getId();
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

        //Check access rights
        if (loggedInUser == null || !loggedInUser.getRole().contentEquals("Administrator")){
            throw new UnauthorizedException();
        }

        //Check id correctness
        if (id == null || id <= 0){
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
        if (loggedInUser == null || !loggedInUser.getRole().contentEquals("Administrator")){
            throw new UnauthorizedException();
        }

        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

        //Check access rights
        if (loggedInUser == null || !loggedInUser.getRole().contentEquals("Administrator")){
            throw new UnauthorizedException();
        }

        //Check id correctness
        if (id == null || id <= 0){
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
        if (loggedInUser == null || !loggedInUser.getRole().contentEquals("Administrator")){
            throw new UnauthorizedException();
        }

        //Check id correctness
        if (id == null || id <= 0){
            throw new InvalidUserIdException();
        }

        //Check role correctness
        if (role.isEmpty() || role == null
                || (!role.contentEquals("Administrator")
                && !role.contentEquals("Cashier")
                && !role.contentEquals("ShopManager"))
        ){
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
        if (password.isEmpty() || password == null){
            throw new InvalidPasswordException();
        }

        //Retrieving user with that username
        Optional<User> optionalUser = users.values().stream().filter(u -> u.getUsername().contentEquals(username)).findFirst();

        //Checking if username and password match
        if (optionalUser.isPresent() && optionalUser.get().getPassword().contentEquals(password)) {
            loggedInUser = optionalUser.get();
        }else {
            loggedInUser = null;
        }

        return loggedInUser;
    }

    @Override
    public boolean logout() {

        //Check if there is a logged user
        if (loggedInUser == null){
            return false;
        }

        loggedInUser = null;
        return true;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        return null;
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        return null;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        return false;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        return false;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        return null;
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
        ){
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
    	sale.addEntry(new TicketEntryImpl(prod, amount));
    	prod.setQuantity(prod.getQuantity() - amount);
    	return true;
    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return null;
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
