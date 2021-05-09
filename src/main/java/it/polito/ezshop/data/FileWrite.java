package it.polito.ezshop.data;

import java.util.List;
import java.util.Map;

import it.polito.ezshop.model.CreditCard;
import it.polito.ezshop.model.LoyaltyCard;
import it.polito.ezshop.model.ReturnTransaction;

public class FileWrite {

	public static boolean writeUsers(String fileName, Map<Integer, User> users) {
		return false;
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
