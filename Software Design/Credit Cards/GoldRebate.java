/**
 * Implementation of the RebateStrategy interface. Calculates a rebate using the Gold Strategy
 * @author jamesdollard
 *
 */
public class GoldRebate implements RebateStrategy{

	static final double GOLD_REBATE_PERCENTAGE = 0;

	public void calculateRebate(UserAccount accountToUpdate) {
		accountToUpdate.setRebateAmount(GOLD_REBATE_PERCENTAGE * accountToUpdate.getCurrentCardBalance());
	}

}
