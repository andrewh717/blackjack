//Author: Andrew Hernandez
//This class represents the deck of cards from which cards are dealt to players.
import java.util.Random;
public class Deck{
  //define fields here
  private Card[] deckOfCards;
  private int numCards;

  //This constructor builds a deck or decks of cards.
  public Deck(int numDecks){
    this.numCards = 52 * numDecks;
    this.deckOfCards = new Card[this.numCards];
    int cardIndex = 0;
    //Creating decks
    for(int i = 0; i < numDecks; i++){
      //Creating suits
      for(int s = 1; s <= 4; s++){
        //Creating faces
        for(int f = 1; f <= 13; f++){
          this.deckOfCards[cardIndex] = new Card(s, f);
          cardIndex++;
        }
      }
    }
  }


  //This method takes the top card off the deck and returns it.
  public Card deal(){
    Card top = this.deckOfCards[0];
    for(int i = 1; i < this.numCards; i++){
      this.deckOfCards[i-1] = this.deckOfCards[i];
    }
    this.deckOfCards[this.numCards-1] = null;
    this.numCards--;
    return top;
  }


  //This method returns true if there are no more cards to deal, false otherwise
  public boolean isEmpty(){

    if(numCards <= 0){
      return true;
    }
    else{
      return false;
    }
  }

  //This method puts the deck int some random order
  public void shuffle(){
    Random rand = new Random();
    Card temp;
    int a;
    for(int i = 0; i < this.numCards; i++){
      a = rand.nextInt(this.numCards);
      temp = this.deckOfCards[i];
      this.deckOfCards[i] = this.deckOfCards[a];
      this.deckOfCards[a] = temp;
    }
  }
}
