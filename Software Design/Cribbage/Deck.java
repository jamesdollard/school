import java.util.ArrayList;
import java.util.Collections;

/**
 * Defines a deck of cards. Allows decks to be smaller for testing purposes.
 * @author jburge
 *
 */
public class Deck {
	private ArrayList<PlayingCard> deck;
	private int deckSize;

	/**
	 * Constructor
	 * @param deckSize - used to create smaller than regulation decks
	 */
	public Deck(int deckSize)
	{
		deck = new ArrayList<PlayingCard>();
		this.deckSize = deckSize;
		this.initializeDeck();
	}

	/**
	 * Used to get how many cards are in the deck
	 * @return
	 */
	public int size()
	{
		return deck.size();
	}

	/**
	 * Used to add a suit of cards to the deck
	 * @param suit
	 */
	public void addSuit(String suit)
	{
		//Normally we'd use PlayingCard.MAX but we want the option of smaller decks
		for (int i = PlayingCard.MIN; i <= deckSize; i++)
		{
			PlayingCard newCard = new PlayingCard(suit, i);
			deck.add(newCard);
		}
	}

	/**
	 * Initializes a deck of cards with all four suits
	 */
	public  void initializeDeck()
	{
		addSuit(PlayingCard.CLUBS);
		addSuit(PlayingCard.DIAMONDS);
		addSuit(PlayingCard.HEARTS);
		addSuit(PlayingCard.SPADES);
		Collections.shuffle(deck);
	}

	/**
	 * Returns a card to the deck - used when undoing commands
	 * @param card - the card that we are returning
	 */
	public void returnCard(PlayingCard card)
	{
		deck.add(card);
	}

	/**
	 * Draws a card from the top of the deck. Assumes the deck has been shuffled!
	 * @return - the card that has been drawn
	 */
	public PlayingCard draw()
	{
		PlayingCard card = deck.get(0);
		deck.remove(0);
		return card;
	}
}
