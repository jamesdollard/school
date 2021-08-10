/**
 * Implementation of the RebateStrategy interface. Calculates a rebate using the Platinum Strategy
 * @author jamesdollard
 *
 */
public class PlatinumRebate implements RebateStrategy{

	static final double PLATINUM_REBATE_PERCENTAGE = 0.02;

	public void calculateRebate(UserAccount accountToUpdate) {
		accountToUpdate.setRebateAmount(PLATINUM_REBATE_PERCENTAGE * accountToUpdate.getCurrentCardBalance());
	}

}
