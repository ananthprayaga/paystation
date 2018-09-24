package paystation.domain;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the pay station.
 *
 * Responsibilities:
 *
 * 1) Accept payment; 
 * 2) Calculate parking time based on payment; 
 * 3) Know earning, parking time bought; 
 * 4) Issue receipts; 
 * 5) Handle buy and cancel events.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
public class PayStationImpl implements PayStation {
    static final int[] VALID_COINS = {5,10,25};
    
    private int insertedSoFar;
    private int timeBought;
    private int totalEarnings=0;
    //The only way to get this map is to cancel(), no other execution flows
    private Map<Integer,Integer> coinMap = new HashMap<Integer,Integer>();

    @Override 
    //Implements updateTransaction(value)
    public void addPayment(int coinValue) throws IllegalCoinException {
        //Check if coin is valid
        //if valid update transaction via updateTransaction(coinValue)
        if(validCoin(coinValue))
	    updateTransaction(coinValue);	
	else
	    throw new IllegalCoinException("Invalid coin: " + coinValue);
    }

    @Override
    public int readDisplay() {
        return timeBought; 
    }

    @Override
    public Receipt buy() {
        //reset current transaction values and return reciept object
        Receipt r = new ReceiptImpl(timeBought);
	totalEarnings+=insertedSoFar;
        reset();
        return r;
    }

    @Override
    public Map<Integer,Integer> cancel() {
        //Reset transaction counts and return the coins in play
        Map<Integer,Integer> tempMap = coinMap;
        reset();
        return tempMap;
    }
    
    private void reset(){
        //reset current counts and coinMap
        timeBought = insertedSoFar = 0;
        coinMap=new HashMap<Integer,Integer>();
    }
    
    @Override
    public int empty(){
        //returns the total value of coins entered since the last empty()
	int earnings = totalEarnings;
	totalEarnings=0;
	return earnings;	
    }
    
     /**
     * Private helper function to determine whether the coin inserted is valid denomination
     * @return true if coin is 5,10 or 25, else false
     */
    private boolean validCoin(int currentCoin){	
        //check currentCoin against valid coins
	for(int i=0; i< VALID_COINS.length; i++)
	    if(VALID_COINS[i]==currentCoin)
		return true;
	return false;
    }
    
     /**
     * Private helper function to update transaction details. 
     * Only called once validCoin has been used to check inputs. 
     */
    private void updateTransaction(int coinValue){
        //If the coinMap already contains coinValue then increment with replace()
        //else add it to the map using put()
	if(coinMap.containsKey(Integer.valueOf(coinValue)))
	   coinMap.replace(Integer.valueOf(coinValue), new Integer(coinMap.get(coinValue).intValue()+1));
	else	    
	    coinMap.put(Integer.valueOf(coinValue), Integer.valueOf(1));
	
	insertedSoFar+=coinValue;
	timeBought=insertedSoFar/5*2;
    }
}
