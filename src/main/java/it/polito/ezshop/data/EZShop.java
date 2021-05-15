package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EZShop implements EZShopInterface {

	public EZShop() {
		EZShopMaps.loadMaps();
	}

	@Override
	public void reset() {
		EZShopMaps.eraseMaps();
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
		if (EZShopMaps.users.values().stream().anyMatch(u -> u.getUsername().contentEquals(username))) {
			return -1;
		}

		// Check role correctness
		if (role == null || role.isEmpty() ||
				(!role.contentEquals("Administrator") &&
						!role.contentEquals("Cashier") &&
						!role.contentEquals("ShopManager"))) {
			throw new InvalidRoleException();
		}

		UserImpl newOne = new UserImpl(username, password, role);
		EZShopMaps.users.put(newOne.getId(), newOne);
		if (FileWrite.writeUsers(EZShopMaps.users)) {
			return newOne.getId();
		}

		return -1;
	}

	@Override
	public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || !EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (id == null || id <= 0) {
			throw new InvalidUserIdException();
		}

		// Remove if present
		if (EZShopMaps.users.containsKey(id)) {
			// UserImpl eliminated = EZShopMaps.users.get(id);
			EZShopMaps.users.remove(id);
			return FileWrite.writeUsers(EZShopMaps.users);
		}

		return false;
	}

	@Override
	public List<User> getAllUsers() throws UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || !EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")) {
			throw new UnauthorizedException();
		}

		return new ArrayList<>(EZShopMaps.users.values());
	}

	@Override
	public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || !EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (id == null || id <= 0) {
			throw new InvalidUserIdException();
		}

		// Return it if present
		if (EZShopMaps.users.containsKey(id)) {
			return EZShopMaps.users.get(id);
		}

		return null;
	}

	@Override
	public boolean updateUserRights(Integer id, String role)
			throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || !EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (id == null || id <= 0) {
			throw new InvalidUserIdException();
		}

		// Check role correctness
		if (role == null || role.isEmpty() || (!role.contentEquals("Administrator") && !role.contentEquals("Cashier")
				&& !role.contentEquals("ShopManager"))) {
			throw new InvalidRoleException();
		}

		// Set if present
		if (EZShopMaps.users.containsKey(id)) {

			EZShopMaps.users.get(id).setRole(role);

			return FileWrite.writeUsers(EZShopMaps.users);
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
		Optional<UserImpl> optionalUser = EZShopMaps.users.values().stream().filter(u -> u.getUsername().contentEquals(username))
				.findFirst();

		// Checking if username and password match
		if (optionalUser.isPresent() && optionalUser.get().getPassword().contentEquals(password)) {
			EZShopMaps.loggedInUser = optionalUser.get();
		} else {
			EZShopMaps.loggedInUser = null;
		}

		return EZShopMaps.loggedInUser;
	}

	@Override
	public boolean logout() {

		// Check if there is a logged user
		if (EZShopMaps.loggedInUser == null) {
			return false;
		}

		EZShopMaps.loggedInUser = null;
		return true;
	}

	@Override
	public Integer createProductType(String description, String productCode, double pricePerUnit, String note)
			throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
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
		if (EZShopMaps.products.values().stream().anyMatch(p -> p.getBarCode().contentEquals(productCode))) {
			return -1;
		}

		ProductTypeImpl newOne = new ProductTypeImpl(productCode, description, pricePerUnit, note);
		EZShopMaps.products.put(newOne.getId(), newOne);
		if (FileWrite.writeProducts(EZShopMaps.products)) {
			return newOne.getId();
		}

		return -1;

	}

	@Override
	public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
			throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
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
		if (EZShopMaps.products.containsKey(id)) {
			ProductTypeImpl chosen = EZShopMaps.products.get(id);
			if (!chosen.getBarCode().contentEquals(newCode)) {
				// The new barcode differs from the original one thus we need to check if it is
				// still unique.
				if (EZShopMaps.products.values().stream().anyMatch(p -> p.getBarCode().contentEquals(newCode)))
					return false;
			}
			chosen.setBarCode(newCode);
			chosen.setNote(newNote);
			chosen.setProductDescription(newDescription);
			chosen.setPricePerUnit(newPrice);
			return FileWrite.writeProducts(EZShopMaps.products);
		}

		return false;
	}

	@Override
	public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (id == null || id <= 0) {
			throw new InvalidProductIdException();
		}

		if (EZShopMaps.products.containsKey(id)) {
			EZShopMaps.products.remove(id);
			return FileWrite.writeProducts(EZShopMaps.products);
		}

		return false;
	}

	@Override
	public List<ProductType> getAllProductTypes() throws UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null) {
			throw new UnauthorizedException();
		}

		return new ArrayList<>(EZShopMaps.products.values());

	}

	@Override
	public ProductType getProductTypeByBarCode(String barCode)
			throws InvalidProductCodeException, UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check productCode correctness
		if (barCode == null || barCode.isEmpty() || !ProductTypeImpl.checkBarCode(barCode)) {
			throw new InvalidProductCodeException();
		}

		// Retrieve the correct one
		Optional<ProductTypeImpl> productOptional = EZShopMaps.products.values().stream()
				.filter(p -> p.getBarCode().contentEquals(barCode)).findFirst();

		return productOptional.orElse(null);

	}

	@Override
	public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// null description treated as empty string
		if (description == null)
			description = "";

		String finalVal = description;
		return EZShopMaps.products.values().stream().filter(p -> p.getProductDescription().contains(finalVal))
				.collect(Collectors.toList());
	}

	@Override
	public boolean updateQuantity(Integer productId, int toBeAdded)
			throws InvalidProductIdException, UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (productId == null || productId <= 0) {
			throw new InvalidProductIdException();
		}

		if (EZShopMaps.products.containsKey(productId)) {
			ProductTypeImpl chosen = EZShopMaps.products.get(productId);
			if ((chosen.getQuantity() + toBeAdded) < 0 || chosen.getLocation() == null) {
				return false;
			}
			chosen.setQuantity(chosen.getQuantity() + toBeAdded);
			return FileWrite.writeProducts(EZShopMaps.products);
		}

		return false;
	}

	@Override
	public boolean updatePosition(Integer productId, String newPos)
			throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (productId == null || productId <= 0) {
			throw new InvalidProductIdException();
		}

		// Check newPos correctness
		if (newPos == null || (!newPos.matches("\\w+-\\w+-\\w+") && !newPos.isEmpty())) {
			throw new InvalidLocationException();
		}

		if (EZShopMaps.products.containsKey(productId)) {
			ProductTypeImpl chosen = EZShopMaps.products.get(productId);
			if (!chosen.getLocation().contentEquals(newPos)) {
				// The new location differs from the original one thus we need to check if it is
				// still unique.
				if (EZShopMaps.products.values().stream().anyMatch(p -> p.getLocation().contentEquals(newPos)))
					return false;
			}
			chosen.setLocation(newPos);
			return FileWrite.writeProducts(EZShopMaps.products);
		}

		return false;

	}

	@Override
	public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException,
			InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
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

		// Check correctness of barcode and retrieve the correct one if exists
		ProductType chosen = this.getProductTypeByBarCode(productCode);

		if (chosen != null) {
			OrderImpl newOne = new OrderImpl(chosen, quantity, pricePerUnit);
			EZShopMaps.orders.put(newOne.getOrderId(), newOne);
			if (FileWrite.writeOrders(EZShopMaps.orders)) {
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
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
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

		// Check correctness of barcode and retrieve the correct one if exists
		ProductType chosen = this.getProductTypeByBarCode(productCode);

		if (chosen == null) {
			return -1;
		}

		OrderImpl newOrd = new OrderImpl(chosen, quantity, pricePerUnit);
		double cost = quantity * pricePerUnit;

		// Check balance
		if ((this.computeBalance() - cost) < 0) {
			return -1;
		}

		// Create new balanceOperation
		BalanceOperationImpl newOp = new BalanceOperationImpl(-1 * cost, "order");
		EZShopMaps.operations.put(newOp.getBalanceId(), newOp);

		newOrd.setBalanceId(newOp.getBalanceId());
		newOrd.setStatus("PAYED");
		EZShopMaps.orders.put(newOrd.getOrderId(), newOrd);
		if (FileWrite.writeOrders(EZShopMaps.orders) &&
				FileWrite.writeOperations(EZShopMaps.operations)) {
			return newOrd.getOrderId();
		}
		return -1;
	}

	@Override
	public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (orderId == null || orderId <= 0) {
			throw new InvalidOrderIdException();
		}

		// Check presence of the specified order
		if (!EZShopMaps.orders.containsKey(orderId)) {
			return false;
		}

		OrderImpl chosen = EZShopMaps.orders.get(orderId);
		if (chosen.getStatus().contentEquals("ISSUED")) {

			double cost = chosen.getPricePerUnit() * chosen.getQuantity();

			// Check balance
			if ((this.computeBalance() - cost) < 0) {
				return false;
			}

			// Create new balanceOperation
			BalanceOperationImpl newOp = new BalanceOperationImpl(-1 * cost, "order");
			EZShopMaps.operations.put(newOp.getBalanceId(), newOp);

			// Update order
			chosen.setBalanceId(newOp.getBalanceId());
			chosen.setStatus("PAYED");
			return FileWrite.writeOrders(EZShopMaps.orders)
					&& FileWrite.writeOperations(EZShopMaps.operations);
		} else
			return chosen.getStatus().contentEquals("PAYED");
	}

	@Override
	public boolean recordOrderArrival(Integer orderId)
			throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (orderId == null || orderId <= 0) {
			throw new InvalidOrderIdException();
		}

		// Check presence of the specified order
		if (!EZShopMaps.orders.containsKey(orderId)) {
			return false;
		}

		OrderImpl chosen = EZShopMaps.orders.get(orderId);

		// Check location of product
		if (chosen.getProduct().getLocation() == null) {
			throw new InvalidLocationException();
		}

		if (chosen.getStatus().contentEquals("PAYED")) {
			// Update quantity in inventory
			chosen.getProduct().setQuantity(chosen.getProduct().getQuantity() + chosen.getQuantity());
			chosen.setStatus("COMPLETED");
			return FileWrite.writeOrders(EZShopMaps.orders) &&
					FileWrite.writeProducts(EZShopMaps.products);
		} else
			return chosen.getStatus().contentEquals("COMPLETED");
	}

	@Override
	public List<Order> getAllOrders() throws UnauthorizedException {

		// Check access rights
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		return new ArrayList<>(EZShopMaps.orders.values());
	}

	@Override
	public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check customer name correctness
		if (customerName == null || customerName.isEmpty()) {
			throw new InvalidCustomerNameException();
		}

		// Check if customer name already exists
		if (EZShopMaps.customers.values().stream().anyMatch(u -> u.getCustomerName().contentEquals(customerName))) {
			return -1;
		}

		CustomerImpl newOne = new CustomerImpl(customerName);
		EZShopMaps.customers.put(newOne.getId(), newOne);
		if (FileWrite.writeCustomers(EZShopMaps.customers))
			return newOne.getId();
		return -1;
	}

	@Override
	public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
			throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException,
			UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
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

		if (EZShopMaps.customers.containsKey(id)) {
			CustomerImpl chosen = EZShopMaps.customers.get(id);

			if (!chosen.getCustomerName().contentEquals(newCustomerName)) {
				// The new customer name differs from the original one thus we need to check if
				// it is still unique.
				if (EZShopMaps.customers.values().stream().anyMatch(p -> p.getCustomerName().contentEquals(newCustomerName)))
					return false;
			}

			chosen.setCustomerName(newCustomerName);

			if (newCustomerCard != null) {
				if (newCustomerCard.matches("^[0-9]{10}$")) {
					if (EZShopMaps.cards.containsKey(newCustomerCard) && EZShopMaps.cards.get(newCustomerCard).getCustomer() == null) {
						// Double reference
						chosen.setCard(EZShopMaps.cards.get(newCustomerCard));
						EZShopMaps.cards.get(newCustomerCard).setCustomer(chosen);
					}
				} else if (newCustomerCard.isEmpty()) {
					// Double reference
					chosen.getCard().setCustomer(null);
					chosen.setCustomerCard(null);
				} // null case does not modify anything

				return FileWrite.writeCustomers(EZShopMaps.customers)
						&& FileWrite.writeCards(EZShopMaps.cards);
			}
		}

		return false;
	}

	@Override
	public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (id == null || id <= 0) {
			throw new InvalidCustomerIdException();
		}

		if (EZShopMaps.customers.containsKey(id)) {
			// Remove reference to loyalty card
			if (EZShopMaps.customers.get(id).getCard() != null) {
				EZShopMaps.customers.get(id).getCard().setCustomer(null);
				EZShopMaps.customers.get(id).getCard().setPoints(0);
			}
			EZShopMaps.customers.remove(id);
			return FileWrite.writeCustomers(EZShopMaps.customers)
					&& FileWrite.writeCards(EZShopMaps.cards);
		}

		return false;
	}

	@Override
	public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (id == null || id <= 0) {
			throw new InvalidCustomerIdException();
		}

		if (EZShopMaps.customers.containsKey(id)) {
			return EZShopMaps.customers.get(id);
		}

		return null;
	}

	@Override
	public List<Customer> getAllCustomers() throws UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		return new ArrayList<>(EZShopMaps.customers.values());
	}

	@Override
	public String createCard() throws UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		LoyaltyCard newOne = new LoyaltyCard();
		EZShopMaps.cards.put(newOne.getCardId(), newOne);
		if (FileWrite.writeCards(EZShopMaps.cards))
			return newOne.getCardId();
		return "";
	}

	@Override
	public boolean attachCardToCustomer(String customerCard, Integer customerId)
			throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check id correctness
		if (customerId == null || customerId <= 0) {
			throw new InvalidCustomerIdException();
		}

		// Check card correctness
		if (customerCard != null && !customerCard.matches("^[0-9]{10}$") && !customerCard.isEmpty()) {
			throw new InvalidCustomerCardException();
		}

		if (EZShopMaps.customers.containsKey(customerId) && EZShopMaps.cards.containsKey(customerCard)
				&& EZShopMaps.cards.get(customerCard).getCustomer() == null) {

			// Customer has already a card, detach it
			if (EZShopMaps.customers.get(customerId).getCard() != null) {
				EZShopMaps.customers.get(customerId).getCard().setCustomer(null);
			}

			// Double reference
			EZShopMaps.customers.get(customerId).setCard(EZShopMaps.cards.get(customerCard));
			EZShopMaps.cards.get(customerCard).setCustomer(EZShopMaps.customers.get(customerId));
			return FileWrite.writeCustomers(EZShopMaps.customers)
					&& FileWrite.writeCards(EZShopMaps.cards);
		}
		return false;
	}

	@Override
	public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded)
			throws InvalidCustomerCardException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check card correctness
		if (customerCard != null && !customerCard.matches("^[0-9]{10}$") && !customerCard.isEmpty()) {
			throw new InvalidCustomerCardException();
		}

		if (EZShopMaps.cards.containsKey(customerCard)) {
			if ((EZShopMaps.cards.get(customerCard).getPoints() + pointsToBeAdded) < 0) {
				return false;
			}
			EZShopMaps.cards.get(customerCard).setPoints(EZShopMaps.cards.get(customerCard).getPoints() + pointsToBeAdded);
			return FileWrite.writeCards(EZShopMaps.cards);
		}
		return false;
	}

	@Override
	public Integer startSaleTransaction() throws UnauthorizedException {
		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		SaleTransactionImpl newSale = new SaleTransactionImpl();
		EZShopMaps.sales.put(newSale.getTicketNumber(), newSale);

		// store changes in persistent memory
		if (!FileWrite.writeSales(EZShopMaps.sales)) {
			// error: restore previous state
			// EZShopMaps.sales.remove(newSale.getTicketNumber());
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
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
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
		Optional<ProductTypeImpl> prod = EZShopMaps.products.values().stream()
				.filter(p -> p.getBarCode().contentEquals(productCode)).findFirst();

		// Check if the product exists in the map
		if (!prod.isPresent() || prod.get().getQuantity() < amount) {
			return false;
		}

		// Check if the transactionId identifies a started transaction
		if (!EZShopMaps.sales.containsKey(transactionId)) {
			return false;
		}
		SaleTransactionImpl sale = EZShopMaps.sales.get(transactionId);

		// Check if the transactionId identifies an open transaction
		if (!sale.getState().contentEquals("OPEN")) {
			return false;
		}

		// Add the product to the sale transaction and decrease the amount of product
		// available on the shelves
		// If this operation goes wrong, return false
		if (!sale.addEntry(new TicketEntryImpl(prod.get(), amount))) {
			return false;
		}
		prod.get().setQuantity(prod.get().getQuantity() - amount);

		// Store changes in persistent memory
		if (!FileWrite.writeSales(EZShopMaps.sales) || !FileWrite.writeProducts(EZShopMaps.products)) {
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
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
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
		Optional<ProductTypeImpl> prod = EZShopMaps.products.values().stream()
				.filter(p -> p.getBarCode().contentEquals(productCode)).findFirst();

		// Check if the product exists in the map
		if (!prod.isPresent()) {
			return false;
		}

		// Check if the transactionId identifies a started transaction
		if (!EZShopMaps.sales.containsKey(transactionId)) {
			return false;
		}
		SaleTransactionImpl sale = EZShopMaps.sales.get(transactionId);

		// Check if the transactionId identifies an open transaction
		if (!sale.getState().contentEquals("OPEN")) {
			return false;
		}

		// Delete the product from the sale transaction and increase the amount of
		// product available on the shelves
		// If this operation goes wrong, return false
		TicketEntry eliminated = sale.deleteEntry(productCode);
		if (eliminated == null) {
			return false;
		}
		prod.get().setQuantity(prod.get().getQuantity() + amount);

		// Store changes in persistent memory
		if (!FileWrite.writeSales(EZShopMaps.sales) || !FileWrite.writeProducts(EZShopMaps.products)) {
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
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
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
		Optional<ProductTypeImpl> prod = EZShopMaps.products.values().stream()
				.filter(p -> p.getBarCode().contentEquals(productCode)).findFirst();

		// Check if the product exists in the map
		if (!prod.isPresent()) {
			return false;
		}

		// Check if the transactionId identifies a started transaction
		if (!EZShopMaps.sales.containsKey(transactionId)) {
			return false;
		}
		SaleTransactionImpl sale = EZShopMaps.sales.get(transactionId);

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
		if (!FileWrite.writeSales(EZShopMaps.sales)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
			throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
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
		if (!EZShopMaps.sales.containsKey(transactionId)) {
			return false;
		}
		SaleTransactionImpl sale = EZShopMaps.sales.get(transactionId);

		// Check if the transactionId identifies an open or closed but not payed
		// transaction
		if (!sale.getState().contentEquals("OPEN") && !sale.getState().contentEquals("CLOSED")) {
			return false;
		}

		sale.setDiscountRate(discountRate);

		// Store changes in persistent memory
		if (!FileWrite.writeSales(EZShopMaps.sales)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check transactionId
		if (transactionId == null || transactionId <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the transactionId exists
		if (!EZShopMaps.sales.containsKey(transactionId)) {
			return -1;
		}

		SaleTransactionImpl sale = EZShopMaps.sales.get(transactionId);

		// Store changes in persistent memory
		if (!FileWrite.writeSales(EZShopMaps.sales)) {
			return -1;
		} else {
			return (int) sale.getPrice() / 10;
		}

	}

	@Override
	public boolean endSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check transactionId
		if (transactionId == null || transactionId <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the transactionId exists
		if (!EZShopMaps.sales.containsKey(transactionId)) {
			return false;
		}
		SaleTransactionImpl sale = EZShopMaps.sales.get(transactionId);

		// Check if the transactionId identifies an already closed transaction
		if (sale.getState().contentEquals("CLOSED")) {
			return false;
		}

		sale.setState("CLOSED");

		// Store changes in persistent memory
		if (!FileWrite.writeSales(EZShopMaps.sales)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean deleteSaleTransaction(Integer saleNumber)
			throws InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check transactionId
		if (saleNumber == null || saleNumber <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the transactionId exists
		if (!EZShopMaps.sales.containsKey(saleNumber)) {
			return false;
		}

		SaleTransactionImpl sale = EZShopMaps.sales.get(saleNumber);
		// Check if the transactionId identifies an already payed transaction
		if (sale.getState().contentEquals("PAYED")) {
			return false;
		}

		// Restore quantities in the inventory for every product
		for (TicketEntry entry : sale.getEntries()) {
			for (ProductTypeImpl p : EZShopMaps.products.values()) {
				if (entry.getBarCode().contentEquals(p.getBarCode())) {
					p.setQuantity(p.getQuantity() + entry.getAmount());
				}
			}
		}

		// Delete the sale from the map
		EZShopMaps.sales.remove(saleNumber);

		// Store changes in persistent memory
		if (!FileWrite.writeSales(EZShopMaps.sales) || !FileWrite.writeProducts(EZShopMaps.products)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public SaleTransaction getSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check transactionId
		if (transactionId == null || transactionId <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the transactionId exists
		if (!EZShopMaps.sales.containsKey(transactionId)) {
			return null;
		}
		SaleTransactionImpl sale = EZShopMaps.sales.get(transactionId);

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
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check saleNumber
		if (saleNumber == null || saleNumber <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the transaction with saleNumber exists
		if (!EZShopMaps.sales.containsKey(saleNumber)) {
			return -1;
		}
		SaleTransactionImpl sale = EZShopMaps.sales.get(saleNumber);

		// Check if the saleNumber identifies a payed transaction
		if (!sale.getState().contentEquals("PAYED")) {
			return -1;
		}

		ReturnTransaction newRet = new ReturnTransaction(sale);
		EZShopMaps.returns.put(newRet.getReturnID(), newRet);

		// Store changes in persistent memory
		if (!FileWrite.writeReturns(EZShopMaps.returns)) {
			return -1;
		} else {
			return newRet.getReturnID();
		}
	}

	@Override
	public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException,
			InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
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
		Optional<ProductTypeImpl> prod = EZShopMaps.products.values().stream()
				.filter(p -> p.getBarCode().contentEquals(productCode)).findFirst();

		// Check if the product exists in the map
		if (!prod.isPresent()) {
			return false;
		}

		// Check if the return transaction exists
		if (!EZShopMaps.returns.containsKey(returnId)) {
			return false;
		}
		ReturnTransaction ret = EZShopMaps.returns.get(returnId);

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
		if (!FileWrite.writeReturns(EZShopMaps.returns)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean endReturnTransaction(Integer returnId, boolean commit)
			throws InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check returnId
		if (returnId == null || returnId <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the return transaction exists
		if (!EZShopMaps.returns.containsKey(returnId)) {
			return false;
		}
		ReturnTransaction ret = EZShopMaps.returns.get(returnId);

		// Check if the return transaction is open
		if (!ret.getState().contentEquals("OPEN")) {
			return false;
		}

		if (commit) {

			// Increase the product quantity available in the shelves
			for (TicketEntryImpl e : ret.getProducts()) {
				e.getProduct().setQuantity(e.getProduct().getQuantity() + e.getAmount());
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
		if (!FileWrite.writeReturns(EZShopMaps.returns) || !FileWrite.writeProducts(EZShopMaps.products)
				|| !FileWrite.writeSales(EZShopMaps.sales)) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean deleteReturnTransaction(Integer returnId)
			throws InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check returnId
		if (returnId == null || returnId <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the return transaction exists
		if (!EZShopMaps.returns.containsKey(returnId)) {
			return false;
		}
		ReturnTransaction ret = EZShopMaps.returns.get(returnId);

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
				for (ProductTypeImpl p : EZShopMaps.products.values()) {
					if (p.getBarCode().equals(e.getBarCode())) {
						prod = p;
					}
				}
				if (prod == null) {
					return false;
				}
				prod.setQuantity(prod.getQuantity() - e.getAmount());
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
		EZShopMaps.returns.remove(returnId);

		// Store changes in persistent memory
		if (!FileWrite.writeReturns(EZShopMaps.returns) || !FileWrite.writeProducts(EZShopMaps.products)
				|| !FileWrite.writeSales(EZShopMaps.sales)) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public double receiveCashPayment(Integer ticketNumber, double cash)
			throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
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
		if (!EZShopMaps.sales.containsKey(ticketNumber)) {
			return -1;
		}
		SaleTransactionImpl sale = EZShopMaps.sales.get(ticketNumber);

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
			BalanceOperationImpl newOp = new BalanceOperationImpl(price, "sale");
			EZShopMaps.operations.put(newOp.getBalanceId(), newOp);

			// Store changes in persistent memory
			if (!FileWrite.writeOperations(EZShopMaps.operations)
					|| !FileWrite.writeSales(EZShopMaps.sales)) {
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
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
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

		// Read the list of credit EZShopMaps.cards from the file
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
		if (!EZShopMaps.sales.containsKey(ticketNumber)) {
			return false;
		}
		SaleTransactionImpl sale = EZShopMaps.sales.get(ticketNumber);

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
		EZShopMaps.operations.put(newOp.getBalanceId(), newOp);

		// Store changes in persistent memory
		if (!FileWrite.writeOperations(EZShopMaps.operations) ||
				!FileWrite.writeSales(EZShopMaps.sales) ||
				!FileWrite.writeCreditCards(creditCardsList)) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		// Check returnId
		if (returnId == null || returnId <= 0) {
			throw new InvalidTransactionIdException();
		}

		// Check if the return transaction exists
		if (!EZShopMaps.returns.containsKey(returnId)) {
			return -1;
		}
		ReturnTransaction ret = EZShopMaps.returns.get(returnId);

		// Check if the return transaction is closed
		if (!ret.getState().contentEquals("CLOSED")) {
			return -1;
		}

		double amount = ret.getValue();

		// Update return transaction fields
		ret.setState("PAYED");

		// Create new balance operation, to record this return in the balance
		BalanceOperationImpl newOp = new BalanceOperationImpl(-amount, "return");
		EZShopMaps.operations.put(newOp.getBalanceId(), newOp);

		// Store changes in persistent memory
		if (!FileWrite.writeOperations(EZShopMaps.operations)
				|| !FileWrite.writeReturns(EZShopMaps.returns)) {
			return -1;
		} else {
			return amount;
		}

	}

	@Override
	public double returnCreditCardPayment(Integer returnId, String creditCard)
			throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
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
		if (!EZShopMaps.returns.containsKey(returnId)) {
			return -1;
		}
		ReturnTransaction ret = EZShopMaps.returns.get(returnId);

		// Check if the return transaction is closed
		if (!ret.getState().contentEquals("CLOSED")) {
			return -1;
		}

		double amount = ret.getValue();

		// Read the list of credit EZShopMaps.cards from the file
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
			return -1;
		}

		// Update credit card balance
		cCard.setBalance(cCard.getBalance() + amount);

		// Update return transaction fields
		ret.setState("PAYED");

		// Create new balance operation, to record this return in the balance
		BalanceOperationImpl newOp = new BalanceOperationImpl(-amount, "return");
		EZShopMaps.operations.put(newOp.getBalanceId(), newOp);

		// Store changes in persistent memory
		if (!FileWrite.writeOperations(EZShopMaps.operations)
				|| !FileWrite.writeReturns(EZShopMaps.returns)
				|| !FileWrite.writeCreditCards(creditCardsList)) {
			return -1;
		} else {
			return amount;
		}

	}

	@Override
	public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		if ((this.computeBalance() + toBeAdded) >= 0) {
			BalanceOperationImpl newOne = new BalanceOperationImpl(toBeAdded, (toBeAdded >= 0) ? "credit" : "debit");
			EZShopMaps.operations.put(newOne.getBalanceId(), newOne);

			// Store changes in persistent memory
			if (!FileWrite.writeOperations(EZShopMaps.operations)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
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
		return EZShopMaps.operations.values().stream()
				.filter(o -> (o.getDate().isAfter(finalStart) && o.getDate().isBefore(finalEnd)))
				.collect(Collectors.toList());

	}

	@Override
	public double computeBalance() throws UnauthorizedException {

		// Check user role
		if (EZShopMaps.loggedInUser == null || (!EZShopMaps.loggedInUser.getRole().contentEquals("Administrator")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("Cashier")
				&& !EZShopMaps.loggedInUser.getRole().contentEquals("ShopManager"))) {
			throw new UnauthorizedException();
		}

		return EZShopMaps.operations.values().stream().mapToDouble(BalanceOperation::getMoney).sum();
	}
}
