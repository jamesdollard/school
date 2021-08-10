
/**
 * Interface for strategy pattern
 * @author jamesdollard
 *
 */
public interface PlayingStrategy {
	
	public static final int ROUND_OVER = Cribbage.ROUND_OVER;
	
	/**
	 * Chooses card based on implemented strategy
	 * @param pileTotal
	 * @param computer
	 * @return chosen playing card
	 */
	public PlayingCard chooseCard(int pileTotal, Player computer);

}
