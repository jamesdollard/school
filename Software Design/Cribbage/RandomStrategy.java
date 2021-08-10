import java.util.ArrayList;
import java.util.Collections;

/**
 * Random implementation of the PlayingStrategy interface. Chooses a card in the hand at random.
 * @author jamesdollard
 *
 */
public class RandomStrategy implements PlayingStrategy {
	
	/**
	 * Chooses random card in hand
	 * @return random playing card
	 */
	public PlayingCard chooseCard(int pileTotal, Player computer) {
		
		int limit = ROUND_OVER - pileTotal;
		PlayingCard nextCard = null;
		//for now, just grab a random card within the limit
		ArrayList<PlayingCard> inRange = new ArrayList<PlayingCard>();
		for (PlayingCard card : computer.getHand())
		{
			if (card.value() < limit)
			{
				inRange.add(card);
			}
		}
		if (inRange.isEmpty())
		{
			return null;
		}
		else
		{
			//Shuffle the in range cards and choose one at random
			Collections.shuffle(inRange);
			nextCard =  inRange.get(0);
		}
		if (nextCard != null)
		{
			System.out.println("Computer:" + nextCard);
		}
		return nextCard;
		
	}

}
