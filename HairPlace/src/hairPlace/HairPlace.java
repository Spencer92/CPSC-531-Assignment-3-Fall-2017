package hairPlace;
import java.io.IOException;
import java.util.Random;

import arrivalRate.*;


public class HairPlace 
{
	public static final int SEED_ONE = 10009;
	public static final int SEED_TWO = 20089;
	public static final int SEED_THREE = 30071;
	public static final int SEED_FOUR = 40093;
	
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
	
	public void getTimes(double [] chairs, double [] barbers, double lambda)
	{
		double prevArrival;
		double currArrival;
		int availableChair = 0;
		int customersLost = 0;
		int availableBarber = 0;
		double arrivalTime;
		double [] serviceTimes = new double[barbers.length];
		double [] waitingTimes = new double[chairs.length];
		double [] barberServiceTimes = new double[barbers.length];
		boolean nextBool;
		
		Random randSeedOne = new Random(SEED_ONE); //used for George, initially
		Random randSeedTwo = new Random(SEED_TWO); //used for Fred, initially
		Random randSeedThree = new Random(SEED_THREE); //used for customers, initially
		Random randSeedFour = new Random(SEED_FOUR); //used for booleans, initially;
	
		for(int i = 0; i < chairs.length; i++)
		{
			chairs[i] = 0;
		}
		
		
//		for(int i = 0; i < AMOUNT_OF_TIMES; i++)
//		{
		nextBool = randSeedFour.nextBoolean();
		if(nextBool && barbers.length > 1)
		{
			barbers[0] = distribution(lambda, randSeedThree.nextDouble());
			barbers[1] = distribution(lambda, randSeedThree.nextDouble());
		}
		else if(!nextBool && barbers.length > 1)
		{
			barbers[1] = distribution(lambda, randSeedThree.nextDouble());
			barbers[0] = distribution(lambda, randSeedThree.nextDouble());
		}
		else
		{
			barbers[0] = distribution(lambda, randSeedThree.nextDouble());
		}
		
		for(int i = 0; i < AMOUNT_OF_TIMES; i++)
		{
			
			//check if there is occupency in barber
			//if there isn't, check if occupency in chair
			//if there isn't, leave
			
			
		}
		
	
//		}
		
		
		
		
		
/*		for(int i = 0; i < chairs.length; i++)
		{
			chairs[i] = getValue(randSeedThree, 0);
		}
		
		barbers[0] = getValue(randSeedOne,0);
		if(barbers.length > 1)
		{
			barbers[1] = getValue(randSeedTwo,0);
		}*/
		
		
		
		
		
/*		for(int i = 0; i < AMOUNT_OF_TIMES; i++)
		{
			if(!allOccupied(chairs))
			{
				//find chair
				
				
				
				
				/*				chairs[availableChair] = false;
				goToNextChair(availableChair,chairs);
				
				if(!allOccupied(barbers))
				{
					randSeedFour.nextInt(barbers.length);
					goToNextChair(availableBarber, barbers);
				}*/
				
				//if no chair
		
				//leave
		
				//wait for barber
		
				//if barber available
		
				//go to barber
		
				//chair now unoccupied
		
				//barber does work
/*			}
			else
			{
				customersLost++;
			}
		}*/
		
	}
	
	private double distribution(double lambda, double randomNumber)
	{
		return ((1.0/lambda) * Math.log(1.0-randomNumber))*-1;
	}
	
	double getValue(Random rand, double prevValue)
	{
		double val = rand.nextDouble();
		val += prevValue;
		return val;
	}
	
	boolean allOccupied(boolean [] chairs)
	{
		for(int i = 0; i < chairs.length; i++)
		{
			if(chairs[i])
			{
				return true;
			}
		}
		return false;
	}
	
	int goToNextChair(int availableChair, boolean [] chairs)
	{
		for(int i = availableChair; i < chairs.length; i++)
		{
			if(chairs[i])
			{
				return i;
			}
		}
		return 0;
	}
	
	double shortestWait(double [] barbers)
	{
		double lowest = barbers[barbers.length-1];
		for(int i = 0; i < barbers.length-1; i++)
		{
			if(barbers[i] < lowest)
			{
				lowest = barbers[i];
			}
		}
		return lowest;
	}
	
	public static void main(String [] args) throws IOException
	{
		new HairPlace(args);
	}
}
