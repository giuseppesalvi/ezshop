package it.polito.ezshop.data;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import it.polito.ezshop.model.CreditCard;
import it.polito.ezshop.model.CustomerImpl;
import it.polito.ezshop.model.LoyaltyCard;
import it.polito.ezshop.model.OrderImpl;
import it.polito.ezshop.model.ProductTypeImpl;
import it.polito.ezshop.model.ReturnTransaction;
import it.polito.ezshop.model.SaleTransactionImpl;
import it.polito.ezshop.model.UserImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FileWrite {

	public static boolean writeUsers(String fileName, Map<Integer, UserImpl> users) {

		JSONObject obj = new JSONObject();

		//Write the current id generator
		obj.put("idgen", UserImpl.idGen);
		JSONArray listUser = new JSONArray();

		//Write each user
		for(Map.Entry<Integer, UserImpl> entry : users.entrySet()){
			JSONObject user = new JSONObject();
			user.put("username", entry.getValue().getUsername());
			user.put("password", entry.getValue().getPassword());
			user.put("role", entry.getValue().getRole());
			user.put("id", entry.getValue().getId());
			listUser.add(user);
		}

		obj.put("users", listUser);

		try (FileWriter file = new FileWriter(fileName)) {
			file.write(obj.toJSONString());
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public static boolean writeProducts(String fileName, Map<Integer, ProductTypeImpl> products) {
		return false;
	}

	public static boolean writeOrders(String fileName, Map<Integer, OrderImpl> orders) {
		return false;
	}

	public static boolean writeCustomers(String fileName, Map<Integer, CustomerImpl> customers) {
		return false;
	}
	
	public static boolean writeCards(String fileName, Map<Integer, LoyaltyCard> loyaltyCards) {
		return false;
	}

	public static boolean writeSales(String fileName, Map<Integer, SaleTransactionImpl> sales) {
	
		JSONObject obj = new JSONObject();

		//Write the current id generator
		obj.put("idgen", SaleTransactionImpl.idGen);
		JSONArray listSalesJSON = new JSONArray();

		//Write each user
		for(SaleTransactionImpl sale : sales.values() ){
			JSONObject saleJSON = new JSONObject();
			saleJSON.put("transactionID", sale.getTicketNumber()) ;
			saleJSON.put("globalDiscountRate", sale.getDiscountRate());
			saleJSON.put("state", sale.getState());
			saleJSON.put("date", sale.getDate().toString());
			saleJSON.put("time", sale.getTime().toString());
			saleJSON.put("cost", sale.getPrice());
			saleJSON.put("paymentType", sale.getPaymentType());
			if( sale.getCreditCard() != null) {
				saleJSON.put("creditCard", sale.getCreditCard().getNumber());
			}
			else {
				saleJSON.put("creditCard", null);
			}
			JSONArray listProductsJSON = new JSONArray();
			for (TicketEntry prod : sale.getEntries()) {
				JSONObject prodJSON = new JSONObject();
				prodJSON.put("product", prod.getBarCode());
				prodJSON.put("quantity", prod.getAmount());
				prodJSON.put("discountRate", prod.getDiscountRate());
				listProductsJSON.add(prodJSON);
			}
			saleJSON.put("products", listProductsJSON);
			listSalesJSON.add(saleJSON);
		}

		obj.put("sales", listSalesJSON);

		try (FileWriter file = new FileWriter(fileName)) {
			file.write(obj.toJSONString());
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public static boolean writeReturns(String fileName, Map<Integer, ReturnTransaction> returns) {
		return false;
	}

	public static boolean writeOperations(String fileName, Map<Integer, BalanceOperation> operations) {
		return false;
	}

	public static boolean writeCreditCards(String fileName, List<CreditCard> creditCards) {
		return false;
	}

}
