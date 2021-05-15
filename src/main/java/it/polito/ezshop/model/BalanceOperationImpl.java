package it.polito.ezshop.model;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.EZShopMaps;
import it.polito.ezshop.data.FileWrite;

import java.time.LocalDate;

public class BalanceOperationImpl implements BalanceOperation {

	public static Integer idGen = 1;
	private Integer id;
	private LocalDate date;
	private Double amount;
	private String type;

	public BalanceOperationImpl(Double amount, String type) {
		this.amount = amount;
		this.type = type;
		this.date = LocalDate.now();
		this.id = idGen++;
	}

	public BalanceOperationImpl(Integer id, String dateString, Double amount, String type) {
		this.id = id;
		this.date = LocalDate.parse(dateString);
		this.amount = amount;
		this.type = type;
	}

	@Override
	public int getBalanceId() {
		return id;
	}

	@Override
	public void setBalanceId(int balanceId) {
		if (balanceId > 0 && !EZShopMaps.operations.containsKey(id)) {
			if (balanceId > idGen)
				idGen = balanceId + 1;
			this.id = balanceId;
		}
	}

	@Override
	public LocalDate getDate() {
		return date;
	}

	@Override
	public void setDate(LocalDate date) {
		if (date != null) {
			this.date = date;
		}
	}

	@Override
	public double getMoney() {
		return amount;
	}

	@Override
	public void setMoney(double money) {
		this.amount = money;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		if (type != null
				&& (type == "SALE" || type == "ORDER" || type == "RETURN" || type == "CREDIT" || type == "DEBIT")) {
			this.type = type;
		}
	}
}
