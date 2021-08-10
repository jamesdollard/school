/**
 * Strategy interface to calculate a user account's rebate.
 * @author jamesdollard
 *
 */
public interface RebateStrategy {

	/**
	 * Calculates rebate
	 * @param accountToUpdate
	 */
	public void calculateRebate(UserAccount accountToUpdate);

}
