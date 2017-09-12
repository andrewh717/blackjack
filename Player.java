//Author: Andrew Hernandez
import java.util.*;
public class Player{
  //Fields
  public int totalValue;
  public double bankroll;

  //Constructor
  public Player(double initialBankroll){
    this.totalValue = 0;
    this.bankroll = initialBankroll;
  }
  //Check to see if the player has money in their bankroll
  public boolean bankrupt(){
    if(this.bankroll == 0){
      return true;
    }
    else{
      return false;
    }
  }
}
