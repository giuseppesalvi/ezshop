package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EZShop implements EZShopInterface {

	private HashMap<Integer, UserImpl> users;
	private HashMap<Integer, SaleTransactionImpl> sales;
	private HashMap<Integer, ProductTypeImpl> products;
	private HashMap<Integer, OrderImpl> orders;
	private HashMap<Integer, BalanceOperationImpl> operations;
	private HashMap<Integer, ReturnTransaction> returns;
	private HashMap<Integer, CustomerImpl> customers;
	private HashMap<String, LoyaltyCard> cards;
	private User loggedInUser;

	public EZShop() {
		this.users = FileRead.readUsers();
		this.sales = FileRead.readSales();
		this.products = FileRead.readProducts();
		this.orders = FileRead.readOrders();
		this.operations = FileRead.readOperations();
		this.returns = FileRead.readReturns();
		this.customers = FileRead.readCustomers();
		this.cards = FileRead.readCards();
		this.loggedInUser = null;

		// Restore references between classes

		// Customer -> this.cards
		for (CustomerImpl cus : this.customers.values()) {
			if (cus.getCard() != null) {
				cus.setCard(this.cards.get(cus.getCard().getCardId()));
			} else {
				cus.setCard(null);
			}

		}

		// Cards -> customer
		for (LoyaltyCard loy : this.cards.values()) {
			if (loy.getCustomer() != null) {
				loy.setCustomer(this.customers.get(loy.getCustomer().getId()));
			} else {
				loy.setCustomer(null);
			}
		}

		// this.orders -> productType
		for (OrderImpl ord : this.orders.values()) {
			ord.setProduct(this.products.get(ord.getProduct().getId()));
		}

		// this.sales -> productType
		for (SaleTransactionImpl sal : this.sales.values()) {
			List<TicketEntry> newEntries = new ArrayList<>();
			for (TicketEntry ent : sal.getEntries()) {
				// Retrieve the product from the map
				Optional<ProductTypeImpl> pro = this.products.values().stream()
						.filter(p -> p.getBarCode().contentEquals(ent.getBarCode())).findFirst();

				newEntries.add(new TicketEntryImpl(pro.get(), ent.getAmount(), ent.getDiscountRate()));
			}
			sal.setEntries(newEntries);
		}

		// this.returns -> productType
		for (ReturnTransaction ret : this.returns.values()) {
			for (TicketEntryImpl ent : ret.getProducts()) {
				// Retrieve the product from the map
				Optional<ProductTypeImpl> pro = this.products.values().stream()
						.filter(p -> p.getBarCode().contentEquals(ent.getBarCode())).findFirst();
				ent.setProduct(pro.get());
			}
		}

		// this.returns -> this.sales
		for (ReturnTransaction ret : this.returns.values()) {
			ret.setTransaction(this.sales.get(ret.getTransaction().getTicketNumber()));
		}
	
	}

	@Override
	public void reset() {
		loggedInUser = null; 
		
		users.clear();
		UserImpl.idGen = 1;
		FileWrite.writeUsers(this.users);

		sales.clear();
		SaleTransactionImpl.idGen = 1;
		FileWrite.writeSales(this.sales);

		products.clear();
		ProductTypeImpl.idGen = 1;
		FileWrite.writeProducts(this.products);

		orders.clear();
		OrderImpl.idGen = 1;
		FileWrite.writeOrders(this.orders);

		operations.clear();
		BalanceOperationImpl.idGen = 1;
		FileWrite.writeOperations(this.operations);

		returns.clear();
		ReturnTransaction.idGen = 1;
		FileWrite.writeReturns(this.returns);

		customers.clear();
		CustomerImpl.idGen = 1;
		FileWrite.writeCustomers(this.customers);

		cards.clear();
		LoyaltyCard.idGen = 1;
		FileWrite.writeCards(this.cards);

		loggedInUser = null;
	}

	@Override
	public Integer createUser(String username, String password, String role)
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {

		// Check username correctness
		if (username == null || username.isEmpty()) {
			throw new InvalidUsernameException();
		}

		// Check password correctness
		if (password == null || password.isEmpty()) {
			throw new InvalidPasswordException();
		}

		// Check if username already exists
		if (this.users.values().stream().anyMatch(u -> u.getUsername().contentEquals(username))) {
			return -1;
		}

		// Check role correctness
		if (role == null ||
				role.isEmpty() ||
				(!role.contentEquals("Administrator") &&
						!role.contentEquals("Cashier") &&
						!role.contentEquals("ShopManager"))) {
			throw new InvalidRoleException();
		}

		// Create new user
		UserImpl newOne = new UserImpl(username, password, role);
		this.users.put(newOne.getId(), newOne);
		if (FileWrite.writeUsers(this.users)) {
			return newOne.getId();
		}

		return -1;
	}

	@Override
	public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null ||
				!this.loggedInUser.getRole().contentEquals("Administrator")) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (id == null || id <= 0) {
			throw new InvalidUserIdException();
		}

		// Remove if present
		if (this.users.containsKey(id)) {
			this.users.remove(id);
			return FileWrite.writeUsers(this.users);
		}

		return false;
	}

	@Override
	public List<User> getAllUsers() throws UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null ||
				!this.loggedInUser.getRole().contentEquals("Administrator")) {
			throw new UnauthorizedException();
		}

		return new ArrayList<>(this.users.values());
	}

	@Override
	public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null ||
				!this.loggedInUser.getRole().contentEquals("Administrator")) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (id == null || id <= 0) {
			throw new InvalidUserIdException();
		}

		// Return it if present
		if (this.users.containsKey(id)) {
			return this.users.get(id);
		}

		return null;
	}

	@Override
	public boolean updateUserRights(Integer id, String role)
			throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null ||
				!this.loggedInUser.getRole().contentEquals("Administrator")) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (id == null || id <= 0) {
			throw new InvalidUserIdException();
		}

		// Check role correctness
		if (role == null ||
				role.isEmpty() ||
				(!role.contentEquals("Administrator") &&
						!role.contentEquals("Cashier") &&
						!role.contentEquals("ShopManager"))) {
			throw new InvalidRoleException();
		}

		// Set if present
		if (this.users.containsKey(id)) {
			this.users.get(id).setRole(role);
			return FileWrite.writeUsers(this.users);
		}

		return false;
	}

	@Override
	public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {

		// Check username correctness
		if (username == null || username.isEmpty()) {
			throw new InvalidUsernameException();
		}

		// Check password correctness
		if (password == null || password.isEmpty()) {
			throw new InvalidPasswordException();
		}

		// Retrieving user with that username
		Optional<UserImpl> optionalUser = this.users.values().stream()
				.filter(u -> u.getUsername().contentEquals(username)).findFirst();

		// Checking if username and password match
		if (optionalUser.isPresent() && optionalUser.get().getPassword().contentEquals(password)) {
			this.loggedInUser = optionalUser.get();
		} else {
			this.loggedInUser = null;
		}

		return this.loggedInUser;
	}

	@Override
	public boolean logout() {

		// Check if there is a logged user
		if (this.loggedInUser == null) {
			return false;
		}

		this.loggedInUser = null;
		return true;
	}

	@Override
	public Integer createProductType(String description, String productCode, double pricePerUnit, String note)
			throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null ||
				(!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check description correctness
		if (description == null || description.isEmpty()) {
			throw new InvalidProductDescriptionException();
		}

		// Check productCode correctness
		if (productCode == null || productCode.isEmpty() || !ProductTypeImpl.checkBarCode(productCode)) {
			throw new InvalidProductCodeException();
		}

		// Check price correctness
		if (pricePerUnit <= 0) {
			throw new InvalidPricePerUnitException();
		}

		// Check if barcode already exists
		if (this.products.values().stream()
				.anyMatch(p -> (p.getBarCode().contentEquals(productCode) && !p.getEliminated()))) {
			return -1;
		}

		Optional<ProductTypeImpl> prevElim = this.products.values().stream()
				.filter(p-> p.getBarCode().contentEquals(productCode)).findFirst();
		ProductTypeImpl newOne;

		//Check if it is already present as eliminated
		if (prevElim.isPresent()){
			newOne = prevElim.get();
			newOne.setProductDescription(description);
			newOne.setNote(note);
			newOne.setPricePerUnit(pricePerUnit);
		}else {
			newOne = new ProductTypeImpl(productCode, description, pricePerUnit, note);
			this.products.put(newOne.getId(), newOne);
		}

		if (FileWrite.writeProducts(this.products)) {
			return newOne.getId();
		}

		return -1;

	}

	@Override
	public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
			throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null ||
				(!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check description correctness
		if (newDescription == null || newDescription.isEmpty()) {
			throw new InvalidProductDescriptionException();
		}

		// Check productCode correctness
		if (newCode == null || newCode.isEmpty() || !ProductTypeImpl.checkBarCode(newCode)) {
			throw new InvalidProductCodeException();
		}

		// Check price correctness
		if (newPrice <= 0) {
			throw new InvalidPricePerUnitException();
		}

		// Check id correctness
		if (id == null || id <= 0) {
			throw new InvalidProductIdException();
		}

		// Check if product exists
		if (this.products.containsKey(id) && !this.products.get(id).getEliminated()) {
			ProductTypeImpl chosen = this.products.get(id);
			if (!chosen.getBarCode().contentEquals(newCode)) {
				// The new barcode differs from the original one thus we need to check if it is
				// still unique.
				if (this.products.values().stream().anyMatch(p -> p.getBarCode().contentEquals(newCode)))
					return false;
			}
			chosen.setBarCode(newCode);
			chosen.setNote(newNote);
			chosen.setProductDescription(newDescription);
			chosen.setPricePerUnit(newPrice);
			return FileWrite.writeProducts(this.products);
		}

		return false;
	}

	@Override
	public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null ||
				(!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (id == null || id <= 0) {
			throw new InvalidProductIdException();
		}

		if (this.products.containsKey(id) && !this.products.get(id).getEliminated()) {
			this.products.get(id).invertEliminated();
			this.products.get(id).setQuantity(0);
			this.products.get(id).setLocation(" - - ");
			return FileWrite.writeProducts(this.products);
		}

		return false;
	}

	@Override
	public List<ProductType> getAllProductTypes() throws UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null) {
			throw new UnauthorizedException();
		}

		return this.products.values().stream()
				.filter(p -> !p.getEliminated()).collect(Collectors.toList());

	}

	@Override
	public ProductType getProductTypeByBarCode(String barCode)
			throws InvalidProductCodeException, UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null ||
				(!this.loggedInUser.getRole().contentEquals("Administrator") &&
						!this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check productCode correctness
		if (barCode == null || barCode.isEmpty() || !ProductTypeImpl.checkBarCode(barCode)) {
			throw new InvalidProductCodeException();
		}

		// Retrieve the correct one
		Optional<ProductTypeImpl> productOptional = this.products.values().stream()
				.filter(p -> (p.getBarCode().contentEquals(barCode) && !p.getEliminated()))
				.findFirst();

		return productOptional.orElse(null);

	}

	@Override
	public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null ||
				(!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// null description treated as empty string
		if (description == null)
			description = "";

		String finalVal = description;
		return this.products.values().stream()
				.filter(p -> (p.getProductDescription().contains(finalVal) && !p.getEliminated()))
				.collect(Collectors.toList());
	}

	@Override
	public boolean updateQuantity(Integer productId, int toBeAdded)
			throws InvalidProductIdException, UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null ||
				(!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (productId == null || productId <= 0) {
			throw new InvalidProductIdException();
		}

		if (this.products.containsKey(productId) && !this.products.get(productId).getEliminated()) {
			ProductTypeImpl chosen = this.products.get(productId);
			if ((chosen.getQuantity() + toBeAdded) < 0 || chosen.getLocation() == null) {
				return false;
			}
			chosen.setQuantity(chosen.getQuantity() + toBeAdded);
			return FileWrite.writeProducts(this.products);
		}

		return false;
	}

	@Override
	public boolean updatePosition(Integer productId, String newPos)
			throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null ||
				(!this.loggedInUser.getRole().contentEquals("Administrator") &&
						!this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (productId == null || productId <= 0) {
			throw new InvalidProductIdException();
		}

		// Check newPos correctness
		if (newPos == null || (!newPos.matches("\\d+-[a-zA-Z]+-\\d+") && !newPos.isEmpty())) {
			throw new InvalidLocationException();
		}

		if (this.products.containsKey(productId) && !this.products.get(productId).getEliminated()) {
			ProductTypeImpl chosen = this.products.get(productId);
			if (!chosen.getLocation().contentEquals(newPos)) {
				// The new location differs from the original one thus we need to check if it is
				// still unique.
				if (this.products.values().stream().anyMatch(p -> p.getLocation().contentEquals(newPos)))
					return false;
			}
			chosen.setLocation(newPos);
			return FileWrite.writeProducts(this.products);
		}

		return false;

	}

	@Override
	public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException,
			InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null ||
				(!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check price correctness
		if (pricePerUnit <= 0) {
			throw new InvalidPricePerUnitException();
		}

		// Check quantity correctness
		if (quantity <= 0) {
			throw new InvalidQuantityException();
		}

		// Check productCode correctness
		if (productCode == null || productCode.isEmpty() || !ProductTypeImpl.checkBarCode(productCode)) {
			throw new InvalidProductCodeException();
		}

		// Retrieve the correct one
		Optional<ProductTypeImpl> chosen = this.products.values().stream()
				.filter(p -> (p.getBarCode().contentEquals(productCode) && !p.getEliminated()))
				.findFirst();

		if (chosen.isPresent()) {
			OrderImpl newOne = new OrderImpl(chosen.get(), quantity, pricePerUnit);
			this.orders.put(newOne.getOrderId(), newOne);
			if (FileWrite.writeOrders(this.orders)) {
				return newOne.getOrderId();
			}
		}

		return -1;
	}

	@Override
	public Integer payOrderFor(String productCode, int quantity, double pricePerUnit)
			throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException,
			UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null ||
				(!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check price correctness
		if (pricePerUnit <= 0) {
			throw new InvalidPricePerUnitException();
		}

		// Check quantity correctness
		if (quantity <= 0) {
			throw new InvalidQuantityException();
		}

		// Check productCode correctness
		if (productCode == null || productCode.isEmpty() || !ProductTypeImpl.checkBarCode(productCode)) {
			throw new InvalidProductCodeException();
		}

		// Retrieve the correct one
		Optional<ProductTypeImpl> chosen = this.products.values().stream()
				.filter(p -> (p.getBarCode().contentEquals(productCode) && !p.getEliminated()))
				.findFirst();

		if (!chosen.isPresent()) {
			return -1;
		}

		OrderImpl newOrd = new OrderImpl(chosen.get(), quantity, pricePerUnit);
		double cost = quantity * pricePerUnit;

		// Check balance
		if ((this.computeBalance() - cost) < 0) {
			return -1;
		}

		// Create new balanceOperation
		BalanceOperationImpl newOp = new BalanceOperationImpl(-1 * cost, "ORDER");
		this.operations.put(newOp.getBalanceId(), newOp);

		newOrd.setBalanceId(newOp.getBalanceId());
		newOrd.setStatus("PAYED");
		this.orders.put(newOrd.getOrderId(), newOrd);
		if (FileWrite.writeOrders(this.orders) && FileWrite.writeOperations(this.operations)) {
			return newOrd.getOrderId();
		}
		return -1;
	}

	@Override
	public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null ||
				(!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (orderId == null || orderId <= 0) {
			throw new InvalidOrderIdException();
		}

		// Check presence of the specified order
		if (!this.orders.containsKey(orderId)) {
			return false;
		}

		OrderImpl chosen = this.orders.get(orderId);
		if (chosen.getStatus().contentEquals("ISSUED")) {

			double cost = chosen.getPricePerUnit() * chosen.getQuantity();

			// Check balance
			if ((this.computeBalance() - cost) < 0) {
				return false;
			}

			// Create new balanceOperation
			BalanceOperationImpl newOp = new BalanceOperationImpl(-1 * cost, "ORDER");
			this.operations.put(newOp.getBalanceId(), newOp);

			// Update order
			chosen.setBalanceId(newOp.getBalanceId());
			chosen.setStatus("PAYED");
			return FileWrite.writeOrders(this.orders) && FileWrite.writeOperations(this.operations);
		} else
			return chosen.getStatus().contentEquals("PAYED");
	}

	@Override
	public boolean recordOrderArrival(Integer orderId)
			throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {

		// Check access rights
		if (this.loggedInUser == null ||
				(!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (orderId == null || orderId <= 0) {
			throw new InvalidOrderIdException();
		}

		// Check presence of the specified order
		if (!this.orders.containsKey(orderId)) {
			return false;
		}

		OrderImpl chosen = this.orders.get(orderId);

		if (chosen.getProduct().getEliminated()){
			chosen.getProduct().invertEliminated();
		}

		// Check location of product
		if (chosen.getProduct().getLocation() == null ||
				chosen.getProduct().getLocation().contentEquals(" - - ")) {
			throw new InvalidLocationException();
		}

		if (chosen.getStatus().contentEquals("PAYED")) {
			// Update quantity in inventory
			chosen.getProduct().setQuantity(chosen.getProduct().getQuantity() + chosen.getQuantity());
			chosen.setStatus("COMPLETED");
			return FileWrite.writeOrders(this.orders) && FileWrite.writeProducts(this.products);
		} else
			return chosen.getStatus().contentEquals("COMPLETED");
	}

	@Override
	public List<Order> getAllOrders() throws UnauthorizedException {

		// Check access rights
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		return new ArrayList<>(this.orders.values());
	}

	@Override
	public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null ||
				(!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check customer name correctness
		if (customerName == null || customerName.isEmpty()) {
			throw new InvalidCustomerNameException();
		}

		// Check if customer name already exists
		if (this.customers.values().stream().anyMatch(u -> u.getCustomerName().contentEquals(customerName))) {
			return -1;
		}

		CustomerImpl newOne = new CustomerImpl(customerName);
		this.customers.put(newOne.getId(), newOne);
		if (FileWrite.writeCustomers(this.customers))
			return newOne.getId();
		return -1;
	}

	@Override
	public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
			throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException,
			UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check customer name correctness
		if (newCustomerName == null || newCustomerName.isEmpty()) {
			throw new InvalidCustomerNameException();
		}

		// Check id correctness
		if (id == null || id <= 0) {
			throw new InvalidCustomerIdException();
		}

		// Check card correctness
		if (newCustomerCard != null && !newCustomerCard.matches("^[0-9]{10}$") && !newCustomerCard.isEmpty()) {
			throw new InvalidCustomerCardException();
		}

		if (this.customers.containsKey(id)) {
			CustomerImpl chosen = this.customers.get(id);

			if (!chosen.getCustomerName().contentEquals(newCustomerName)) {
				// The new customer name differs from the original one thus we need to check if
				// it is still unique.
				if (this.customers.values().stream()
						.anyMatch(p -> p.getCustomerName().contentEquals(newCustomerName)))
					return false;
			}

			chosen.setCustomerName(newCustomerName);

			if (newCustomerCard != null) {
				if (newCustomerCard.matches("^[0-9]{10}$")) {
					if (this.cards.containsKey(newCustomerCard)
							&& this.cards.get(newCustomerCard).getCustomer() == null) {
						// Double reference
						chosen.setCard(this.cards.get(newCustomerCard));
						//this.cards.get(newCustomerCard).setCustomer(chosen);
					} else { return false;}

				} else if (newCustomerCard.isEmpty()) {
					// Double reference
					chosen.getCard().setCustomer(null);
					chosen.setCard(null);
				} // null case does not modify anything
			}

			return FileWrite.writeCustomers(this.customers) && FileWrite.writeCards(this.cards);
		}

		return false;
	}

	@Override
	public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (id == null || id <= 0) {
			throw new InvalidCustomerIdException();
		}

		if (this.customers.containsKey(id)) {
			// Remove reference to loyalty card
			if (this.customers.get(id).getCard() != null) {
				this.customers.get(id).getCard().setCustomer(null);
				this.customers.get(id).getCard().setPoints(0);
			}
			this.customers.remove(id);
			return FileWrite.writeCustomers(this.customers) && FileWrite.writeCards(this.cards);
		}

		return false;
	}

	@Override
	public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (id == null || id <= 0) {
			throw new InvalidCustomerIdException();
		}

		if (this.customers.containsKey(id)) {
			return this.customers.get(id);
		}

		return null;
	}

	@Override
	public List<Customer> getAllCustomers() throws UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		return new ArrayList<>(this.customers.values());
	}

	@Override
	public String createCard() throws UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		LoyaltyCard newOne = new LoyaltyCard();
		this.cards.put(newOne.getCardId(), newOne);
		if (FileWrite.writeCards(this.cards))
			return newOne.getCardId();
		return "";
	}

	@Override
	public boolean attachCardToCustomer(String customerCard, Integer customerId)
			throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (customerId == null || customerId <= 0) {
			throw new InvalidCustomerIdException();
		}

		// Check card correctness
		if (customerCard == null || !customerCard.matches("^[0-9]{10}$") || customerCard.isEmpty()) {
			throw new InvalidCustomerCardException();
		}

		if (this.customers.containsKey(customerId) && this.cards.containsKey(customerCard)
				&& this.cards.get(customerCard).getCustomer() == null) {

			this.customers.get(customerId).setCard(this.cards.get(customerCard));
			return FileWrite.writeCustomers(this.customers) && FileWrite.writeCards(this.cards);
		}
		return false;
	}

	@Override
	public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded)
			throws InvalidCustomerCardException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check card correctness
		if (customerCard == null || !customerCard.matches("^[0-9]{10}$") || customerCard.isEmpty()) {
			throw new InvalidCustomerCardException();
		}

		if (this.cards.containsKey(customerCard)) {
			if ((this.cards.get(customerCard).getPoints() + pointsToBeAdded) < 0) {
				return false;
			}
			this.cards.get(customerCard)
					.setPoints(this.cards.get(customerCard).getPoints() + pointsToBeAdded);
			return FileWrite.writeCards(this.cards);
		}
		return false;
	}

	@Override
	public Integer startSaleTransaction() throws UnauthorizedException {
		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		SaleTransactionImpl newSale = new SaleTransactionImpl();
		this.sales.put(newSale.getTicketNumber(), newSale);

		// store changes in persistent memory
		if (!FileWrite.writeSales(this.sales)) {
			return -1;
		} else {
			return newSale.getTicketNumber();
		}

	}

	@Override
	public boolean addProductToSale(Integer transactionId, String productCode, int amount)
			throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			UnauthorizedException {
		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
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

		// Retrieve the correct product
		Optional<ProductTypeImpl> prod = this.products.values().stream()
				.filter(p -> p.getBarCode().contentEquals(productCode)).findFirst();

		// Check if the product exists in the map and the quantity available is enough
		if (!prod.isPresent() || prod.get().getEliminated() || prod.get().getQuantity() < amount) {
			return false;
		}

		// Check if the transactionId identifies a started transaction
		if (!this.sales.containsKey(transactionId)) {
			return false;
		}
		SaleTransactionImpl sale = this.sales.get(transactionId);

		// Check if the transactionId identifies an open transaction
		if (!sale.getState().contentEquals("OPEN")) {
			return false;
		}

		// Add the product to the sale transaction and decrease the amount of product
		// available on the shelves
		// If the product is already present in the transaction update its quantity instead 
		boolean found = false;
		for (TicketEntry entry : sale.getEntries()) {
			if (entry.getBarCode().contentEquals(productCode)) {
				entry.setAmount(entry.getAmount() + amount);
				found = true;
			}

		}
		if (!found) {
			sale.addEntry(new TicketEntryImpl(prod.get(), amount));
		}
		prod.get().setQuantity(prod.get().getQuantity() - amount);

		// Store changes in persistent memory
		if (!FileWrite.writeSales(this.sales) || !FileWrite.writeProducts(this.products)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount)
			throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
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

		// Retrieve the correct product
		Optional<ProductTypeImpl> prod = this.products.values().stream()
				.filter(p -> p.getBarCode().contentEquals(productCode)).findFirst();

		// Check if the product exists in the map
		if (!prod.isPresent()) {
			return false;
		}

		// Check if the transactionId identifies a started transaction
		if (!this.sales.containsKey(transactionId)) {
			return false;
		}
		SaleTransactionImpl sale = this.sales.get(transactionId);

		// Check if the transactionId identifies an open transaction
		if (!sale.getState().contentEquals("OPEN")) {
			return false;
		}
		
		// Check if the quantity of product can satisfy the request
		boolean toDelete = false;
		boolean found = false;
		for(TicketEntry entry : sale.getEntries()) {
			if (entry.getBarCode().contentEquals(productCode)) {
				if(entry.getAmount() >= amount) {
					entry.setAmount(entry.getAmount() - amount);
					found = true;
					if(entry.getAmount() == 0) {
						toDelete = true;
					}
				}
				else {
					return false;
				}
			}
		}
		
		// Check if the product was present in the sale with enough quantity
		if (found == false) {
			return false;
		}

		// Delete the product from the sale transaction if the remaining amount = 0
		// If this operation goes wrong, return false
		if (toDelete) {
			sale.deleteEntry(productCode);
		}

		// Increase the amount of product available on the shelves
		prod.get().setQuantity(prod.get().getQuantity() + amount);
		if (prod.get().getEliminated() ) {
			prod.get().invertEliminated();
		}

		// Store changes in persistent memory
		if (!FileWrite.writeSales(this.sales) || !FileWrite.writeProducts(this.products)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
			throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException,
			UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
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

		// Retrieve the correct product
		Optional<ProductTypeImpl> prod = this.products.values().stream()
				.filter(p -> p.getBarCode().contentEquals(productCode)).findFirst();

		// Check if the product exists in the map
		if (!prod.isPresent()) {
			return false;
		}

		// Check if the transactionId identifies a started transaction
		if (!this.sales.containsKey(transactionId)) {
			return false;
		}
		SaleTransactionImpl sale = this.sales.get(transactionId);

		// Check if the transactionId identifies an open transaction
		if (!sale.getState().contentEquals("OPEN")) {
			return false;
		}

		// double previousDiscountRate = 0.0;
		TicketEntry target = null;

		// Check the product and apply the discountRate
		for (TicketEntry entry : sale.getEntries()) {
			if (entry.getBarCode().contentEquals(productCode)) {
				// previousDiscountRate = entry.getDiscountRate();
				target = entry;
				entry.setDiscountRate(discountRate);
			}
		}
		// Product not found in the transaction
		if (target == null) {
			return false;
		}

		// Store changes in persistent memory
		if (!FileWrite.writeSales(this.sales)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
			throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
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
		if (!this.sales.containsKey(transactionId)) {
			return false;
		}
		SaleTransactionImpl sale = this.sales.get(transactionId);

		// Check if the transactionId identifies an open or closed but not payed
		// transaction
		if (!sale.getState().contentEquals("OPEN") && !sale.getState().contentEquals("CLOSED")) {
			return false;
		}

		sale.setDiscountRate(discountRate);

		// Store changes in persistent memory
		if (!FileWrite.writeSales(this.sales)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check transactionId
		if (transactionId == null || transactionId <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the transactionId exists
		if (!this.sales.containsKey(transactionId)) {
			return -1;
		}

		SaleTransactionImpl sale = this.sales.get(transactionId);

		// Store changes in persistent memory
		if (!FileWrite.writeSales(this.sales)) {
			return -1;
		} else {
			return (int) sale.getPrice() / 10;
		}

	}

	@Override
	public boolean endSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check transactionId
		if (transactionId == null || transactionId <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the transactionId exists
		if (!this.sales.containsKey(transactionId)) {
			return false;
		}
		SaleTransactionImpl sale = this.sales.get(transactionId);

		// Check if the transactionId identifies an already closed or payed transaction
		if (sale.getState().contentEquals("CLOSED") || sale.getState().contentEquals("PAYED")) {
			return false;
		}

		sale.setState("CLOSED");

		// Store changes in persistent memory
		if (!FileWrite.writeSales(this.sales)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean deleteSaleTransaction(Integer saleNumber)
			throws InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check transactionId
		if (saleNumber == null || saleNumber <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the transactionId exists
		if (!this.sales.containsKey(saleNumber)) {
			return false;
		}

		SaleTransactionImpl sale = this.sales.get(saleNumber);
		// Check if the transactionId identifies an already payed transaction
		if (sale.getState().contentEquals("PAYED")) {
			return false;
		}

		// Restore quantities in the inventory for every product
		for (TicketEntry entry : sale.getEntries()) {
			for (ProductTypeImpl p : this.products.values()) {
				if (entry.getBarCode().contentEquals(p.getBarCode())) {
					p.setQuantity(p.getQuantity() + entry.getAmount());
					if (p.getEliminated() ) {
						p.invertEliminated();
					}
				}
			}
		}

		// Delete the sale from the map
		this.sales.remove(saleNumber);

		// Store changes in persistent memory
		if (!FileWrite.writeSales(this.sales) || !FileWrite.writeProducts(this.products)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public SaleTransaction getSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check transactionId
		if (transactionId == null || transactionId <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the transactionId exists
		if (!this.sales.containsKey(transactionId)) {
			return null;
		}
		SaleTransactionImpl sale = this.sales.get(transactionId);

		// Check if the transactionId is still open
		if (sale.getState().contentEquals("OPEN")) {
			return null;
		}

		return sale;
	}

	@Override
	public Integer startReturnTransaction(Integer saleNumber)
			throws /* InvalidTicketNumberException, */InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check saleNumber
		if (saleNumber == null || saleNumber <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the transaction with saleNumber exists
		if (!this.sales.containsKey(saleNumber)) {
			return -1;
		}
		SaleTransactionImpl sale = this.sales.get(saleNumber);

		// Check if the saleNumber identifies a payed transaction
		if (!sale.getState().contentEquals("PAYED")) {
			return -1;
		}

		ReturnTransaction newRet = new ReturnTransaction(sale);
		this.returns.put(newRet.getReturnID(), newRet);

		// Store changes in persistent memory
		if (!FileWrite.writeReturns(this.returns)) {
			return -1;
		} else {
			return newRet.getReturnID();
		}
	}

	@Override
	public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException,
			InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check returnId
		if (returnId == null || returnId <= 0) {
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

		// Retrieve the correct product
		Optional<ProductTypeImpl> prod = this.products.values().stream()
				.filter(p -> p.getBarCode().contentEquals(productCode)).findFirst();

		// Check if the product exists in the map
		if (!prod.isPresent()) {
			return false;
		}

		// Check if the return transaction exists
		if (!this.returns.containsKey(returnId)) {
			return false;
		}
		ReturnTransaction ret = this.returns.get(returnId);

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
		ret.addEntry(new TicketEntryImpl(prod.get(), amount, entrySold.getDiscountRate()));

		// Note: this method doesn't update the productType quantity
		// Store changes in persistent memory
		if (!FileWrite.writeReturns(this.returns)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean endReturnTransaction(Integer returnId, boolean commit)
			throws InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check returnId
		if (returnId == null || returnId <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the return transaction exists
		if (!this.returns.containsKey(returnId)) {
			return false;
		}
		ReturnTransaction ret = this.returns.get(returnId);

		// Check if the return transaction is open
		if (!ret.getState().contentEquals("OPEN")) {
			return false;
		}

		if (commit) {

			// Increase the product quantity available in the shelves
			for (TicketEntryImpl e : ret.getProducts()) {
				e.getProduct().setQuantity(e.getProduct().getQuantity() + e.getAmount());
				if (e.getProduct().getEliminated()) {
					e.getProduct().invertEliminated();
				}
			}

			// Update the sale transaction status: decrease the number of units sold by the
			// number of returned one
			for (TicketEntry sold : ret.getTransaction().getEntries()) {
				for (TicketEntry returned : ret.getProducts()) {
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
		if (!FileWrite.writeReturns(this.returns) || !FileWrite.writeProducts(this.products)
				|| !FileWrite.writeSales(this.sales)) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean deleteReturnTransaction(Integer returnId)
			throws InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check returnId
		if (returnId == null || returnId <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the return transaction exists
		if (!this.returns.containsKey(returnId)) {
			return false;
		}
		ReturnTransaction ret = this.returns.get(returnId);

		// Check if the return transaction has been closed, if it's open or payed return
		// false
		if (!ret.getState().contentEquals("CLOSED")) {
			return false;
		}

		// Restore the quantity of product available on the shelves and the quantity of
		// product sold in the connected sale transaction
		if (ret.isCommit()) {
			// Decrease the product quantity available in the shelves
			for (TicketEntry e : ret.getProducts()) {
				ProductTypeImpl prod = null;
				for (ProductTypeImpl p : this.products.values()) {
					if (p.getBarCode().equals(e.getBarCode())) {
						prod = p;
					}
				}
				if (prod == null) {
					return false;
				}
				prod.setQuantity(prod.getQuantity() - e.getAmount());
				if (prod.getQuantity() == 0) {
					prod.invertEliminated();
				}
			}

			// Update the sale transaction status: increase the number of units sold by the
			// number of returned one
			for (TicketEntry sold : ret.getTransaction().getEntries()) {
				for (TicketEntry returned : ret.getProducts()) {
					if (sold.getBarCode().contentEquals(returned.getBarCode())) {
						sold.setAmount(sold.getAmount() + returned.getAmount());
					}
				}
			}

			// Recompute the final price of the sale transaction
			ret.getTransaction().setPrice(ret.getTransaction().computeCost());

		}

		// Delete the return transaction from the map
		this.returns.remove(returnId);

		// Store changes in persistent memory
		if (!FileWrite.writeReturns(this.returns) || !FileWrite.writeProducts(this.products)
				|| !FileWrite.writeSales(this.sales)) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public double receiveCashPayment(Integer ticketNumber, double cash)
			throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
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
		if (!this.sales.containsKey(ticketNumber)) {
			return -1;
		}
		SaleTransactionImpl sale = this.sales.get(ticketNumber);

		// Check if the sale transaction is closed
		if (!sale.getState().contentEquals("CLOSED")) {
			return -1;
		}

		double price = sale.getPrice();
		double rest = cash - price;

		if (rest < 0) {
			return -1;
		} else {
			// Update sale fields
			sale.setPaymentType("cash");
			sale.setState("PAYED");

			// Create new balance operation, to record this sale in the balance
			BalanceOperationImpl newOp = new BalanceOperationImpl(price, "SALE");
			this.operations.put(newOp.getBalanceId(), newOp);

			// Store changes in persistent memory
			if (!FileWrite.writeOperations(this.operations) || !FileWrite.writeSales(this.sales)) {
				return -1;
			} else {
				return rest;
			}
		}
	}

	@Override
	public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard)
			throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
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

		// Read the list of credit this.cards from the file
		List<CreditCard> creditCardsList = FileRead.readCreditCards();

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
		if (!this.sales.containsKey(ticketNumber)) {
			return false;
		}
		SaleTransactionImpl sale = this.sales.get(ticketNumber);

		// Check if the sale transaction is closed
		if (!sale.getState().contentEquals("CLOSED")) {
			return false;
		}

		double price = sale.getPrice();

		// Check if the credit card has enough money
		if (cCard.getBalance() == null || cCard.getBalance() < price) {
			return false;
		}

		// Update credit card balance
		cCard.setBalance(cCard.getBalance() - price);

		// Update sale fields
		sale.setPaymentType("creditCard");
		sale.setState("PAYED");
		sale.setCreditCard(cCard);

		// Create new balance operation, to record this sale in the balance
		BalanceOperationImpl newOp = new BalanceOperationImpl(price, "SALE");
		this.operations.put(newOp.getBalanceId(), newOp);

		// Store changes in persistent memory
		if (!FileWrite.writeOperations(this.operations) || !FileWrite.writeSales(this.sales)
				|| !FileWrite.writeCreditCards(creditCardsList)) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check returnId
		if (returnId == null || returnId <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the return transaction exists
		if (!this.returns.containsKey(returnId)) {
			return -1;
		}
		ReturnTransaction ret = this.returns.get(returnId);

		// Check if the return transaction is closed
		if (!ret.getState().contentEquals("CLOSED")) {
			return -1;
		}

		double amount = ret.getValue();

		// Update return transaction fields
		ret.setState("PAYED");

		// Create new balance operation, to record this return in the balance
		BalanceOperationImpl newOp = new BalanceOperationImpl(-amount, "RETURN");
		this.operations.put(newOp.getBalanceId(), newOp);

		// Store changes in persistent memory
		if (!FileWrite.writeOperations(this.operations) || !FileWrite.writeReturns(this.returns)) {
			return -1;
		} else {
			return amount;
		}

	}

	@Override
	public double returnCreditCardPayment(Integer returnId, String creditCard)
			throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
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
		if (!this.returns.containsKey(returnId)) {
			return -1;
		}
		ReturnTransaction ret = this.returns.get(returnId);

		// Check if the return transaction is closed
		if (!ret.getState().contentEquals("CLOSED")) {
			return -1;
		}

		double amount = ret.getValue();

		// Read the list of credit this.cards from the file
		List<CreditCard> creditCardsList = FileRead.readCreditCards();

		// Search the CreditCard in the list
		CreditCard cCard = null;
		for (CreditCard c : creditCardsList) {
			if (c.getNumber().contentEquals(creditCard)) {
				cCard = c;
			}
		}

		// Credit card not registered in the credit card circuit
		if (cCard == null || cCard.getBalance() == null) {
			return -1;
		}

		// Update credit card balance
		cCard.setBalance(cCard.getBalance() + amount);

		// Update return transaction fields
		ret.setState("PAYED");

		// Create new balance operation, to record this return in the balance
		BalanceOperationImpl newOp = new BalanceOperationImpl(-amount, "RETURN");
		this.operations.put(newOp.getBalanceId(), newOp);

		// Store changes in persistent memory
		if (!FileWrite.writeOperations(this.operations) || !FileWrite.writeReturns(this.returns)
				|| !FileWrite.writeCreditCards(creditCardsList)) {
			return -1;
		} else {
			return amount;
		}

	}

	@Override
	public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		if ((this.computeBalance() + toBeAdded) >= 0) {
			BalanceOperationImpl newOne = new BalanceOperationImpl(toBeAdded, (toBeAdded >= 0) ? "CREDIT" : "DEBIT");
			this.operations.put(newOne.getBalanceId(), newOne);

			// Store changes in persistent memory and return result of the operation
			return FileWrite.writeOperations(this.operations);
		}
		return false;
	}

	@Override
	public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		LocalDate start = from, end = to;

		if (from == null) {
			start = LocalDate.MIN;
		}
		if (to == null) {
			end = LocalDate.MAX;
		}
		if (start.isAfter(end)) {
			LocalDate temp = start;
			start = end;
			end = temp;
		}

		LocalDate finalStart = start;
		LocalDate finalEnd = end;
		return this.operations.values().stream()
				.filter(o -> (o.getDate().isAfter(finalStart) && o.getDate().isBefore(finalEnd)))
				.collect(Collectors.toList());

	}

	@Override
	public double computeBalance() throws UnauthorizedException {

		// Check user role
		if (this.loggedInUser == null || (!this.loggedInUser.getRole().contentEquals("Administrator")
				&& !this.loggedInUser.getRole().contentEquals("Cashier")
				&& !this.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		return this.operations.values().stream().mapToDouble(BalanceOperation::getMoney).sum();
	}
}
