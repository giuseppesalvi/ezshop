package it.polito.ezshop.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.polito.ezshop.model.*;
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
				UserImpl curr = new UserImpl(
						(String) currUser.get("username"),
						(String) currUser.get("password"),
						(String) currUser.get("role"),
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
	
	public static HashMap<Integer, ProductTypeImpl> readProducts(String fileName) {

		//Create the map
		HashMap<Integer, ProductTypeImpl> products = new HashMap<>();
		JSONParser parser = new JSONParser();

		try (FileReader reader = new FileReader(fileName)) {

			//Parse the total object
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			Integer idGenerator = Integer.parseInt(jsonObject.get("idgen").toString());

			//Retrieve the array of products
			JSONArray productString = (JSONArray) jsonObject.get("products");

			//Create each user
			for (JSONObject currProduct : (Iterable<JSONObject>) productString) {
				ProductTypeImpl curr = new ProductTypeImpl(
						(String) currProduct.get("productCode"),
						(String) currProduct.get("description"),
						Double.parseDouble(currProduct.get("sellPrice").toString()),
						(String) currProduct.get("notes"),
						Integer.parseInt(currProduct.get("quantity").toString()),
						(String) currProduct.get("position"),
						Integer.parseInt(currProduct.get("id").toString())
						);
				products.put(curr.getId(), curr);
			}

			//Set the id generator for the users
			UserImpl.idGen = idGenerator;

		} catch (IOException | ParseException e) {
			//return an empty map if there is some error.
			products.clear();
		}
		
		return products;
	}
	
	public static HashMap<Integer, OrderImpl> readOrders(String fileName) {
		//Create the map
		HashMap<Integer, OrderImpl> orders = new HashMap<>();
		JSONParser parser = new JSONParser();

		try (FileReader reader = new FileReader(fileName)) {

			//Parse the total object
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			Integer idGenerator = Integer.parseInt(jsonObject.get("idgen").toString());

			//Retrieve the array of orders
			JSONArray orderString = (JSONArray) jsonObject.get("orders");

			//Create each user
			for (JSONObject currOrder : (Iterable<JSONObject>) orderString) {
				OrderImpl curr = new OrderImpl(
						new ProductTypeImpl(
								Integer.parseInt(currOrder.get("productId").toString())
						),
						Integer.parseInt(currOrder.get("quantity").toString()),
						Double.parseDouble(currOrder.get("pricePerUnit").toString()),
						(String) currOrder.get("status"),
						Integer.parseInt(currOrder.get("balanceId").toString()),
						Integer.parseInt(currOrder.get("id").toString())
				);
				orders.put(curr.getOrderId(), curr);
			}

			//Set the id generator for the users
			UserImpl.idGen = idGenerator;

		} catch (IOException | ParseException e) {
			//return an empty map if there is some error.
			orders.clear();
		}

		return orders;
		
	}
	
	public static HashMap<Integer, CustomerImpl> readCustomers(String fileName) {

		//Create the map
		HashMap<Integer, CustomerImpl> customers = new HashMap<>();
		JSONParser parser = new JSONParser();

		try (FileReader reader = new FileReader(fileName)) {

			//Parse the total object
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			Integer idGenerator = Integer.parseInt(jsonObject.get("idgen").toString());

			//Retrieve the array of customers
			JSONArray customerString = (JSONArray) jsonObject.get("customers");

			//Create each user
			for (JSONObject currCustomer : (Iterable<JSONObject>) customerString) {
				CustomerImpl curr = new CustomerImpl(
						(String) currCustomer.get("customerName"),
						Integer.parseInt(currCustomer.get("id").toString()),
						new LoyaltyCard(
							currCustomer.get("cardId").toString()
						)
				);
				customers.put(curr.getId(), curr);
			}

			//Set the id generator for the users
			UserImpl.idGen = idGenerator;

		} catch (IOException | ParseException e) {
			//return an empty map if there is some error.
			customers.clear();
		}

		return customers;
	}
	
	public static HashMap<String, LoyaltyCard> readCards(String fileName) {

		//Create the map
		HashMap<String, LoyaltyCard> cards = new HashMap<>();
		JSONParser parser = new JSONParser();

		try (FileReader reader = new FileReader(fileName)) {

			//Parse the total object
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			Integer idGenerator = Integer.parseInt(jsonObject.get("idgen").toString());

			//Retrieve the array of cards
			JSONArray cardString = (JSONArray) jsonObject.get("cards");

			//Create each user
			for (JSONObject currCard : (Iterable<JSONObject>) cardString) {
				LoyaltyCard curr = new LoyaltyCard(
						(String) currCard.get("id"),
						new CustomerImpl(
							Integer.parseInt(currCard.get("customerId").toString())
						),
						Integer.parseInt(currCard.get("points").toString())
				);
				cards.put(curr.getCardId(), curr);
			}

			//Set the id generator for the users
			UserImpl.idGen = idGenerator;

		} catch (IOException | ParseException e) {
			//return an empty map if there is some error.
			cards.clear();
		}

		return cards;
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
	
	public static HashMap<Integer, BalanceOperationImpl> readOperations(String fileName) {

		//Create the map
		HashMap<Integer, BalanceOperationImpl> operations= new HashMap<>();
		JSONParser parser = new JSONParser();

		try (FileReader reader = new FileReader(fileName)) {

			//Parse the total object
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			Integer idGen = Integer.parseInt(jsonObject.get("idGen").toString());

			//Retrieve the array of operations 
			JSONArray listOperationsJSON = (JSONArray) jsonObject.get("operations");

			//Create each balance operation 
			for (JSONObject opJSON : (Iterable<JSONObject>) listOperationsJSON) {
				BalanceOperationImpl op= new BalanceOperationImpl(
						(Integer) opJSON.get("id"),
						(String) opJSON.get("dateString"),
						(Double) opJSON.get("amount"),
						(String) opJSON.get("type"));
						
				operations.put(op.getBalanceId(), op);
			}

			//Set the id generator for the balance operations 
			BalanceOperationImpl.idGen = idGen;

		} catch (IOException | ParseException e) {
			//return an empty map if there is some error.
			operations.clear();
		}

		return operations;

	}
	
	public static List<CreditCard> readCreditCards(String fileName) {
		return null;
	}

}
