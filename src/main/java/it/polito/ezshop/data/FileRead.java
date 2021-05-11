package it.polito.ezshop.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.polito.ezshop.model.BalanceOperationImpl;
import it.polito.ezshop.model.CreditCard;
import it.polito.ezshop.model.CustomerImpl;
import it.polito.ezshop.model.LoyaltyCard;
import it.polito.ezshop.model.OrderImpl;
import it.polito.ezshop.model.ProductTypeImpl;
import it.polito.ezshop.model.ReturnTransaction;
import it.polito.ezshop.model.SaleTransactionImpl;
import it.polito.ezshop.model.TicketEntryImpl;
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
	
	public static Map<Integer, ProductTypeImpl> readProducts(String fileName) {
		return null;
	}
	
	public static Map<Integer, OrderImpl> readOrders(String fileName) {
		return null;
	}
	
	public static Map<Integer, CustomerImpl> readCustomers(String fileName) {
		return null;
	}
	
	public static Map<Integer, LoyaltyCard> readCards(String fileName) {
		return null;
	}
	
	public static HashMap<Integer, SaleTransactionImpl> readSales(String fileName) {
	
		//Create the map
		HashMap<Integer, SaleTransactionImpl> sales= new HashMap<>();
		JSONParser parser = new JSONParser();

		try (FileReader reader = new FileReader(fileName)) {

			//Parse the total object
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			Integer idGen = Integer.parseInt(jsonObject.get("idGen").toString());

			//Retrieve the array of sales 
			JSONArray listSalesJSON = (JSONArray) jsonObject.get("sales");

			//Create each sale 
			for (JSONObject saleJSON : (Iterable<JSONObject>) listSalesJSON) {
				JSONArray listProductsJSON = (JSONArray) saleJSON.get("products");
				List<TicketEntry> products = new ArrayList<TicketEntry>();
				for (JSONObject productJSON : (Iterable<JSONObject>) listProductsJSON) {
					TicketEntry prod = new TicketEntryImpl(
							// create a dummy ProductType, with just the barcode
							// it will be used later to build the reference with the real ProductType
							new ProductTypeImpl( (String) productJSON.get("productBarCode"), null, null, null),
							(Integer) productJSON.get("quantity"),
							(Double) productJSON.get("discountRate")
							);
					products.add(prod);
				}
				SaleTransactionImpl sale = new SaleTransactionImpl(
						(Integer) saleJSON.get("transactionId"),
						products,
						(Double) saleJSON.get("globalDiscountRate"),
						(String) saleJSON.get("state"),
						(String) saleJSON.get("dateString"),
						(String) saleJSON.get("timeString"),
						(Double) saleJSON.get("cost"),
						(String) saleJSON.get("paymentType"),
						(String) saleJSON.get("creditCardNumber"));
				
						
				sales.put(sale.getTicketNumber(), sale);
			}

			//Set the id generator for the sales 
			SaleTransactionImpl.idGen = idGen;

		} catch (IOException | ParseException e) {
			//return an empty map if there is some error.
			sales.clear();
		}

		return sales;
	}
	
	public static HashMap<Integer, ReturnTransaction> readReturns(String fileName) {
		
		//Create the map
		HashMap<Integer, ReturnTransaction> returns= new HashMap<>();
		JSONParser parser = new JSONParser();

		try (FileReader reader = new FileReader(fileName)) {

			//Parse the total object
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			Integer idGen = Integer.parseInt(jsonObject.get("idGen").toString());

			//Retrieve the array of returns 
			JSONArray listReturnsJSON = (JSONArray) jsonObject.get("returns");

			//Create each return
			for (JSONObject retJSON: (Iterable<JSONObject>) listReturnsJSON) {
				JSONArray listProductsJSON = (JSONArray) retJSON.get("products");
				List<TicketEntry> products = new ArrayList<TicketEntry>();
				for (JSONObject productJSON : (Iterable<JSONObject>) listProductsJSON) {
					TicketEntry prod = new TicketEntryImpl(
							// create a dummy ProductType, with just the barcode
							// it will be used later to build the reference with the real ProductType
							new ProductTypeImpl( (String) productJSON.get("productBarCode"), null, null, null),
							(Integer) productJSON.get("quantity"),
							(Double) productJSON.get("discountRate")
							);
					products.add(prod);
				}
				// create dummy SaleTransactionImpl, with just transactionId
				SaleTransactionImpl saleTransaction = new SaleTransactionImpl((Integer) retJSON.get("saleTransactionID"), null, null, null, null, null, null, null, null);
				ReturnTransaction ret = new ReturnTransaction(
						(Integer) retJSON.get("returnID"),
						saleTransaction,
						products,
						(String) retJSON.get("state"),
						(boolean) retJSON.get("commit"),
						(Double) retJSON.get("value"));
						
				returns.put(ret.getReturnID(), ret);
			}

			//Set the id generator for the sales 
			ReturnTransaction.idGen = idGen;

		} catch (IOException | ParseException e) {
			//return an empty map if there is some error.
			returns.clear();
		}

		return returns;
	}
	
	public static Map<Integer, BalanceOperationImpl> readOperations(String fileName) {
		return null;
	}
	
	public static List<CreditCard> readCreditCards(String fileName) {
		return null;
	}

}
