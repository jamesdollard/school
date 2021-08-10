/**
 * Implementation of the RebateStrategy interface. Calculates a rebate using the Corporate Strategy
 * @author jamesdollard
 *
 */
public class CorporateRebate implements RebateStrategy{

	static final double CORPORATE_REBATE_PERCENTAGE = 0.05;
	
	public void calculateRebate(UserAccount accountToUpdate) {
		accountToUpdate.setRebateAmount(CORPORATE_REBATE_PERCENTAGE * accountToUpdate.getCurrentCardBalance());
	}

}
