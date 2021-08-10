import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Min/max implementation of the PlayingStrategy interface. Chooses either the minimum or maximum value card in the hand
 * @author jamesdollard
 *
 */
public class MinMaxStrategy implements PlayingStrategy {
	
	/**
	 * Chooses card based on min/max strategy
	 * @return chosen playing card
	 */
	public PlayingCard chooseCard(int pileTotal, Player computer) {
		
		int limit = ROUND_OVER - pileTotal;
		PlayingCard nextCard = null;
		//like other strategy, make sure the chosen card won't go over 31
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
			//Order the cards by value
			Collections.sort(inRange);
			
			//Chooses absolute smallest card half of the time and absolute largest the other half
			Random randomGen = new Random();
			if(randomGen.nextInt(2) == 0) 
			{
				nextCard =  inRange.get(0);
			}
			else 
			{
				nextCard = inRange.get(inRange.size() - 1);
			}
			
		}
		if (nextCard != null)
		{
			System.out.println("Computer:" + nextCard);
		}
		return nextCard;
		
	}

}
