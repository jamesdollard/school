import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

/**
 * This class captures a cribbage score - the numbers and the reason. It also has all the helper
 * methods needed to do scoring. 
 * @author jburge
 *
 */
public class Score {
	private int pointsAwarded;
	private String explanation;

	private static int[] scoreVal = {0, 0, 2, 6, 12};
	private static String[] names = {"zero", "one", "two", "three", "four", "five", "six", "seven"};

	/**
	 * Creates a new Score instance with points and a reason for those points
	 * @param points
	 * @param reason
	 */
	public Score(int points, String reason) {
		pointsAwarded = points;
		explanation = reason;
	}
	
	/**
	 * Returns the explanation for the score so it can be printed out
	 * @return - the explanation
	 */
	public String toString()
	{
		return explanation;
	}

	/******* Pile Scoring Methods *******/
	
	/**
	 * Check to see if the cards on the pile add up exactly to fifteen
	 * @param pile
	 * @return returns the appropriate score or null if no runs found
	 */
	public  static Score checkFifteen(ArrayList<PlayingCard> pile)
	{
		int total = 0;
		int next = 0;
		while ((total <= 15) && (next < pile.size()))
		{
			total += pile.get(next).value();
			next++;
		}
		if (total == 15)
		{
			return new Score(2, "two for fifteen");
		}
		return null; //no score
	}
	
	/**
	 * This checks for matches in the pile. For the pile, we are only concerned with matches
	 * that include the last card played (i.e., that start with the first card). This is 
	 * different from in the hand.
	 * @param pile
	 * @return the appropriate pair score or null if no runs found
	 */
	public static Score checkPairs(ArrayList<PlayingCard> pile)
	{
		int matches = 1;
		int next = 0;
		while (next < (pile.size() -1))
		{
			if (pile.get(next).getType() == (pile.get(next+1).getType()))
			{
				matches += 1;
				next += 1;
			}
			else
			{
				break;
			}
		}

		if (matches >= 2)
		{
			return new Score(scoreVal[matches], names[matches] + " of a kind");
		}
		return null;

	}
	
	/**
	 * Checks for runs in the pile. Order does not matter but duplicate cards break the run.
	 * This means that only the player who completes the run gets the points during pegging.
	 * @param pile - the merged piles
	 * @return returns the appropriate run score or null if no runs found
	 */
	public static  Score checkRuns(ArrayList<PlayingCard> pile)
	{
		ArrayList<PlayingCard> sortedList = null;
		for (int length=7; length >= 3; length--)
		{
			if (pile.size() >= length)
			{
				sortedList = partialSort(pile, length);
				if (isConsecutive(sortedList))
				{
					return new Score(length, "run of " + length);
				}
			}
		}
		return null;
	}

	/**
	 * Combines the two piles into one ArrayList of cards - assume that currentPile has the
	 * last card drawn
	 */
	public static ArrayList<PlayingCard> combinePile(ArrayList<PlayingCard> currentPile, ArrayList<PlayingCard> otherPile)
	{
		//check to make sure there's something in the first pile (needed for undo)
		if (currentPile.isEmpty())
		{
			return otherPile;
		}
		//Combine the piles so they can be checked
		ListIterator<PlayingCard> firstPile = currentPile.listIterator();
		ListIterator<PlayingCard> secondPile = otherPile.listIterator();
		ArrayList<PlayingCard>combinedPile = new ArrayList<PlayingCard>();

		while (firstPile.hasNext())
		{
			combinedPile.add(firstPile.next());
			if (secondPile.hasNext())
			{
				combinedPile.add(secondPile.next());
			}
		}
		return combinedPile;
	}

	/**
	 * Scores the pile - assume the current pile has the last drawn card
	 * @param currentPile
	 * @param otherPile
	 * @return an aggregate of scores from the pile
	 */
	public static ArrayList<Score> scorePile(ArrayList<PlayingCard> currentPile, ArrayList<PlayingCard> otherPile)
	{
		ArrayList<Score> scores = new ArrayList<Score>();

        ArrayList<PlayingCard> combinedPile = combinePile(currentPile, otherPile);
        
		//check for fifteens
		Score fifteen = checkFifteen(combinedPile);
		if (fifteen != null)
		{
			scores.add(fifteen);
		}
		//check for pairs
		Score pairs = checkPairs(combinedPile);
		if (pairs != null)
		{
			scores.add(pairs);
		}

		//check for straights
		Score runs = checkRuns(combinedPile);
		if (runs != null)
		{
			scores.add(runs);
		}

		return scores;
	}
	
	/******* Hand Scoring Methods *****/

	/**
	 * Checks to see if there are any flushes in the hand
	 * @param hand - the player's hand
	 * @param starter - the start card
	 * @return - a score - four or five flushes count
	 */
	public static Score  checkFlushes(ArrayList<PlayingCard> hand, PlayingCard starter)
	{
		int matches = 1;
		int next = 0;
		while (next < (hand.size() -1))
		{
			if (hand.get(next).getSuit().equals(hand.get(next+1).getSuit()))
			{
				matches += 1;
				next += 1;
			}
			else
			{
				break;
			}
		}
		if (matches == 4)
		{
			if (starter.getSuit().equals(hand.get(0).getSuit()))
			{
				return new Score(5, "flush plus matching starter");
			}
			else
			{
				return new Score(4, "flush");
			}
		}
		return null;
	}

	/**
	 * Checks for pairs in the hand (or crib) - this will include the start card. O
	 * @param hand - the cards in the hand
	 * @param start - the start card
	 * @return a list of any pairs found
	 */
	public static ArrayList<Score> checkPairs(ArrayList<PlayingCard> hand, PlayingCard start)
	{

		ArrayList<Score>scores = new ArrayList<Score>();
		ArrayList<PlayingCard> sortedList = new ArrayList<PlayingCard>();
		sortedList.addAll(hand);
		sortedList.add(start);
		Collections.sort(sortedList);

		int lastValue = sortedList.get(0).getType();
		int index = 1;
		int match = 1;
		while (index < sortedList.size())
		{
			int thisValue = sortedList.get(index).getType();
			if (thisValue == lastValue)
			{
				match++;
			}
			else
			{
				//report current matches
				if (match > 1)
				{
					scores.add(new Score(scoreVal[match], names[match] + " of a kind" ));
				}
				match = 1;
			}
			lastValue = thisValue;
			index++;
		}
		//report current matches
		if (match > 1)
		{
			scores.add(new Score(scoreVal[match], names[match] + " of a kind" ));
		}
		return scores;
	}


	/**
	 * Checks to see if there are runs in a hand + start card. This code checks for double-runs.
	 * @param hand - the cards in the player's hand
	 * @param start - the start card
	 * @return a list of scores
	 */
	public static ArrayList<Score> checkRuns(ArrayList<PlayingCard> hand, PlayingCard start)
	{
		ArrayList<Score> scores = new ArrayList<Score>();
		ArrayList<PlayingCard> sortedList = new ArrayList<PlayingCard>();
		sortedList.addAll(hand);
		sortedList.add(start);
		Collections.sort(sortedList);

		//Do we have a run of five?
		if (isConsecutive(sortedList))
		{
			scores.add(new Score(5, "run of five"));
			return scores;
		}
		//Check for other runs
		int duplicates = 0;
		int consecutive = 1;
		int lastValue = sortedList.get(0).getType();
		int index = 1;
		while (index < sortedList.size())
		{
			if (sortedList.get(index).getType() == lastValue)
			{
				duplicates++;
				index++;
			}
			else if ((sortedList.get(index).getType() - lastValue) == 1)
			{
				lastValue = sortedList.get(index).getType();
				index++;
				consecutive++;
			}
			else
			{
				//does what we have count as a score or scores?
				if (consecutive >= 3)
				{
					for (int ndup = 0; ndup <= duplicates; ndup++)
					{
						Score nextScore = new Score(consecutive, "run of " + consecutive);
						scores.add(nextScore);
					}
					//reset
					consecutive = 1;
					duplicates = 0;
				}
				if ((sortedList.size() - index) >= 3)
				{
					lastValue = sortedList.get(index).getType();
					index++;
					consecutive = 1;
					duplicates = 0;
				}
				else
				{
					consecutive = 1;
					duplicates = 0;
					break; //not enough left to start a new run
				}
			}
		}
		//does what we have count as a score or scores?
		if (consecutive >= 3)
		{
			for (int ndup = 0; ndup <= duplicates; ndup++)
			{
				Score nextScore = new Score(consecutive, "run of " + consecutive);
				scores.add(nextScore);
			}
		}
		return scores;
	}
	
	/**
	 * Checks if there's a matching jack
	 * @param hand
	 * @param start
	 * @return appropraite score
	 */
	public  Score checkMatchingJack(ArrayList<PlayingCard> hand, PlayingCard start)
	{
		for (PlayingCard card : hand)
		{
			if (card.equalsType(PlayingCard.JACK) && card.getSuit().equals(start.getSuit()))
			{
				return new Score(2, "one for his nob");
			}
		}
		return null;
	}
	
	/**
	 * Counts sum of cards
	 * @param total
	 * @param cards
	 * @return sum of cards
	 */
	public static int countSums(int total, ArrayList<PlayingCard> cards)
	{
		int count = 0;
		if (cards.isEmpty())
		{
			return 0;
		}
		if ((cards.size() == 1) && (cards.get(0).value() == total))
		{
			return 1;
		}
		
		ArrayList<PlayingCard> rest = new ArrayList<PlayingCard>();
		rest.addAll(cards.subList(1,  cards.size()));
		//First card can't be part of the total
		if (cards.get(0).value() > total)
		{
			return countSums(total, rest);
		}
		else if (cards.get(0).value() == total)
		{
			return 1 + countSums(total, rest);
		}
		//First card might be part of the total
		else
		{
			return countSums(total, rest) + countSums((total - cards.get(0).value()), rest);
		}
		
	}
	
	/**
	 * Returns score per rule of fifteen
	 * @param cards
	 * @param starter
	 * @return appropriate score
	 */
	public static Score countFifteens(ArrayList<PlayingCard> cards, PlayingCard starter)
	{
		ArrayList<PlayingCard> sortedList = new ArrayList<PlayingCard>();
		sortedList.addAll(cards);
		sortedList.add(starter);
		Collections.sort(sortedList);
		int numFifteens = countSums(15, sortedList);
		if (numFifteens > 0)
		{
			int points = numFifteens*2;
			Score ourScore = new Score(points, points + " for fifteen");
			return ourScore;
		}
		return null;
	}
	
	/**
	 * Returns list of scores by comparing hand to starter card
	 * @param cards
	 * @param starter
	 * @return appropriate score
	 */
	public static ArrayList<Score> scoreHand(ArrayList<PlayingCard> cards, PlayingCard starter)
	{
		ArrayList<Score> scores = new ArrayList<Score>();
		Score fifteens = countFifteens(cards, starter);
		if (fifteens != null)
		{
			scores.add(fifteens);
		}
		Score flushes = checkFlushes(cards, starter);
		if (flushes != null)
		{
			scores.add(flushes);
		}
		if (scores != null)
		{
			scores.addAll(checkRuns(cards, starter));
		}
		if (scores != null)
		{
			scores.addAll(checkPairs(cards, starter));
		}
		
		return scores;
	}
	/****** Helper Methods *****/

	/**
	 * This code takes an arrayList of playing cards and returns a new sorted list containing the top n cards 
	 * where n is the number passed in
	 * @param cards - the cards
	 * @param number - the number of cards requested
	 * @return the sorted cards. This is a new list (we don't want to modify the order of the original)
	 */
	public static ArrayList<PlayingCard> partialSort(ArrayList<PlayingCard> cards, int number)
	{
		ArrayList<PlayingCard>sortedList = new ArrayList<PlayingCard>();
		sortedList.addAll(cards.subList(0, number));
		Collections.sort(sortedList);
		return sortedList;
	}

	/**
	 * Checks to see if a list of cards are a run. Duplicates break a run.
	 * @param cards - the cards being checked
	 * @return true if the cards are consecutive
	 */
	public static  boolean isConsecutive(ArrayList<PlayingCard> cards)
	{
		boolean consecutive = true;
		int lastValue = cards.get(0).getType();
		int index = 1;
		while (index < (cards.size()))
		{
			if ((cards.get(index).getType() - lastValue) == 1)
			{
				lastValue = cards.get(index).getType();
				index++;
			}
			else
			{
				consecutive = false;
				break;
			}			
		}
		return consecutive;	
	}

	/**
	 * Adds up an ArrayList of scores
	 * @param scores
	 * @return - the total score
	 */
	public static int addScores(ArrayList<Score> scores)
	{
		if (scores == null)
			return 0;
		
		int sum = 0;
		for (Score s : scores)
		{
			sum += s.pointsAwarded;
		}
		return sum;
	}
	
	/**
	 * Gets points
	 * @return points
	 */
	public int getPoints()
	{
		return pointsAwarded;
	}

}
