import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

/**
 * This is the main class for a Cribbage game. See http: https://en.wikipedia.org/wiki/Rules_of_cribbage#The_end
 * @author jburge
 *
 */
public class Cribbage {

	public static final int START_CARDS = 6; //six card cribbage
	public static final int DECK_SIZE = PlayingCard.MAX;   //set to PlayingCard.MAX for a full deck
	public static final  int CHOOSE_CARD = 1;
	public static final int UNDO_MOVE = 2;
	public static final int CHANGE_STRATEGY = 3;

	public static final int WINNING_TOTAL = 121;
	public static final int ROUND_OVER = 31;
	public static final int FIFTEEN = 15; //ok... this is silly!
	public static final int NIBS = 2;

	private Deck deck;
	private Player human = new Player("Human");
	private Player computer = new Player("Computer");
	private ArrayList<PlayingCard> crib = new ArrayList<PlayingCard>();
	private PlayingCard starter;
	private int pileTotal = 0;
	
	private PlayingStrategy randomStrategy = new RandomStrategy();
	private PlayingStrategy minMaxStrategy = new MinMaxStrategy();
	private PlayingStrategy playingStrategy = randomStrategy;
	
	Stack<Command> commands = new Stack<Command>(); 

	/**
	 * Prints out a string declaring the winner. Exists so we aren't sprinkling the code with
	 * print messages
	 * @param winner - name of the winner
	 */
	private  void declareWinner(String winner)
	{
		System.out.println(winner + " has won!");
	}

	/**
	 * Our main menu for the player options. 
	 * @return the valid menu choice
	 */
	private  int playerMenu()
	{
		boolean valid = false;
		Scanner in = new Scanner(System.in);
		int selection = 1;
		while (!valid)
		{
			System.out.println("\nChoose the player option: ");
			System.out.println("1: Continue Play");
			System.out.println("2: Undo the last move");
			System.out.println("3: Change the opponent's strategy");
			System.out.print("> ");
			try {
				selection = in.nextInt();

				in.nextLine();
				if ((selection > 0) && (selection <= 3))
				{
					valid = true;
				}
			}
			//this will catch the mismatch and prevent the error
			catch(InputMismatchException ex)
			{
				//still need to gobble up the end of line
				in.nextLine();
			}
			if (!valid)
			{
				System.out.println("Invalid entry -- enter a number between 1 and 3");
			}
		}
		return selection;
	}

	/**
	 * Find out which card the user wants to play/lay down/ ...
	 * @param limit
	 * @return
	 */
	public PlayingCard chooseCard(int limit)
	{
		int cardWanted = 1;
		boolean valid = false;
		Scanner in = new Scanner(System.in);
		int selection = 1;
		ArrayList<PlayingCard> playerHand = human.getHand();
		int max = playerHand.size();
		PlayingCard choice = null;
		while (!valid)
		{
			for (PlayingCard card : playerHand)
			{
				if (card.value() <= limit)
				{
					System.out.println("" + cardWanted + ": " + card.toString() );
					cardWanted++;
				}
			}
			//Were there playable cards?
			if (cardWanted == 1)
			{
				//It's a GO
				return null;
			}
			try {
				selection = in.nextInt();

				in.nextLine();
				if ((selection > 0) && (selection < cardWanted))
				{
					valid = true;
					choice = playerHand.get(selection-1);
					return choice;

				}
			}
			//this will catch the mismatch and prevent the error
			catch(InputMismatchException ex)
			{
				//still need to gobble up the end of line
				in.nextLine();
			}
			if (!valid)
			{
				System.out.println("Invalid entry -- enter a number between 1 and " + max);
				cardWanted = 1;
			}
		}
		return null;
	}

	/**
	 * This prints out the pile - this involves combining the piles for the two players. This was
	 * done because it was hard to follow what was where
	 * @param currentPlayer - the player who just played a card - this should be the top card on the pile
	 * @param otherPlayer - the other player
	 */
	private  void printPile(Player currentPlayer, Player otherPlayer)
	{
		ArrayList<PlayingCard> pile = Score.combinePile(currentPlayer.getActivePile(), otherPlayer.getActivePile());
		System.out.println("Pile:");
		System.out.println(pile);
	}
	



	/**
	 * This method finds out what the human wants to do. If they want, they can change strategy or
	 * undo but eventually this method needs to return the next card they want to play 
	 * @return the next card
	 */
	private PlayingCard nextHumanCard()
	{
		boolean done = false;
		PlayingCard nextCard = null;
		//First, figure out what the player should do. 
		while (!done)
		{
			int choice = playerMenu();
			switch(choice) {
			case CHOOSE_CARD:
				nextCard = chooseCard(ROUND_OVER - pileTotal);
				done = true;
				break;
			case UNDO_MOVE:
				
				

				if(commands.size() == 0) {
					System.out.println("No more player commands to undo.");
				}
				else
				{
					
					commands.pop().undoCommand();
					if(commands.size() == 0) {
						System.out.println("No more player commands to undo.");
					}else {
						commands.pop().undoCommand();
					}

					
					/*
					for(int i = 0; i < commandsSinceLastPrompt; i++) {
						commands.pop().undoCommand();
					}
					*/
						
				}

				
				
					System.out.println();
					System.out.println("Human:");
					System.out.println("Points = " + human.getPoints());
					System.out.println(human.getHand());
					
					System.out.println();
					System.out.println("Computer:");
					System.out.println("Points = " + computer.getPoints());
					System.out.println(computer.getHand());
					
					System.out.println();
					System.out.println("Pile total = " + pileTotal);
					printPile(human, computer);
					
					
				
				break;
			case CHANGE_STRATEGY:
				if(playingStrategy.equals(randomStrategy)) 
				{
					playingStrategy = minMaxStrategy;
					System.out.println("Successfully switched to min/max strategy");
				}
				else 
				{
					playingStrategy = randomStrategy;
					System.out.println("Successfully switched to random strategy");
				}
				break;	
			}
		}
		return nextCard;

	}

	/**
	 * Get the next card for the computer player
	 * @return - the playing card
	 */
	private PlayingCard nextComputerCard()
	{
		
		return playingStrategy.chooseCard(pileTotal, computer);
		
	}
	
	/**
	 * Adds to pile total
	 * @param additionAmount
	 */
	public void addToPileTotal(int additionAmount) {
		pileTotal += additionAmount;
	}
	
	/**
	 * Subtracts from pile total
	 * @param subtractionAmount
	 */
	public void subtractFromPileTotal(int subtractionAmount) {
		pileTotal -= subtractionAmount;
	}
	
	/**
	 * Gets the total pile amount
	 * @return pile total
	 */
	public int getPileTotal() {
		return pileTotal;
	}
	
	/**
	 * Sets a new total pile amount
	 * @param newTotal
	 */
	public void setPileTotal(int newTotal) {
		pileTotal = newTotal;
	}

	/**
	 * Executes play for a specific player (human or computer)
	 * @param currentPlayer
	 * @param otherPlayer
	 * @return won - return True if this play made the player win
	 */
	private boolean playHand(Player currentPlayer, Player otherPlayer)
	{
		PlayingCard cardPlayed;
		ArrayList<Score> scores = new ArrayList<Score>();
		
		boolean isComputer;
		if(currentPlayer == computer) {
			isComputer = true;
		}else {
			isComputer = false;
		}

		//Find the card we want to play
		if (isComputer)
		{
			cardPlayed = nextComputerCard();
		}
		else
		{
			cardPlayed = nextHumanCard();
		}
		
		//Check to see if it's a go
		if (cardPlayed != null)
		{
			
			Command playCommand = new PlayCommand(this, currentPlayer, otherPlayer, cardPlayed, isComputer);
			scores = playCommand.doCommand();
			commands.push(playCommand);
			
			System.out.println(currentPlayer);
			System.out.println("\nPile total = " + pileTotal);
			printPile(currentPlayer, otherPlayer);
/*
			currentPlayer.playCard(cardPlayed);
			scores = Score.scorePile(currentPlayer.getActivePile(), otherPlayer.getActivePile());
			if (scores == null)
			{
				scores = new ArrayList<Score>();
			}
			//check to see if opponent passed last time
			if (otherPlayer.hasPassed())
			{
				scores.add(new Score(2, "two for the go"));
			}
			//add up all the points and award them to the player; also save them!
			int pointsAwarded = Score.addScores(scores);
			currentPlayer.addPoints(pointsAwarded);
			pileTotal += cardPlayed.value();
			System.out.println(currentPlayer);
			System.out.println("\nPile total = " + pileTotal);
			printPile(currentPlayer, otherPlayer);
*/

		}
		else
		{
			System.out.println("It's a Go");
			

			
			Command goCommand = new GoCommand(this, currentPlayer, otherPlayer);
			scores = goCommand.doCommand();
			commands.push(goCommand);

			System.out.println(currentPlayer);
		}
		if (!scores.isEmpty())
		{
			System.out.println(currentPlayer.getName() + " gets: ");
			System.out.println(scores);
		}
		return (currentPlayer.getPoints() >= WINNING_TOTAL);



	}

	/**
	 * Checks to see if there are cards left for either player
	 * @param firstPlayer
	 * @param secondPlayer
	 * @return true if either player has any cards left
	 */
	private boolean cardsLeft(Player firstPlayer, Player secondPlayer)
	{
		return ((firstPlayer.numCards() > 0) || (secondPlayer.numCards() > 0));
	}

	/**
	 * Plays a game of cribbage!
	 */
	public void playGame()
	{

		boolean isWinner = false;
		System.out.println("Welcome to Cribbage!");
		Player nonDealer;
		Player dealer;
	
		Random randomGen = new Random();
		//Figure out who deals first - player or computer
		if (randomGen.nextInt(2) == 0)
		{
			nonDealer = human;
			dealer = computer;
		}
		else
		{
			nonDealer = computer;
			dealer = human;
		}

		System.out.println("\nDealer is " + dealer.getName());

		//Loop for the game - need to deal out new hands each round
		while (!isWinner)
		{

			//Dealing out new hands - always start with a freshly shuffled deck
			
			commands = new Stack<Command>(); 
			deck = new Deck(DECK_SIZE);
			human.resetForNewHand();
			computer.resetForNewHand();
			pileTotal = 0;
			crib = new ArrayList<PlayingCard>();

			//Deal initial cards to each player
			for (int i=0; i < Cribbage.START_CARDS; i++)
			{
				human.draw(deck);
				computer.draw(deck);
			}

			System.out.println(human);
			System.out.println(computer);


			//Each player, human and computer, needs to choose two cards to go in the crib
			System.out.println("\nChoosing cards for the crib");
			
			PlayingCard next = chooseCard(ROUND_OVER);
			crib.add(next);
			human.removeCard(next);
			next = chooseCard(ROUND_OVER);
			crib.add(next);
			human.removeCard(next);

			//TODO: You will probably not want to use the same strategy for crib vs. play!
			System.out.println("\nComputer is choosing cards for the crib");
			next = nextComputerCard();
			crib.add(next);
			computer.removeCard(next);
			next = nextComputerCard();
			crib.add(next);
			computer.removeCard(next);

			System.out.println("\nFinal contents of crib:");
			System.out.println(crib);

			//Flip over the top card, if a Jack, dealer gets two points.
			starter = deck.draw();
			System.out.println("\nStarter = " + starter.toString());
			if (starter.equalsType(PlayingCard.JACK))
			{
				dealer.addPoints(2);
				System.out.println("Points to dealer: " + dealer.getName());
				System.out.println("Two for his nibs");
			}

			//While we still have cards left to play...
			while (cardsLeft(dealer, nonDealer) && (!isWinner))
			{
				if (!nonDealer.hasPassed())
				{
					isWinner = playHand(nonDealer, dealer);
					if ((!isWinner) && !(dealer.hasPassed()))
					{
						isWinner = playHand(dealer, nonDealer);
					}
				}
				else if (!dealer.hasPassed())
				{
					//Need to keep playing cards until out!
					isWinner = playHand(dealer, nonDealer);
				}


				if (!isWinner)
				{
					if (dealer.hasPassed() && nonDealer.hasPassed())
					{
						dealer.newPile();
						nonDealer.newPile();
					}
				}
			}

			//Return the cards from the pile to the hand for scoring
			nonDealer.returnPile();

			//Score the non-dealer's hand
			ArrayList<Score> scores = Score.scoreHand(nonDealer.getHand(), starter);
			if (!scores.isEmpty())
			{
				System.out.println(nonDealer.getName() + " hand has:");
				System.out.println(scores);
				System.out.println();
			}

			int points = Score.addScores(scores);
			nonDealer.addPoints(points);

			//Have they won?
			if (nonDealer.getPoints() >= WINNING_TOTAL)
			{
				declareWinner(nonDealer.getName());
				isWinner = true;
			}
			//Score dealer's hand
			else
			{
				//Return the cards to their hand
				dealer.returnPile();
				scores = Score.scoreHand(dealer.getHand(), starter);
				if (!scores.isEmpty())
				{
					System.out.println(dealer.getName() + " hand has");
					System.out.println(scores);
					System.out.println();
				}
				points = Score.addScores(scores);
				//Score the crib too
				scores = Score.scoreHand(crib, starter);
				if (!scores.isEmpty())
				{
					System.out.println(dealer.getName() + " crib has");
					System.out.println(scores);
					System.out.println();
				}
				points += Score.addScores(scores);
				dealer.addPoints(points);

				if (dealer.getPoints() >= WINNING_TOTAL)
				{
					declareWinner(dealer.getName());
					isWinner = true;
				}

			}

			if (!isWinner)
			{

				//Swap players
				Player temp = nonDealer;
				nonDealer = dealer;
				dealer = temp;
				System.out.println("New dealer is: " + dealer.getName());
			}

		} 
	}
	/**
	 * Main Program
	 * @param args
	 */
	public static void main(String[] args) {
		Cribbage cribbageGame = new Cribbage();
		cribbageGame.playGame();
	}


}
