package it.polito.ezshop.data;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import it.polito.ezshop.model.CreditCard;
import it.polito.ezshop.model.LoyaltyCard;
import it.polito.ezshop.model.ReturnTransaction;
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

	public static boolean writeProducts(String fileName, Map<Integer, ProductType> products) {
		return false;
	}

	public static boolean writeOrders(String fileName, Map<Integer, Order> orders) {
		return false;
	}

	public static boolean writeCustomers(String fileName, Map<Integer, Customer> customers) {
		return false;
	}
	
	public static boolean writeCards(String fileName, Map<Integer, LoyaltyCard> loyaltyCards) {
		return false;
	}

	public static boolean writeSales(String fileName, Map<Integer, SaleTransaction> sales) {
		return false;
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
