import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Stack;

/**
 * Defines a group of cards laid down on the table
 * @author jburge
 *
 */
public class Pile {
	private Stack<PlayingCard> activePile; //cards laid down - order matters!
	private Stack<PlayingCard> previousPile; //pile that had hit 31

	/**
	 * Create an empty pile
	 */
	public Pile() {
		activePile = new Stack<PlayingCard>();
		previousPile = new Stack<PlayingCard>();
	}

	/**
	 * Create a new pile
	 */
	public void newPile() {
		previousPile.addAll(activePile);
		activePile = new Stack<PlayingCard>();
	}

	/**
	 * Gets the active pile
	 * @return active pile
	 */
	public ArrayList<PlayingCard>getActivePile()
	{
		ArrayList<PlayingCard> pile = new ArrayList(Arrays.asList(this.activePile.toArray()));
		Collections.reverse(pile);
		return pile;
	}

	/**
	 * Adds a card to the top of the pile
	 */
	public void addCard(PlayingCard card) {
		this.activePile.push(card);
	}
	
	/**
	 * Removes a card at the top of the pile
	 */
	public void removeCard() {
		if(activePile.size() != 0) {
			this.activePile.pop();
		}

	}

	/**
	 * Returns the entire pile to the player's hand so it can be counted
	 * @param hand
	 */
	public void returnPile(ArrayList<PlayingCard> hand) {
		hand.addAll( new ArrayList(Arrays.asList(this.activePile.toArray())));
		hand.addAll(new ArrayList(Arrays.asList(this.previousPile.toArray())));
		this.activePile = new Stack<PlayingCard>(); //empty out the pile
		this.previousPile = new Stack<PlayingCard>();
	}


	/**
	 * Used to print out the pile. Java iterates over stacks in the wrong order so we
	 * have to do it manually. Kind of negates the whole value of using Stack...
	 */
	public String toString()
	{
		String pileInfo = "";
		pileInfo += "Active Pile\n";
		for (int i = activePile.size() -1; i >= 0; i--)
		{
			pileInfo += activePile.get(i).toString() + "\n";
		}
		pileInfo += "Previous Pile\n";
		for (int i = previousPile.size() -1; i >= 0; i--)
		{
			pileInfo += previousPile.get(i).toString() + "\n";
		}
		return pileInfo;
	}
}
