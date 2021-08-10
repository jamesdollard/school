import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
   
		 
/**
 * This class reads .csv files. It creates user accounts using the userAccountFactory and also creates transaction requests.
 * @author jamesdollard
 *
 */
public class CSVReader {

	private ArrayList<UserAccount> userAccounts = new ArrayList<UserAccount>();
	private ArrayList<TransactionRequest> transactionRequests = new ArrayList<TransactionRequest>();
	
	/**
	 * Reads accounts from the CreditCardAccounts.csv file
	 * @return
	 */
	private boolean readAccounts() {
		try {
			Scanner in = new Scanner(new FileInputStream("CreditCardAccounts.csv"));
			while (in.hasNextLine()) {
				
				String nextLine = in.nextLine();
				String regularExpression = ",";
				String[] listOfWords = nextLine.split(regularExpression);
				
				String cardNumber = listOfWords[0];
				String userName = listOfWords[1];
				String cardType = listOfWords[2];

				double cardBalance = Double.valueOf(listOfWords[3]);
				
				UserAccount newAccount = UserAccountFactory.getUserAccount(cardNumber, userName, cardType, cardBalance);

				userAccounts.add(newAccount);

			}
			in.close();
			return true;
		}
		catch (FileNotFoundException e) {
			System.out.println("Had problem with file IO");
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Reads accounts from the CreditCardTransactions.csv file
	 * @return
	 */
	private boolean readTransactions() {
		try {
			Scanner in = new Scanner(new FileInputStream("CreditCardTransactions.csv"));
			while (in.hasNextLine()) {
				
				String nextLine = in.nextLine();
				String regularExpression = ",";
				String[] listOfWords = nextLine.split(regularExpression);
				
				String timeStamp = listOfWords[0];
				int transactionNumber = Integer.valueOf(listOfWords[1]);
				String cardNumber = listOfWords[2];
				String vendorName = listOfWords[3];
				double purchaseAmount = Double.valueOf(listOfWords[4]);
				
				TransactionRequest newTransactionRequest = new TransactionRequest(timeStamp, transactionNumber, cardNumber, vendorName, purchaseAmount);
				transactionRequests.add(newTransactionRequest);
				
			}
			in.close();
			return true;
		}
		catch (FileNotFoundException e) {
			System.out.println("Had problem with file IO");
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Returns array list of all accounts
	 * @return
	 */
	public ArrayList<UserAccount> getAllAccounts() {
		readAccounts();
		return userAccounts;
	}
	
	/**
	 * Returns array list of all transaction requests
	 * @return
	 */
	public ArrayList<TransactionRequest> getAllTransactionRequests(){
		readTransactions();
		return transactionRequests;
	}
	
}
