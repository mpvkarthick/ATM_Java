/**
 * 
 */
package com.mannepk.atm.model;

import java.util.Map;

/**
 * @author Karthik Mannepalli
 *
 */
public class Transaction {

	private String id;

	private String type;

	private String status;

	private double amount;

	private double balance;

	private Map<Integer, Integer> currency;

	private Map<Integer, Integer> dispensedCurrency;

	private String input;

	/**
	 * @return the dispensedCurrency
	 */
	public Map<Integer, Integer> getDispensedCurrency() {
		return dispensedCurrency;
	}

	/**
	 * @param dispensedCurrency the dispensedCurrency to set
	 */
	public void setDispensedCurrency(Map<Integer, Integer> dispensedCurrency) {
		this.dispensedCurrency = dispensedCurrency;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * @return the currency
	 */
	public Map<Integer, Integer> getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(Map<Integer, Integer> currency) {
		this.currency = currency;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	@Override
	public String toString() {
		if(type.equalsIgnoreCase("Credit"))
		return "Balance: " + currency + ",Total=" + balance; 
		else 
			return "Dispensed: "+dispensedCurrency+"\nBalance: " + currency + ",Total=" + balance; 
	}

}
