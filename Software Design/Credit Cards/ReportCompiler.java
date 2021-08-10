import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * This class writes a BankReport.txt file which details account and transaction activity
 * @author jamesdollard
 *
 */
public class ReportCompiler {
	
	private ArrayList<TransactionRequest> validatedTransactions = new ArrayList<TransactionRequest>();
	private ArrayList<TransactionRequest> invalidCardRequests = new ArrayList<TransactionRequest>();
	private ArrayList<TransactionRequest> insufficientCreditFailedTransactions = new ArrayList<TransactionRequest>();
	private ArrayList<UserAccount> userAccounts;
	
	public ReportCompiler(ArrayList<TransactionRequest> validatedRequests, ArrayList<TransactionRequest> invalidCardRequests, ArrayList<TransactionRequest> insufficientCreditFailedTransactions, ArrayList<UserAccount> userAccounts) {
		this.validatedTransactions = validatedRequests;
		this.invalidCardRequests = invalidCardRequests;
		this.insufficientCreditFailedTransactions = insufficientCreditFailedTransactions;
		this.userAccounts = userAccounts;
	}
	
	/**
	 * Writes report to BankReport.txt
	 */
	public void recordActivity() {
		try {
			
			PrintWriter out = new PrintWriter(new FileOutputStream("BankReport.txt"));
			
			// Records failed transactions
			
			out.println("Failed Transactions:\n");
			for(int i = 0; i < invalidCardRequests.size(); i++) {
				TransactionRequest invalidRequest = invalidCardRequests.get(i);
				out.println(invalidRequest.getTimeStamp() + " " 
						+ invalidRequest.getTransactionNumber() + " "
						+ invalidRequest.getVendorName() + " "
						+ invalidRequest.getPurchaseAmount() + " "
						+ "Transaction Failed: Invalid card number");
			}
			for(int i = 0; i < insufficientCreditFailedTransactions.size(); i++) {
				TransactionRequest invalidRequest = insufficientCreditFailedTransactions.get(i);
				out.println(invalidRequest.getTimeStamp() + " " 
						+ invalidRequest.getTransactionNumber() + " "
						+ invalidRequest.getVendorName() + " "
						+ invalidRequest.getPurchaseAmount() + " "
						+ "Transaction Failed: Credit limit exceeded");
			}
			out.println();
			
			// Records user activity
			
			for(int i = 0; i < userAccounts.size(); i++) {
				while(userAccounts.size() > 0) {
					
					UserAccount accountAtHand = userAccounts.remove(0);
					accountAtHand.toFile(out);
					
					out.println();
					
					
					
					
					
					
				}
			}
			
			
			
			
			
			
			
			out.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("file could not be created");
		}
	}
}
	
