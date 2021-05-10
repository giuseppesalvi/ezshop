package it.polito.ezshop.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.polito.ezshop.model.CreditCard;
import it.polito.ezshop.model.LoyaltyCard;
import it.polito.ezshop.model.ReturnTransaction;
import it.polito.ezshop.model.UserImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileRead {
	
	public static HashMap<Integer, UserImpl> readUsers(String fileName) {

		//Create the map
		HashMap<Integer, UserImpl> users = new HashMap<>();
		JSONParser parser = new JSONParser();

		try (FileReader reader = new FileReader(fileName)) {

			//Parse the total object
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			Integer idGenerator = Integer.parseInt(jsonObject.get("idgen").toString());

			//Retrieve the array of users
			JSONArray userString = (JSONArray) jsonObject.get("users");

			//Create each user
			for (JSONObject currUser : (Iterable<JSONObject>) userString) {
				UserImpl curr = new UserImpl(currUser.get("username").toString(),
						currUser.get("password").toString(),
						currUser.get("role").toString(),
						Integer.parseInt(currUser.get("id").toString()));
				users.put(curr.getId(), curr);
			}

			//Set the id generator for the users
			UserImpl.idGen = idGenerator;

		} catch (IOException | ParseException e) {
			//return an empty map if there is some error.
			users.clear();
		}

		return users;
	}
	
	public static Map<Integer, ProductType> readProducts(String fileName) {
		return null;
	}
	
	public static Map<Integer, Order> readOrders(String fileName) {
		return null;
	}
	
	public static Map<Integer, Customer> readCustomers(String fileName) {
		return null;
	}
	
	public static Map<Integer, LoyaltyCard> readCards(String fileName) {
		return null;
	}
	
	public static Map<Integer, SaleTransaction> readSales(String fileName) {
		return null;
	}
	
	public static Map<Integer, ReturnTransaction> readReturns(String fileName) {
		return null;
	}
	
	public static Map<Integer, BalanceOperation> readOperations(String fileName) {
		return null;
	}
	
	public static List<CreditCard> readCreditCards(String fileName) {
		return null;
	}

}
