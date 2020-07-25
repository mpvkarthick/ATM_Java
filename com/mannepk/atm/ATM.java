package com.mannepk.atm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import com.mannepk.atm.exception.ATMException;
import com.mannepk.atm.model.Transaction;

public class ATM {


	static BufferedReader br;

	static Scanner s;

	Map<Integer, Integer> currencyStore;

	String[] availableDenominations;

	List<String> sortedDenominations;

	double balance;

	/**
	 * Initializes Total Amount, Denominations to Count Mapping in the Bank
	 * 
	 * @param allowedCurrency
	 * @throws Exception
	 */
	public void createGlobalStore(String allowedCurrency) throws Exception {
		this.availableDenominations = allowedCurrency.split(",");
		sortedDenominations = Arrays.asList(allowedCurrency.split(","));
		// sort the incoming denominations in descending order
		Collections.sort(sortedDenominations, (a, b) -> Integer.parseInt(b)
				- Integer.parseInt(a));
		currencyStore = new HashMap<Integer, Integer>();
		//Initialize the  Denominations to Count Mapping to 0 for all Denominations
		sortedDenominations.stream().forEach(currency -> {
			if (Integer.parseInt(currency) > 0) {
				currencyStore.put(Integer.parseInt(currency), 0);
			}
		});
		this.balance = 0.0;
	}

	public static void main(String[] args) throws IOException {

		ATM atm = new ATM();

		try {
			String allowedCurrency = args[0];
			atm.createGlobalStore(allowedCurrency);
		} catch (Exception e) {
			System.out.println("Invalid Denomination Input");
			System.exit(0);
		}

		Scanner s = new Scanner(System.in);
		br = new BufferedReader(new InputStreamReader(System.in));
		String dInput;
		Transaction transaction;
		System.out.println("Welcome User, please choose your action - ");
		while (true) {
			System.out.println("Please choose your action - ");
			System.out.println("Choose 1 for Deposit");
			System.out.println("Choose 2 for Withdraw");
			System.out.println("Choose 3 for EXIT");
			System.out.println("Enter the operation");
			int n = Integer.parseInt(br.readLine());
			switch (n) {
			case 1:
				System.out.println("Enter money to be Deposited:");
				dInput = br.readLine();
				transaction = new Transaction();
				try {
					// deposit the incoming currency denomination
					atm.deposit(dInput, transaction);
					System.out.println(transaction.toString());

				} catch (ATMException e) {
					transaction.setStatus("Failed");
					System.out.println(e.getReason());
				} catch (Exception e) {
					transaction.setStatus("Failed");
					System.out.println("Invalid Input");
				}
				System.out.println("");
				break;

			case 2:
				System.out.println("Enter money to be Withdrawn:");
				dInput = br.readLine();
				transaction = new Transaction();
				try {
					atm.withdraw(dInput, transaction);
					System.out.println(transaction.toString());

				} catch (ATMException e) {
					transaction.setStatus("Failed");
					System.out.println(e.getReason());
				} catch (Exception e) {
					transaction.setStatus("Failed");
					System.out.println("Invalid Input");
				}
				System.out.println("");
				break;

			case 3:

				s.close();
				br.close();
				System.exit(0);
			}
		}

	}

	public void deposit(String input, Transaction transaction)
			throws ATMException {

		transaction.setId(UUID.randomUUID().toString());
		transaction.setType("Credit");
		transaction.setInput(input);

		// Step 1 extract denominations and amount
		// Step 2 run the rules
		// Step 3, in case of no errors, calculate the
		// System.out.print("Input inside ATM "+input);
		// System.out.print(globalStore.toString());
		Map<Integer, Integer> currency = new HashMap<Integer, Integer>();
		int denomination = 0;
		int billCount = 0;
		List<String> billInfos = Arrays.asList(input.split(","));
		double totalDepositAmount = 0;
		// rules to be executed
		// 1. valid integers provided
		// 2. No Negative Numbers
		// 3. Valid Denominations Provided
		// 4. Total input amount cannot be zero
		for (String billInfo : billInfos) {
			// billInfo.split("s: ");
			try {
				denomination = Integer
						.parseInt(billInfo.split("s: ")[0].trim());
				billCount = Integer.parseInt(billInfo.split("s: ")[1].trim());
			} catch (NumberFormatException nf) {
				throw new ATMException("Incorrect deposit amount");
			}

			// Valid positive integer Check
			if (denomination < 0 || billCount < 0) {
				throw new ATMException("Incorrect deposit amount");
			}
			// Valid Denomination Check
			if (!Arrays.asList(this.availableDenominations).contains(
					billInfo.split("s: ")[0].trim())) {
				throw new ATMException("Invalid Denomination");
			}
			currency.put(denomination, billCount);
			totalDepositAmount += denomination * billCount;
		}
		// if total amount is 0, throw an error
		if (totalDepositAmount == 0) {
			throw new ATMException("Deposit amount cannot be zero");
		}
		// No error, proceed to adding denominations to store and generating
		// balance
		transaction.setAmount(totalDepositAmount);
		transaction.setCurrency(currency);

		this.currencyStore
				.entrySet()
				.stream()
				.forEach(
						c -> {
							currency.put(
									c.getKey(),
									currency.getOrDefault(c.getKey(), 0)
											+ c.getValue());
						});
		currencyStore = currency;
		this.balance += totalDepositAmount;
		transaction.setBalance(this.balance);
		transaction.setStatus("Success");

	}

	public void withdraw(String input, Transaction t) throws ATMException,
			Exception {
		t.setId(UUID.randomUUID().toString());
		t.setType("Debit");
		Map<Integer, Integer> dispensedCurrency = new HashMap<Integer, Integer>();
		// double balance = globalStore.debit(t.getAmount(),dispensedCurrency);
		Map<Integer, Integer> globalCurrencyStore;
		double tempAmount = Double.parseDouble(input);
		double amount = Double.parseDouble(input);
		if (amount > this.balance || amount <= 0) {
			throw new ATMException("Incorrect or insufficient funds");
		}
		int billDenom, count = 0;
		// Deduct the amount from back in the sorted order of denominations
		for (String denomination : this.sortedDenominations) {
			billDenom = Integer.parseInt(denomination);
			if (this.currencyStore.get(billDenom) == 0) {
				continue;
			}
			if (amount >= billDenom) {
				count = ((int) (amount / billDenom)) > this.currencyStore
						.get(billDenom) ? this.currencyStore.get(billDenom)
						: ((int) (amount / billDenom));
				dispensedCurrency.put(billDenom, count);
				amount = amount - billDenom * count;
			}

		}
		if (amount > 0) {
			throw new ATMException(
					"Requested withdraw amount is not dispensable");
		}
		globalCurrencyStore = this.currencyStore;
		dispensedCurrency
				.entrySet()
				.stream()
				.forEach(
						d -> {
							globalCurrencyStore.put(
									d.getKey(),
									globalCurrencyStore.get(d.getKey())
											- d.getValue());
						});

		this.balance = this.balance - tempAmount;
		this.currencyStore = globalCurrencyStore;
		t.setDispensedCurrency(dispensedCurrency);
		t.setBalance(this.balance);
		t.setStatus("Success");
		t.setCurrency(globalCurrencyStore);

	}

}
