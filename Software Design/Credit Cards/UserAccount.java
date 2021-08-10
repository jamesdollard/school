import java.util.ArrayList;
import java.io.PrintWriter;

/**
 * This class stores the information for a user account and processes transactions related to the account
 * @author jamesdollard
 *
 */
public class UserAccount {
	
	//add a toString() method for better printing
	
	private String cardNumber;
	private String userName;
	private Card cardType;
	private double initialCardBalance;
	private double currentCardBalance;
	private double rebateAmount;
	private boolean creditExceeded;
	private Bank bank;
	
	private int creditLine;
	private int allowedOverdraft;

	private ArrayList<TransactionRequest> validatedTransactions = new ArrayList<TransactionRequest>();
	private ArrayList<TransactionRequest> failedTransactions = new ArrayList<TransactionRequest>();
	
	private RebateStrategy rebateStrategy;
	
	public UserAccount(String cardNumber, String userName, String cardType, double cardBalance, int creditLine, int allowedOverdraft, RebateStrategy cardTypeProcessor) {
		
		this.cardNumber = cardNumber;
		this.userName = userName;
		this.currentCardBalance = cardBalance;
		this.initialCardBalance = cardBalance;
		this.creditLine = creditLine;
		this.allowedOverdraft = allowedOverdraft;
		creditExceeded = false;
		rebateAmount = 0;
		
		this.rebateStrategy = cardTypeProcessor;
		
		switch(cardType) {
		case "Gold":
			this.cardType = Card.GOLD;
			break;
		case "Platinum":
			this.cardType = Card.PLATINUM;
			break;
		case "Corporate":
			this.cardType = Card.CORPORATE;
			break;
		}
	}
	
	public void toFile(PrintWriter out) {
		
		out.println("Credit Card: " + cardNumber + " " + cardType);
		out.println("Card-holder: " + userName);
		out.println("Initial balance: " + initialCardBalance);
		out.println();
		out.println("Transactions: ");
		
		double accountTransactionTotal = 0;
		
		for(int j = 0; j < validatedTransactions.size(); j++) {
			
			TransactionRequest requestAtHand = validatedTransactions.get(j);
			String requestCardNumber = requestAtHand.getCardNumber();
			if(requestCardNumber.equals(cardNumber)) {
				
				accountTransactionTotal += requestAtHand.getPurchaseAmount();
				
				out.println(requestAtHand.getTimeStamp() + " " 
						+ requestAtHand.getTransactionNumber() + " "
						+ requestAtHand.getVendorName() + " "
						+ requestAtHand.getPurchaseAmount() + " "
						+ (initialCardBalance + accountTransactionTotal));
			}
			
		}
		
		for(int j = 0; j < failedTransactions.size(); j++) {
			TransactionRequest requestAtHand = failedTransactions.get(j);
			String requestCardNumber = requestAtHand.getCardNumber();
			if(requestCardNumber.equals(cardNumber)) {
				
				out.println(requestAtHand.getTimeStamp() + " " 
						+ requestAtHand.getTransactionNumber() + " "
						+ requestAtHand.getVendorName() + " "
						+ requestAtHand.getPurchaseAmount() + " "
						+ "Transaction Failed: Credit limit exceeded");
			}
		}
		out.println();
		
		double totalBalance = initialCardBalance + accountTransactionTotal;
		
		out.println("Balance, before rebate: " + (totalBalance));
		
		out.println("Rebate: " + rebateAmount); //must fix this
		out.println("Balance due, after rebate: " + (totalBalance - rebateAmount)); //also this
		
		if(creditExceeded) {
			out.println("***** WARNING - credit limit exceeded *****");
		}
		
	}

	/**
	 * Processes a pending transaction request
	 * @param pendingRequest
	 */
	public void addTransaction(TransactionRequest pendingRequest) {
			if(isCreditExceeded(pendingRequest)) {
				failedTransactions.add(pendingRequest);
				bank.recordInsufficientCreditTransaction(pendingRequest);
				System.out.println(1);
			}
			else {
				currentCardBalance += pendingRequest.getPurchaseAmount();
				validatedTransactions.add(pendingRequest);
				System.out.println(2);
			}
	}
	
	/**
	 * Calculates rebate and determines whether the account's credit has been exceeded
	 */
	public void processRebatesAndFlagExceededCredit() {
		
		rebateStrategy.calculateRebate(this);
		if(currentCardBalance >= creditLine) {
			creditExceeded = true;
		}
	}
	
	/**
	 * Checks if the account's credit will be exceeded by a pending transaction request
	 * @param requestToCheck
	 * @return
	 */
	public boolean isCreditExceeded(TransactionRequest requestToCheck) {
		if(currentCardBalance >= creditLine) {
			return true;
		}
		else if(currentCardBalance + requestToCheck.getPurchaseAmount() > creditLine + allowedOverdraft) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Gets current card balance
	 * @return
	 */
	public double getCurrentCardBalance() {
		return currentCardBalance;
	}
	
	/**
	 * Ties this account to a bank
	 * @param bank
	 */
	public void setBank(Bank bank) {
		this.bank = bank;
	}

	/**
	 * Sets card balance
	 * @param cardBalance
	 */
	public void setCardBalance(double cardBalance) {
		this.currentCardBalance = cardBalance;
	}
	
	/**
	 * Sets exceeded credit flag
	 * @param creditExceeded
	 */
	public void setExceededCreditFlag(boolean creditExceeded) {
		this.creditExceeded = creditExceeded;
	}

	/**
	 * Gets card number
	 * @return
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * Gets user name
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Gets card type
	 * @return
	 */
	public Card getCardType() {
		return cardType;
	}
	
	/**
	 * Sets rebate amount
	 * @param rebateAmount
	 */
	public void setRebateAmount(double rebateAmount) {
		this.rebateAmount = rebateAmount;
	}
	
	/**
	 * Gets rebate amount
	 * @return
	 */
	public double getRebateAmount() {
		return rebateAmount;
	}
	
	/**
	 * Enumerated type defining three types of cards: Gold, Platinum, and Corporate
	 * @author jamesdollard
	 *
	 */
	public enum Card
	{
		GOLD, PLATINUM, CORPORATE;
	}

	
	
	
	

}
