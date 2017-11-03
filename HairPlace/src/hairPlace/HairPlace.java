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
	
	public void getTimes(double [] chairs, double [] barbers, double lambda, double mu1, double mu2)
	{
		
		double [] barbersServiceRate = new double[barbers.length];
		double arrival = 0;
		double nextArrival = 0;
		int lostCustomers = 0;
		double lowestVal;
		
		for(int i = 0; i < barbers.length; i++)
		{
			barbers[i] = 0;
			barbersServiceRate[i] = 0;
		}
		
		for(int i = 0; i < chairs.length; i++)
		{
			chairs[i] = 0;
		}
		
		Random randSeedOne = new Random(SEED_ONE); //used for George, initially
		Random randSeedTwo = new Random(SEED_TWO); //used for Fred, initially
		Random randSeedThree = new Random(SEED_THREE); //used for customers, initially
		Random randSeedFour = new Random(SEED_FOUR); //used for booleans, initially;

		
		nextArrival = distribution(lambda, randSeedThree.nextDouble());
		

		if(randSeedFour.nextBoolean())
		{
			barbers[0] = distribution(mu1, randSeedOne.nextDouble());
			barbers[0] += nextArrival;
		}
		else
		{
			barbers[1] = distribution(mu2, randSeedTwo.nextDouble());
			barbers[1] += nextArrival;
		}
		
		nextArrival = distribution(lambda, randSeedThree.nextDouble());
		
		if(barbers[0] == 0)
		{
			barbers[0] = distribution(mu1, randSeedOne.nextDouble());
			barbers[0] += nextArrival;
		}
		else
		{
			barbers[1] = distribution(mu2, randSeedTwo.nextDouble());
			barbers[1] += nextArrival;
		}
		
		for(int i = 0; i < chairs.length; i++)
		{
			chairs[i] = distribution(lambda, randSeedThree.nextDouble());
		}
		
		for(int i = 0; i < AMOUNT_OF_TIMES; i++)
		{
			nextArrival = distribution(lambda, randSeedThree.nextDouble());
			nextArrival += arrival;
			arrival = nextArrival;
			
			
			//if arrived after the last person departed, no wait
			if((nextArrival > (longestWait(chairs) + barbers[0]))
					&&
					(nextArrival > (longestWait(chairs) + barbers[1])))
			{
				if(randSeedFour.nextBoolean())
				{
					barbers[0] = distribution(mu1, randSeedOne.nextDouble());
					barbers[0] += nextArrival;
				}
				else
				{
					barbers[1] = distribution(mu2, randSeedTwo.nextDouble());
					barbers[1] += nextArrival;
				}
			}
			else if((nextArrival > (longestWait(chairs) + barbers[0]))
					&&
					(nextArrival < (longestWait(chairs) + barbers[1])))
			{
				barbers[0] = distribution(mu1, randSeedOne.nextDouble());
				barbers[0] += nextArrival;
			}
			else if((nextArrival < (longestWait(chairs) + barbers[0]))
					&&
					(nextArrival > (longestWait(chairs) + barbers[1])))
			{
				barbers[1] = distribution(mu2, randSeedTwo.nextDouble());
				barbers[1] += nextArrival;
			}
			else if((nextArrival < (longestWait(chairs) + barbers[0]))
				&&
				(nextArrival < (longestWait(chairs) + barbers[1])))
			{
				//If there isn't any seats, then it can't work
				if(nextArrival < shortestWait(chairs))
				{
					lostCustomers++;
				}
				else
				{
					lowestVal = shortestWait(chairs);
					for(int j = 0; j < chairs.length; j++)
					{
						if(lowestVal == chairs[j])
						{
							chairs[j] = lowestVal + nextArrival;
							break;
						}
						
					}
				}
			}
			else
			{
				//Dunno yet
			}
			
			
		}
		
		
		/*		double prevArrival;
		double currArrival;
		int availableChair = 0;
		int customersLost = 0;
		int availableBarber = 0;
		double arrivalTime;
		double [] serviceTimes = new double[barbers.length];
		double [] waitingTimes = new double[chairs.length];
		double [] barberServiceTimes = new double[barbers.length];
		boolean nextBool;
		double nextCustomer;
		int totalDelay = 0;
		int delayAmount;
		
		

		
		
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
		
		for(int i = 0; i < chairs.length; i++)
		{
			chairs[i] = distribution(lambda, randSeedThree.nextDouble());
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
	
	
	
	private double average(double [] chairs)
	{
		int total = 0;
		for(int i = 0; i < chairs.length; i++)
		{
			total += chairs[i];
		}
		return total/chairs.length;
	}
	
	private double distribution(double lambda, double randomNumber)
	{
		return ((1.0/lambda) * Math.log(1.0-randomNumber))*-1;
	}
	
	private double getValue(Random rand, double prevValue)
	{
		double val = rand.nextDouble();
		val += prevValue;
		return val;
	}
	
	private boolean allOccupied(boolean [] chairs)
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
	
	private int goToNextChair(int availableChair, boolean [] chairs)
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
	
	private double shortestWait(double [] barbers)
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
	
	private double longestWait(double [] barbers)
	{
		double highest = barbers[barbers.length-1];
		for(int i = 0; i < barbers.length-1; i++)
		{
			if(barbers[i] > highest)
			{
				highest = barbers[i];
			}
		}
		return highest;
	}
	
	public static void main(String [] args) throws IOException
	{
		new HairPlace(args);
	}
}
