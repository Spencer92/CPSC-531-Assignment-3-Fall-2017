package hairPlace;
import java.io.IOException;

import arrivalRate.*;


public class HairPlace 
{
	public static final int seedOne = 10009;
	public static final int seedTwo = 20089;
	public static final int seedThree = 30071;
	private static final int amountOfTimes = 10000;
	
	
	public HairPlace(String [] args) throws IOException
	{
		Barber fred = new Barber(3.0, seedOne);
		Barber george = new Barber(2.0, seedTwo);
		Customer customers = new Customer(4.0, seedThree);
		
		fred.getTimes(amountOfTimes);
		george.getTimes(amountOfTimes);
		customers.getTimes(amountOfTimes);
		
		fred.printResults("Fred");
		george.printResults("George");
		customers.printResults("customers");
		
	}
	
	public static void main(String [] args) throws IOException
	{
		new HairPlace(args);
	}
}
