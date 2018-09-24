/**
 * Testcases for the Pay Station system.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 


 TODO Tests
	1-Call to empty returns the total amount entered. 
	3-Canceled entry does not add to the amount returned by empty. 
	2-Call to empty resets the total to zero. 
	4-Call to cancel returns a map containing one coin entered. 
	5-Call to cancel returns a map containing a mixture of coins entered.
        6-CoinMap never null
	7-Call to cancel returns a map that does not contain a key for a coin not entered. 
	8-Call to cancel clears the map. 
	9-Call to buy clears the map.  

  
 */
package paystation.domain;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class PayStationImplTest {

    PayStation ps;
    @Before
    public void setup() {
        ps = new PayStationImpl();
    }
    

//	1)Call to empty returns the total amount entered
    @Test
    public void emptyReturnsTotalonSingle() throws IllegalCoinException{
	ps.addPayment(5);
	ps.buy();
	assertEquals(5,ps.empty());
    }

    @Test
    public void emptyReturnsTotalOnMulti() throws IllegalCoinException{
	ps.addPayment(5);
	ps.addPayment(25);
	ps.addPayment(10);
	ps.buy();
	assertEquals(40,ps.empty());
    }    
    

//	3)Canceled entry does not add to the amount returned by empty
    @Test
    public void CanceledOrdersDontCount() throws IllegalCoinException{
	ps.addPayment(5);
	ps.addPayment(25);
	ps.addPayment(10);
	ps.buy();
	ps.addPayment(5);
	ps.cancel();
	assertEquals(40,ps.empty());
    }    
    

//	2)Call to empty resets the total to zero. 
    @Test
    public void emptyResetsTotalToZero() throws IllegalCoinException{
	ps.addPayment(5);
	ps.addPayment(25);
	ps.addPayment(10);
	ps.buy();
	int total = ps.empty();
	assertEquals(0,ps.empty());
    }

//	4)Call to cancel returns a map containing one coin entered. 
    @Test
    public void cancelMapReturnsOneCoin() throws IllegalCoinException{
        ps.addPayment(5);
        ps.addPayment(10);
        Map<Integer,Integer> coins = ps.cancel();
        assertEquals(Integer.valueOf(1), coins.get(5));
    }
     

//	5)Call to cancel returns a map containing a mixture of coins entered. 
    @Test
    public void cancelMapReturnsMultiCoins() throws IllegalCoinException{
	ps.addPayment(5);
	ps.addPayment(10);
	ps.addPayment(10);
	ps.addPayment(25);
	Map<Integer,Integer> coins = ps.cancel();
	assertEquals(Integer.valueOf(1),coins.get(25));
	assertEquals(Integer.valueOf(2),coins.get(10));
	assertEquals(Integer.valueOf(1),coins.get(5));
    }
    
 
 //  6) Map returned is never null even if no coins entered
    @Test
    public void coinMapNeverNull() throws NullPointerException{
	Map<Integer,Integer> coins = ps.cancel();
        coins = ps.cancel();
	if(coins==null)
	    fail("CoinMap is null, failfailfail");
    }
    
// 	7)Call to cancel returns a map that does not contain a key for a coin not entered. 
    @Test
    public void coinMapContainsOnlyKeysInPlay() throws IllegalCoinException{
	ps.addPayment(10);
	ps.addPayment(10);
	ps.addPayment(25);
	Map<Integer,Integer> coins = ps.cancel();
	if(coins.containsKey(5))
	    fail("Map contains key for out of play coin, fail.");
    }
    
// 	8) Call to cancel clears the map. 
    @Test
    public void cancelEmptiesMap() throws IllegalCoinException{
        ps.addPayment(10);
	ps.addPayment(10);
	ps.addPayment(25);
	Map<Integer,Integer> coins = ps.cancel();
        coins = ps.cancel();
        if(coins.containsKey(10))
            fail("Cancel didn't empty map, key 10");
        if(coins.containsKey(25))
            fail("Cancel didn't empty map, key 25");
    }

//	9) Call to buy clears the map. 
    @Test
    public void buyEmptiesInputsMap() throws IllegalCoinException{
        ps.addPayment(10);
	ps.addPayment(10);
	ps.addPayment(25);
        ps.buy();
	Map<Integer,Integer> coins = ps.cancel();
        if(coins.containsKey(10))
            fail("Buy didn't empty map, key 10");
        if(coins.containsKey(25))
            fail("Buy didn't empty map, key 25)");
    }
    //////////////////////////PreExisting Tests ////////////////////////////////
    /**
     * Entering 5 cents should make the display report 2 minutes parking time.
     */
    @Test
    public void shouldDisplay2MinFor5Cents()
            throws IllegalCoinException {
        ps.addPayment(5);
        assertEquals("Should display 2 min for 5 cents",
                2, ps.readDisplay());
    }

    /**
     * Entering 25 cents should make the display report 10 minutes parking time.
     */
    @Test
    public void shouldDisplay10MinFor25Cents() throws IllegalCoinException {
        ps.addPayment(25);
        assertEquals("Should display 10 min for 25 cents",
                10, ps.readDisplay());
    }

    /**
     * Verify that illegal coin values are rejected.
     */
    @Test(expected = IllegalCoinException.class)
    public void shouldRejectIllegalCoin() throws IllegalCoinException {
        ps.addPayment(17);
    }

    /**
     * Entering 10 and 25 cents should be valid and return 14 minutes parking
     */
    @Test
    public void shouldDisplay14MinFor10And25Cents()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals("Should display 14 min for 10+25 cents",
                14, ps.readDisplay());
    }

    /**
     * Buy should return a valid receipt of the proper amount of parking time
     */
    @Test
    public void shouldReturnCorrectReceiptWhenBuy()
            throws IllegalCoinException {
        ps.addPayment(5);
        ps.addPayment(10);
        ps.addPayment(25);
        Receipt receipt;
        receipt = ps.buy();
        assertNotNull("Receipt reference cannot be null",
                receipt);
        assertEquals("Receipt value must be 16 min.",
                16, receipt.value());
    }

    /**
     * Buy for 100 cents and verify the receipt
     */
    @Test
    public void shouldReturnReceiptWhenBuy100c()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(25);
        ps.addPayment(25);

        Receipt receipt;
        receipt = ps.buy();
        assertEquals(40, receipt.value());
    }

    /**
     * Verify that the pay station is cleared after a buy scenario
     */
    @Test
    public void shouldClearAfterBuy()
            throws IllegalCoinException {
        ps.addPayment(25);
        ps.buy(); // I do not care about the result
        // verify that the display reads 0
        assertEquals("Display should have been cleared",
                0, ps.readDisplay());
        // verify that a following buy scenario behaves properly
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals("Next add payment should display correct time",
                14, ps.readDisplay());
        Receipt r = ps.buy();
        assertEquals("Next buy should return valid receipt",
                14, r.value());
        assertEquals("Again, display should be cleared",
                0, ps.readDisplay());
    }

    /**
     * Verify that cancel clears the pay station
     */
    @Test
    public void shouldClearAfterCancel()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.cancel();
        assertEquals("Cancel should clear display",
                0, ps.readDisplay());
        ps.addPayment(25);
        assertEquals("Insert after cancel should work",
                10, ps.readDisplay());
    }
}
