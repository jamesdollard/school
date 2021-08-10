/**
 * This class stores the information in a transaction request
 * @author jamesdollard
 *
 */
public class TransactionRequest {
	
	private String timeStamp;
	private int transactionNumber;
	private String cardNumber;
	private String vendorName;
	private double purchaseAmount;
	
	public TransactionRequest(String timeStamp, int transactionNumber, String cardNumber, String vendorName, double purchaseAmount){
		
		this.timeStamp = timeStamp;
		this.transactionNumber = transactionNumber;
		this.cardNumber = cardNumber;
		this.vendorName = vendorName;
		this.purchaseAmount = purchaseAmount;
		
	}
	
	/**
	 * Returns time stamp
	 * @return
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Returns transaction number
	 * @return
	 */
	public int getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * Returns card number
	 * @return
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * Returns vendor name
	 * @return
	 */
	public String getVendorName() {
		return vendorName;
	}

	/**
	 * Returns purchase amount
	 * @return
	 */
	public double getPurchaseAmount() {
		return purchaseAmount;
	}
	
}
