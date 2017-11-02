package hairPlace;
import java.io.IOException;

import arrivalRate.*;


public class HairPlace 
{
	public static final int SEED_ONE = 10009;
	public static final int SEED_TWO = 20089;
	public static final int SEED_THREE = 30071;
	private static final int AMOUNT_OF_TIMES = 10000;
	private static final int GEORGE = 1;
	private static final int FRED = 1;
	
	
	public HairPlace(String [] args) throws IOException
	{
		Barber fred = new Barber(3.0, SEED_ONE);
		Barber george = new Barber(2.0, SEED_TWO);
		Customer customers = new Customer(4.0, SEED_THREE);
		
		fred.getTimes(AMOUNT_OF_TIMES);
		george.getTimes(AMOUNT_OF_TIMES);
		customers.getTimes(AMOUNT_OF_TIMES);
		
		fred.printResults("Fred");
		george.printResults("George");
		customers.printResults("customers");
		
	}
	
	public void getTimes(boolean [] chairs, double [] serviceTimes)
	{
		
	}
	
	public static void main(String [] args) throws IOException
	{
		new HairPlace(args);
	}
}
