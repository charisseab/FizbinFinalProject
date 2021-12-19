import java.util.*;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;

public class MainApplication {
	public static void main(String[] args) {
		System.out.println("Let's Play Cards!");
		TheTable.PlayCards();
	}
}

class TheTable {

	// CardDeck deck = new CardDeck();
	
	public static void PlayCards() {
		CardDeck.GenerateCardDeck();
		// I now have a card deck

		GamePlayers p = new GamePlayers();
		p.GeneratePlayers();
		// I now have Card Players with Hands of Cards: use the Debugger to verify that you can see this
		
		// my Players have a calcuatel card value Method
		
		// your To Do: For Assignment 8: Is to out WHO the winner player is, based on the cards
		// that the Dealer gave them
		// We will look at additional code formulations later to see how to take and give new cards 
		// in additional rounds of Play
		Round round_1 = new Round();
		round_1.EvaluateHand(p);
		round_1.CardExchange(p);
		
		DeclareWinner(p);
		
	}
	
	public static void DeclareWinner(GamePlayers p) {
		int highestValue = 0;
		Player winner = null;
		System.out.println("\n\ncards left: " + CardDeck._cardDeck.size() + "\t " + CardDeck._cardDeck + "\n\n");;
		for (var i = 0; i < p.players.size(); i ++) {
			int HandValue = p.players.get(i).CalculateHandValue();
			Player playerName =  p.players.get(i);
			System.out.println(playerName.PlayerName + " and their hand value: " +  HandValue);
			if (highestValue == 0 || highestValue < HandValue ) {
				highestValue = HandValue;
				winner = playerName;
			}
		}
		System.out.println("\n\nThe winner is " + winner.PlayerName + " with a winning hand value of " + highestValue );
	}
}

class CardDeck {
	public static ArrayList<Card> _cardDeck = new ArrayList<Card>();
	static String[] suites = { "Clubs", "Diamonds", "Hearts", "Spades" };

	public static void GenerateCardDeck() {
		String[] suites;
		int[] cardValue;
		suites = new String[4];
		suites[0] = "Clubs";
		suites[1] = "Diamonds";
		suites[2] = "Hearts";
		suites[3] = "Spades";

		cardValue = new int[13];
		cardValue[0] = 2;
		cardValue[1] = 3;
		cardValue[2] = 4;
		cardValue[3] = 5;
		cardValue[4] = 6;
		cardValue[5] = 7;
		cardValue[6] = 8;
		cardValue[7] = 9;
		cardValue[8] = 10;
		cardValue[9] = 11;
		cardValue[10] = 12;
		cardValue[11] = 13;
		cardValue[12] = 14;

		for (int i = 0; i < suites.length; i++) {
			for (int j = 0; j < cardValue.length; j++) {
				CardDeck._cardDeck.add(new Card(suites[i], cardValue[j]));
			}
		}

		// shuffle the card deck
		Collections.shuffle(CardDeck._cardDeck);
	}
}

class Card {

	private String suite;
	private int suitevalue; 
	private int cardvalue;
	// #TODO: Add to the this Class a VALUE Data Attribute
	// set the VALUE of this Card based on Suite and Card Value

	public Card(String _suite, int _cardvalue) {
		this.suite = _suite;
		this.cardvalue = _cardvalue;
		calculateSuiteValue(this.suite);
	}
	
	private void calculateSuiteValue(String _suite) {
		if (_suite.equals("Clubs")) {
			this.suitevalue = this.cardvalue  * 1; // multiplier based on suite
		} else if (_suite.equals("Diamonds")) {
			this.suitevalue  = this.cardvalue * 2;
		} else if (_suite.equals("Hearts")) {
			this.suitevalue  = this.cardvalue * 3;
		} else {
			this.suitevalue  = this.cardvalue * 4;
		}
		System.out.println("this.cardvalue: " + this + "\t this.suitevalue: " + this.suitevalue + "\n");
		
	} 
	
	public int getCardValue() {
		return this.cardvalue;
	} 
	
	public int getSuiteValue() {
		return this.suitevalue;
	} 
	
	public String getCardSuite() {
		return this.suite;
	} 

	public String toString() {
		return this.suite + " " + this.cardvalue;
	}
}

class GamePlayers {
	// contains our list of players
	public GamePlayers() {

	}

	public static ArrayList<Player> players = new ArrayList<Player>();

	public static void GeneratePlayers() {
		// for (int m = 0; m < 4; m++) {
			players.add(new Player("Bill"));
			players.add(new Player("Mary"));
			players.add(new Player("Steve"));
			players.add(new Player("Susan"));
		//} // removed, otherwise, each player is present 4 times resulting in 16 number of players
	}

}

class Player {
	public Player(String playerName) {
		PlayerName = playerName;
		setOfCards = new Hand();
//		playersCardSet = setOfCards.IssueHand();
	}

	String PlayerName;
	Hand setOfCards;
	Card[] playersCardSet = new Card[5];
	
	public int CalculateHandValue() {
		int HandValue = 0; // always reset to 0
		for (var i = 0; i < playersCardSet.length; i++) {
			int suiteValue = playersCardSet[i].getSuiteValue();
			HandValue +=	suiteValue;
			System.out.print(playersCardSet[i].toString() + ", with suiteValue: " + suiteValue + "\n" );
		}
		System.out.println("\nCalculateHandValue: "+ HandValue );
		return HandValue;
	}
	
	public void setHand() {  // issueHand() instead of during player creation
		playersCardSet = setOfCards.IssueHand();
	}
	
	public void returnCards() {
		
	}
}

class Hand {
	// the Hand is the cards the player holds

	Card[] PlayersHand = new Card[5];

	public Hand() {

	}

	public Card[] IssueHand() {
		for (int j = 0; j < 5; j++) {
			PlayersHand[j] = (CardDeck._cardDeck).remove(j); 
			// used .remove() instead of .get() so that the cards in the player's hand are not in the deck
			// and no duplicates will occur
		}
		return PlayersHand;
	}

}

class Round{
	int roundNumber=0;
	private int highestScore ;
	ArrayList<Player> RoundWinners; // 
	
	
	public Round() {
		this.roundNumber++;
	}
	
	public void EvaluateHand(GamePlayers p) {
		System.out.println("Round #" + this.roundNumber);
		for (var i = 0; i < p.players.size(); i ++) {
			p.players.get(i).setHand();
			int playerHandValue = p.players.get(i).CalculateHandValue();
			String playerName =  p.players.get(i).PlayerName;
			System.out.println(playerName + " has " + playerHandValue + " points.\n\n");
		}
	}
	
	public void CardExchange(GamePlayers p) {
		/* In each round: Player may give 1, 2, or 3 cards  back to the Table (dealer) : and receive back an equivalent 
		 * number of cards
		*/
		
		
	}
}
