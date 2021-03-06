package it.polito.ezshop.data;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import it.polito.ezshop.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FileWrite {

	public static boolean writeUsers(Map<Integer, UserImpl> users) {

		JSONObject obj = new JSONObject();

		// Write the current id generator
		obj.put("idgen", UserImpl.idGen);
		JSONArray listUser = new JSONArray();

		// Write each user
		for (UserImpl user : users.values()) {
			JSONObject jsonUser = new JSONObject();
			jsonUser.put("username", user.getUsername());
			jsonUser.put("password", user.getPassword());
			jsonUser.put("role", user.getRole());
			jsonUser.put("id", user.getId());
			listUser.add(jsonUser);
		}

		obj.put("users", listUser);

		try (FileWriter file = new FileWriter("db/users.json")) {
			file.write(obj.toJSONString());
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public static boolean writeProducts(Map<Integer, ProductTypeImpl> products) {

		JSONObject obj = new JSONObject();

		// Write the current id generator
		obj.put("idgen", ProductTypeImpl.idGen);
		JSONArray listUser = new JSONArray();

		// Write each user
		for (ProductTypeImpl product : products.values()) {
			JSONObject jsonProduct = new JSONObject();
			jsonProduct.put("productCode", product.getBarCode());
			jsonProduct.put("description", product.getProductDescription());
			jsonProduct.put("sellPrice", product.getPricePerUnit());
			jsonProduct.put("notes", product.getNote()); //May be null
			jsonProduct.put("quantity", product.getQuantity());
			jsonProduct.put("position", product.getLocation()); //May be null
			jsonProduct.put("id", product.getId());
			jsonProduct.put("eliminated", product.getEliminated());
			listUser.add(jsonProduct);
		}

		obj.put("products", listUser);

		try (FileWriter file = new FileWriter("db/products.json")) {
			file.write(obj.toJSONString());
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public static boolean writeOrders(Map<Integer, OrderImpl> orders) {

		JSONObject obj = new JSONObject();

		// Write the current id generator
		obj.put("idgen", OrderImpl.idGen);
		JSONArray listOrder = new JSONArray();

		// Write each order
		for (OrderImpl order : orders.values()) {
			JSONObject jsonOrder = new JSONObject();
			jsonOrder.put("productId", order.getProduct().getId());
			jsonOrder.put("quantity", order.getQuantity());
			jsonOrder.put("pricePerUnit", order.getPricePerUnit());
			jsonOrder.put("status", order.getStatus());
			jsonOrder.put("balanceId", order.getBalanceId()); //May be null
			jsonOrder.put("id", order.getOrderId());
			listOrder.add(jsonOrder);
		}

		obj.put("orders", listOrder);

		try (FileWriter file = new FileWriter("db/orders.json")) {
			file.write(obj.toJSONString());
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public static boolean writeCustomers(Map<Integer, CustomerImpl> customers) {

		JSONObject obj = new JSONObject();

		// Write the current id generator
		obj.put("idgen", CustomerImpl.idGen);
		JSONArray listCustomer = new JSONArray();

		// Write each order
		for (CustomerImpl customer : customers.values()) {
			JSONObject jsonCustomer = new JSONObject();
			jsonCustomer.put("id", customer.getId());
			jsonCustomer.put("customerName", customer.getCustomerName());
			jsonCustomer.put("cardId", (customer.getCard() == null) ? "" : customer.getCard().getCardId());
			listCustomer.add(jsonCustomer);
		}

		obj.put("customers", listCustomer);

		try (FileWriter file = new FileWriter("db/customers.json")) {
			file.write(obj.toJSONString());
		} catch (IOException e) {
			return false;
		}

		return true;

	}

	public static boolean writeCards(Map<String, LoyaltyCard> loyaltyCards) {

		JSONObject obj = new JSONObject();

		// Write the current id generator
		obj.put("idgen", LoyaltyCard.idGen);
		JSONArray listCards = new JSONArray();

		// Write each order
		for (LoyaltyCard card : loyaltyCards.values()) {
			JSONObject jsonCards = new JSONObject();
			jsonCards.put("id", card.getCardId());
			jsonCards.put("customerId", (card.getCustomer() == null) ? "-1" : card.getCustomer().getId());
			jsonCards.put("points", card.getPoints());
			listCards.add(jsonCards);
		}

		obj.put("cards", listCards);

		try (FileWriter file = new FileWriter("db/cards.json")) {
			file.write(obj.toJSONString());
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public static boolean writeSales(Map<Integer, SaleTransactionImpl> sales) {

		JSONObject obj = new JSONObject();

		// Write the current id generator
		obj.put("idGen", SaleTransactionImpl.idGen);

		JSONArray listSalesJSON = new JSONArray();

		// Write each sale
		for (SaleTransactionImpl sale : sales.values()) {
			JSONObject saleJSON = new JSONObject();
			saleJSON.put("transactionID", sale.getTicketNumber());
			saleJSON.put("globalDiscountRate", sale.getDiscountRate());
			saleJSON.put("state", sale.getState());
			saleJSON.put("dateString", sale.getDate().toString());
			saleJSON.put("timeString", sale.getTime().toString());
			saleJSON.put("cost", sale.getPrice());
			saleJSON.put("paymentType", sale.getPaymentType());
			if (sale.getCreditCard() != null) {
				saleJSON.put("creditCardNumber", sale.getCreditCard().getNumber());
			} else {
				saleJSON.put("creditCardNumber", null);
			}
			JSONArray listProductsJSON = new JSONArray();
			for (TicketEntry prod : sale.getEntries()) {
				JSONObject prodJSON = new JSONObject();
				prodJSON.put("productBarCode", prod.getBarCode());
				prodJSON.put("quantity", prod.getAmount());
				prodJSON.put("discountRate", prod.getDiscountRate());
				listProductsJSON.add(prodJSON);
			}
			JSONArray listProductRFIDJSON = new JSONArray();
			for (Product p: sale.getProductsRFID()){
				JSONObject pJSON = new JSONObject();
				pJSON.put("RFID", p.getRFID());
				pJSON.put("productID", p.getProductType().getId());
				listProductRFIDJSON.add(pJSON);
			}
			saleJSON.put("products", listProductsJSON);
			saleJSON.put("productsRFID", listProductRFIDJSON);
			listSalesJSON.add(saleJSON);
		}

		obj.put("sales", listSalesJSON);

		try (FileWriter file = new FileWriter("db/sales.json")) {
			file.write(obj.toJSONString());
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public static boolean writeReturns(Map<Integer, ReturnTransaction> returns) {

		JSONObject obj = new JSONObject();

		// Write the current id generator
		obj.put("idGen", ReturnTransaction.idGen);

		JSONArray listReturnsJSON = new JSONArray();

		// Write each return transaction
		for (ReturnTransaction ret : returns.values()) {
			JSONObject retJSON = new JSONObject();
			retJSON.put("returnID", ret.getReturnID());
			retJSON.put("saleTransactionID", ret.getTransaction().getTicketNumber());
			retJSON.put("state", ret.getState());
			retJSON.put("commit", ret.isCommit());
			retJSON.put("value", ret.getValue());
			JSONArray listProductsJSON = new JSONArray();
			for (TicketEntry prod : ret.getProducts()) {
				JSONObject prodJSON = new JSONObject();
				prodJSON.put("productBarCode", prod.getBarCode());
				prodJSON.put("quantity", prod.getAmount());
				prodJSON.put("discountRate", prod.getDiscountRate());
				listProductsJSON.add(prodJSON);
			}
			JSONArray listProductRFIDJSON = new JSONArray();
			for (Product p: ret.getProductsRFID()){
				JSONObject pJSON = new JSONObject();
				pJSON.put("RFID", p.getRFID());
				pJSON.put("productID", p.getProductType().getId());
				listProductRFIDJSON.add(pJSON);
			}
			retJSON.put("products", listProductsJSON);
			retJSON.put("productsRFID", listProductRFIDJSON);
			listReturnsJSON.add(retJSON);
		}

		obj.put("returns", listReturnsJSON);

		try (FileWriter file = new FileWriter("db/returns.json")) {
			file.write(obj.toJSONString());
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public static boolean writeOperations(Map<Integer, BalanceOperationImpl> operations) {
		JSONObject obj = new JSONObject();

		// Write the current id generator
		obj.put("idGen", BalanceOperationImpl.idGen);

		JSONArray listOperationsJSON = new JSONArray();

		// Write each sale
		for (BalanceOperationImpl op : operations.values()) {
			JSONObject operationJSON = new JSONObject();
			operationJSON.put("id", op.getBalanceId());
			operationJSON.put("dateString", op.getDate().toString());
			operationJSON.put("amount", op.getMoney());
			operationJSON.put("type", op.getType());
			listOperationsJSON.add(operationJSON);
		}

		obj.put("operations", listOperationsJSON);

		try (FileWriter file = new FileWriter("db/operations.json")) {
			file.write(obj.toJSONString());
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static boolean writeCreditCards(List<CreditCard> creditCards) {
		try (FileWriter file = new FileWriter("creditcards.txt")) {
			file.write("#Do not delete the lines preceded by an \"#\" and do not modify the first three credit cards\n");
			file.write("#since they will be used in the acceptance tests.\n");
			file.write("#The lines preceded by an \"#\" must be ignored.\n");
			file.write("#Here you can add all the credit card numbers you need with their balance. The format MUST be :\n");
			file.write("#<creditCardNumber>;<balance>\n");
			for(CreditCard c : creditCards) {
				file.write(c.getNumber().concat(";").concat(c.getBalance().toString()).concat("\n"));
			}
		} catch (IOException e) {
			// return null if there is some error.
			return false;
		}
		return true;
	}

	public static boolean writeProductsRFID(Map<String, Product> productsRFID){
		JSONObject obj = new JSONObject();
		JSONArray listProductsRFID = new JSONArray();
		for (Product p : productsRFID.values()) {
			JSONObject jsonProductRFID = new JSONObject();
			jsonProductRFID.put("productId", p.getProductType().getId());
			jsonProductRFID.put("RFID",p.getRFID());
			listProductsRFID.add(jsonProductRFID);
		}
		obj.put("productsRFID", listProductsRFID);

		try (FileWriter file = new FileWriter("db/productsRFID.json")) {
			file.write(obj.toJSONString());
		} catch (IOException e) {
			return false;
		}

		return true;
	}
}
