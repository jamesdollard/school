import java.util.ArrayList;

/**
 * This class is central to the design. It stores account and transaction information and calls other classes to process them.
 * @author jamesdollard
 *
 */
public class Bank {

	// Bank code definitions
	static final int SUCCESSFUL_TRANSACTION = 1;
	static final int INVALID_CARD = 2;
	static final int EXCEEDED_CREDIT_FAILED_TRANSACTION = 3;

	private ArrayList<UserAccount> userAccounts; //maybe use hashmap here
	private ArrayList<TransactionRequest> transactionRequests;
	
	private ArrayList<TransactionRequest> validatedRequests = new ArrayList<TransactionRequest>();
	private ArrayList<TransactionRequest> invalidCardRequests = new ArrayList<TransactionRequest>();
	private ArrayList<TransactionRequest> insufficientCreditFailedTransactions = new ArrayList<TransactionRequest>();
	
	public Bank() {
		
		CSVReader csvReader = new CSVReader();
		userAccounts = csvReader.getAllAccounts();
		transactionRequests = csvReader.getAllTransactionRequests();
		
	}
	
	/**
	 * Processes transaction requests and creates a ReportCompiler instance to build a report
	 */
	public void processMonthlyReport() {
		processTransactionRequests();
		ReportCompiler reportCompiler = new ReportCompiler(validatedRequests, invalidCardRequests, insufficientCreditFailedTransactions, userAccounts);
		reportCompiler.recordActivity();
	}
	
	/**
	 * Processes all transaction requests, updates rebates, and flags exceeded credit
	 */
	private void processTransactionRequests() {
		connectUsersToBank();
		
		// Gives each account its own TransactionRequest
		while(!transactionRequests.isEmpty()) {
			TransactionRequest pendingTransaction = transactionRequests.remove(0);
			UserAccount pendingUser = getAccountFromCardNumber(pendingTransaction.getCardNumber());
			
			if(pendingUser == null) {
				invalidCardRequests.add(pendingTransaction);
			}else {
				pendingUser.addTransaction(pendingTransaction);
			}
		}
		
		// Asks each account to update its process rebates and flag exceeded credit
		for(int i = 0; i < userAccounts.size(); i++) {
			UserAccount accountToUpdate = userAccounts.get(i);
			accountToUpdate.processRebatesAndFlagExceededCredit();
		}
	}
	
	/**
	 * Connects all user accounts to this bank
	 */
	private void connectUsersToBank() {
		for(int i = 0; i < userAccounts.size(); i++) {
			UserAccount accountAtHand = userAccounts.get(i);
			accountAtHand.setBank(this);
		}
	}
	
	/**
	 * Returns an accounts that matches the card number, or returns null if no accounts match
	 * @param cardNumber
	 * @return
	 */
	public UserAccount getAccountFromCardNumber(String cardNumber) {
		
		for(int i = 0; i < userAccounts.size(); i++) {
			UserAccount accountToCheck = userAccounts.get(i);
			String accountNumber = accountToCheck.getCardNumber();
			if(accountNumber.equals(cardNumber)) {
				return accountToCheck;
			}
		}
		return null;
		
	}
	/**
	 * Method for user accounts to record when an insufficient credit transaction has been attempted
	 * @param insufficientCreditRequest
	 */
	public void recordInsufficientCreditTransaction(TransactionRequest insufficientCreditRequest) {
		insufficientCreditFailedTransactions.add(insufficientCreditRequest);
	}
	
	public static void main(String[] args) {
		
		Bank bank = new Bank();
		

		
		bank.processMonthlyReport();
		
		
	}
	
	
	
}
