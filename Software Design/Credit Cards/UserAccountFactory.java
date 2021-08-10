/**
 * This class is a Factory for UserAccount instances. It creates the user account with a strategy class depending on the account type.
 * @author jamesdollard
 *
 */
public class UserAccountFactory {
	
	static final int GOLD_CREDIT_LINE = 3000;
	static final int GOLD_ALLOWED_OVERDRAFT = 500;
	static final int PLATINUM_CREDIT_LINE = 5000;
	static final int PLATINUM_ALLOWED_OVERDRAFT = 1000;
	static final int CORPORATE_CREDIT_LINE = 10000;
	static final int CORPORATE_ALLOWED_OVERDRAFT = 5000;
	
	/**
	 * Returns a user account with the proper strategy, credit line, and allowed overdraft.
	 * @param cardNumber
	 * @param userName
	 * @param cardType
	 * @param cardBalance
	 * @return
	 */
	public static UserAccount getUserAccount(String cardNumber, String userName, String cardType, double cardBalance) {
		
		if(cardType.equals("Gold")) {
			return new UserAccount(cardNumber, userName, cardType, cardBalance, GOLD_CREDIT_LINE, GOLD_ALLOWED_OVERDRAFT, new GoldRebate());
		}
		else if(cardType.equals("Platinum")) {
			return new UserAccount(cardNumber, userName, cardType, cardBalance, PLATINUM_CREDIT_LINE, PLATINUM_ALLOWED_OVERDRAFT, new PlatinumRebate());

		}
		else { // Corporate card type is the only remaining option
			return new UserAccount(cardNumber, userName, cardType, cardBalance, CORPORATE_CREDIT_LINE, CORPORATE_ALLOWED_OVERDRAFT, new CorporateRebate());
		}
	}

}
