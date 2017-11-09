package hairPlace;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import arrivalRate.*;


public class HairPlace 
{
	public static final int SEED_ONE = 10009;
	public static final int SEED_TWO = 20089;
	public static final int SEED_THREE = 30071;
	public static final int SEED_FOUR = 40093;
	
	private static final int AMOUNT_OF_TIMES = 10000;
	private static final int GEORGE = 0;
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
		double [] barber = new double[2];
		double [] chairs = new double[10];
		double lambda = 4.0;
		double mu1 = 3.0;
		double mu2 = 2.0;
		
		getTimes(chairs,barber,lambda,mu1,mu2);
		
		
	}
	
	public void getTimes(double [] chairs, double [] barbers, double lambda, double mu1, double mu2)
	{
		
//		double [] barbersServiceRate = new double[barbers.length];
		double arrivalTime = 0;
		int lostCustomers = 0;
		double lowestVal;
		double totalDelay = 0;
		double totalAvgDelay = 0;
//		LinkedList<Double> serviceTimes = new LinkedList<Double>();
//		LinkedList<Double> arrivalTimes = new LinkedList<Double>();
//		LinkedList<double[]> chairTimes = new LinkedList<double[]>();
		double waitLength = 0;
		double departureTime = 0;
		double delayAmount;
//		double [] serviceTimes = new double[barbers.length];
		double [] sumServiceBarbers = new double[barbers.length];
		double [] departureBarbers = new double[barbers.length];
		double [] departureChairs = new double[chairs.length];
		boolean waitingRoom = false;
		LinkedList<Double> serviceTimes = new LinkedList<Double>();
		boolean customerLeft = false;
		double savedArrival;
		double prevArrival;
		
		
		for(int i = 0; i < barbers.length; i++)
		{
			barbers[i] = 0;
			departureBarbers[i] = 0;
		}
		
		for(int i = 0; i < chairs.length; i++)
		{
			chairs[i] = 0;
			departureChairs[i] = 0;
		}
		
		Random randSeedOne = new Random(SEED_ONE); //used for George, initially
		Random randSeedTwo = new Random(SEED_TWO); //used for Fred, initially
		Random randSeedThree = new Random(SEED_THREE); //used for customers, initially
		Random randSeedFour = new Random(SEED_FOUR); //used for booleans, initially;
		
		
		arrivalTime = distribution(lambda, randSeedThree.nextDouble());
		savedArrival = arrivalTime;
		prevArrival = arrivalTime;
		
		serviceTimes.add(arrivalTime);
		barbers[FRED] = distribution(mu2,randSeedTwo.nextDouble());
		barbers[GEORGE] = distribution(mu1,randSeedOne.nextDouble());
		
		int noAvail = 0;
		int bothAvail = 0;
		int fredAvail = 0;
		int georgeAvail = 0;
		
		for(int i = 0; i < AMOUNT_OF_TIMES; i++)
		{
			//Neither are available, so calculate delay
			if(arrivalTime < departureBarbers[FRED] && arrivalTime < departureBarbers[GEORGE])
			{
				
				//Fred will get customer first
				if(departureBarbers[FRED] < departureBarbers[GEORGE])
				{
					delayAmount = departureBarbers[FRED] - arrivalTime;
					totalDelay += delayAmount;
					departureBarbers[FRED] = barbers[FRED] + arrivalTime;
					barbers[FRED] = distribution(mu2,randSeedTwo.nextDouble());					
				}//George gets customer first
				else if(departureBarbers[FRED] > departureBarbers[GEORGE])
				{
					delayAmount = departureBarbers[GEORGE] - arrivalTime;
					totalDelay += delayAmount;
					departureBarbers[GEORGE] = barbers[GEORGE] + arrivalTime;
					barbers[GEORGE] = distribution(mu1,randSeedOne.nextDouble());
					
				}
				else //they finished at the same time
				{
					if(randSeedFour.nextBoolean())
					{
						delayAmount = departureBarbers[FRED] - arrivalTime;
						totalDelay += delayAmount;
						departureBarbers[FRED] = barbers[FRED] + arrivalTime;
						barbers[FRED] = distribution(mu2, randSeedTwo.nextDouble());
					}
					else
					{
						delayAmount = departureBarbers[FRED] - arrivalTime;
						totalDelay += delayAmount;
						departureBarbers[GEORGE] = barbers[GEORGE] + arrivalTime;
						barbers[GEORGE] = distribution(mu1,randSeedOne.nextDouble());
					}
				}
				
				noAvail++;
				waitingRoom = true;
				
			}
			else if(arrivalTime >= departureBarbers[FRED] && arrivalTime >= departureBarbers[GEORGE])
			{
				delayAmount = 0;
				if(randSeedFour.nextBoolean())
				{
					departureBarbers[FRED] = barbers[FRED] + arrivalTime;
					barbers[FRED] = distribution(mu2, randSeedTwo.nextDouble());
				}
				else
				{
					departureBarbers[GEORGE] = barbers[GEORGE] + arrivalTime;
					barbers[GEORGE] = distribution(mu1,randSeedOne.nextDouble());
				}
				bothAvail++;
			}
			else if(arrivalTime >= departureBarbers[FRED] && arrivalTime < departureBarbers[GEORGE])
			{//Fred is available but george is not
				delayAmount = 0;
				departureBarbers[FRED] = barbers[FRED] + arrivalTime;
				barbers[FRED] = distribution(mu2,randSeedTwo.nextDouble());
				fredAvail++;
			}
			else if(arrivalTime < departureBarbers[FRED] && arrivalTime >= departureBarbers[GEORGE])
			{//George is available but Fred is not
				delayAmount = 0;
				departureBarbers[GEORGE] = barbers[GEORGE] + arrivalTime;
				barbers[GEORGE] = distribution(mu1,randSeedOne.nextDouble());
				georgeAvail++;
			}
			
			
			//Both are busy
			if(waitingRoom)
			{
				
				if(availableSeat(arrivalTime,departureChairs,shortestWait(barbers)))//savedArrival, chairs))
				{
					int openChair = getSeat(arrivalTime,departureChairs,shortestWait(barbers));//savedArrival, chairs);
					chairs[openChair] = savedArrival;
					departureChairs[openChair] = arrivalTime;
					customerLeft = false;
				}
				else
				{
					lostCustomers++;
					customerLeft = true;
				}
				waitingRoom = false;
			}
			
			//Get the value, save it, and add it to the next
			
			arrivalTime = distribution(lambda,randSeedThree.nextDouble());
			savedArrival = arrivalTime;
			arrivalTime += prevArrival;//serviceTimes.get(serviceTimes.size()-1);
			
			//If the customer left then they can't be put in the total delay
			if(customerLeft)
			{
				serviceTimes.remove(serviceTimes.size()-1); //If the customer left, the customer after may still stay
//				arrivalTime -= savedArrival;
			}
			prevArrival = arrivalTime;
			serviceTimes.add(savedArrival);
			
		}
		
		
		System.out.println("Times with no availability: " + noAvail);
		System.out.println("Times when both were available: " + bothAvail);
		System.out.println("Times when Fred was available: " + fredAvail);
		System.out.println("Times when george was available: " + georgeAvail);

		/*
		for(int i = 0; i < AMOUNT_OF_TIMES; i++)
		{
			
			
//			arrivalTime = distribution(lambda, randSeedThree.nextDouble());
//			barbers[GEORGE] = distribution(mu1,randSeedOne.nextDouble());
//			barbers[FRED] = distribution(mu2,randSeedTwo.nextDouble());
			
			
			
			if(arrivalTime < departureBarbers[GEORGE] && arrivalTime < departureBarbers[FRED])
			{
//				delayAmount = shortestWait(departureBarbers) - arrivalTime;
				waitingRoom = true;
				
//				waitLength = delayAmount + shortestWait(barbers);
				
				if(departureBarbers[GEORGE] < departureBarbers[FRED])
				{
					departureBarbers[GEORGE] = arrivalTime + barbers[GEORGE];//waitLength;
//					totalDelay += delayAmount;
					sumServiceBarbers[GEORGE] += barbers[GEORGE]; 
					barbers[GEORGE] = distribution(mu1,randSeedOne.nextDouble());
				}
				else if(departureBarbers[GEORGE] > departureBarbers[FRED])
				{
					departureBarbers[FRED] = arrivalTime + barbers[FRED];//waitLength;
//					totalDelay += delayAmount;
					sumServiceBarbers[FRED] += barbers[FRED];
					barbers[FRED] = distribution(mu2,randSeedTwo.nextDouble());
				}
				else
				{
					if(randSeedFour.nextBoolean())
					{
						departureBarbers[GEORGE] = arrivalTime + barbers[GEORGE];//waitLength;
//						totalDelay += delayAmount;
						sumServiceBarbers[GEORGE] += barbers[GEORGE]; 	
						barbers[GEORGE] = distribution(mu2,randSeedTwo.nextDouble());
					}
					else
					{
						departureBarbers[FRED] = arrivalTime + barbers[FRED];//waitLength;
//						totalDelay += delayAmount;
						sumServiceBarbers[FRED] += barbers[FRED];	
						barbers[FRED] = distribution(mu2,randSeedTwo.nextDouble());
					}
				}
			}
			else if(arrivalTime < departureBarbers[GEORGE] && arrivalTime > departureBarbers[FRED])
			{
				delayAmount = 0;
				waitingRoom = false;
				
				waitLength = barbers[FRED];
				departureBarbers[FRED] = arrivalTime + barbers[FRED];//waitLength;
				sumServiceBarbers[FRED] += barbers[FRED];
				barbers[FRED] = distribution(mu2,randSeedTwo.nextDouble());
			}
			else if(arrivalTime > departureBarbers[GEORGE] && arrivalTime < departureBarbers[FRED])
			{
				delayAmount = 0;
				waitingRoom = false;
				
				waitLength = barbers[GEORGE];
				departureBarbers[GEORGE] = arrivalTime + barbers[GEORGE];//waitLength;
				sumServiceBarbers[GEORGE] += barbers[GEORGE];	
				barbers[GEORGE] = distribution(mu2,randSeedTwo.nextDouble());
			}
			else if(arrivalTime >= departureBarbers[GEORGE] && arrivalTime >= departureBarbers[FRED])
			{
				delayAmount = 0;
				waitingRoom = false;
				
				
				if(randSeedFour.nextBoolean())
				{
					waitLength = barbers[GEORGE];
					departureBarbers[GEORGE] = arrivalTime + barbers[GEORGE];//waitLength;
					sumServiceBarbers[GEORGE] += barbers[GEORGE]; 		
					barbers[GEORGE] = distribution(mu2,randSeedTwo.nextDouble());
				}
				else
				{
					waitLength = barbers[FRED];
					departureBarbers[FRED] = arrivalTime + barbers[FRED];// waitLength;
					sumServiceBarbers[FRED] += barbers[FRED];		
					barbers[FRED] = distribution(mu2,randSeedTwo.nextDouble());
				}
				
			}
			
			if(waitingRoom)
			{

				
				if(arrivalTime >= shortestWait(departureChairs))
				{
					delayAmount = shortestWait(departureBarbers) - arrivalTime;
					waitLength = delayAmount + shortestWait(barbers);
					totalDelay += delayAmount;
					customerLeft = false;
					
					double getVal = shortestWait(departureChairs);
					for(int j = 0; j < departureChairs.length; j++)
					{
						if(departureChairs[j] == getVal)
						{
							departureChairs[j] = arrivalTime;
							break;
						}
					}
					
//					delayAmount = shortestWait(departureBarbers) - arrivalTime;
//					waitLength = delayAmount + shortestWait(barbers);
				}
				else
				{
					lostCustomers++;
					customerLeft = true;
				}
			}
		
			arrivalTime = distribution(lambda, randSeedThree.nextDouble());
			arrivalTime += serviceTimes.get(serviceTimes.size()-1);
			
			if(customerLeft)
			{
				serviceTimes.remove(serviceTimes.size()-1);
			}
			
			serviceTimes.add(arrivalTime);
			
		}*/
		
		System.out.println("Lost customers: " + lostCustomers);
		
		
		double mean = totalDelay/serviceTimes.size();
		double [] adjustedTimes = new double[serviceTimes.size()];
		double adjustedTotal = 0;
		
		for(int i = 0; i < adjustedTimes.length; i++)
		{
			adjustedTimes[i] = serviceTimes.get(i) - mean;
			adjustedTimes[i] *= adjustedTimes[i];
			adjustedTotal += adjustedTimes[i];
		}
		
		
		double standardDeviation = adjustedTotal/adjustedTimes.length;
		standardDeviation = Math.sqrt(standardDeviation);
		
		System.out.println(mean + ": mean");
		System.out.println("Standard deviation: " + standardDeviation + "\n");
		
		
		
		
		
		
		
		
		
		/*		
		nextArrival = distribution(lambda, randSeedThree.nextDouble());
		

		if(randSeedFour.nextBoolean())
		{
			barbers[0] = distribution(mu1, randSeedOne.nextDouble());
			serviceTimes.add(barbers[0]);
			barbers[0] += nextArrival;
		}
		else
		{
			barbers[1] = distribution(mu2, randSeedTwo.nextDouble());
			serviceTimes.add(barbers[1]);
			barbers[1] += nextArrival;
		}
		
		nextArrival = distribution(lambda, randSeedThree.nextDouble());
		
		if(barbers[0] == 0)
		{
			barbers[0] = distribution(mu1, randSeedOne.nextDouble());
			serviceTimes.add(barbers[0]);
			barbers[0] += nextArrival;
		}
		else
		{
			barbers[1] = distribution(mu2, randSeedTwo.nextDouble());
			serviceTimes.add(barbers[1]);
			barbers[1] += nextArrival;
		}
		
		for(int i = 0; i < chairs.length; i++)
		{
			chairs[i] = distribution(lambda, randSeedThree.nextDouble());
		}
		chairTimes.add(chairs);
*/

		
		
		
		
		
		
		
		/*		
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
					serviceTimes.add(barbers[0]);
					barbers[0] += nextArrival;
				}
				else
				{
					barbers[1] = distribution(mu2, randSeedTwo.nextDouble());
					serviceTimes.add(barbers[1]);
					barbers[1] += nextArrival;
				}
			}
			else if((nextArrival > (longestWait(chairs) + barbers[0]))
					&&
					(nextArrival < (longestWait(chairs) + barbers[1])))
			{
				barbers[0] = distribution(mu1, randSeedOne.nextDouble());
				serviceTimes.add(barbers[0]);
				barbers[0] += nextArrival;
			}
			else if((nextArrival < (longestWait(chairs) + barbers[0]))
					&&
					(nextArrival > (longestWait(chairs) + barbers[1])))
			{
				barbers[1] = distribution(mu2, randSeedTwo.nextDouble());
				serviceTimes.add(barbers[1]);
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
					if(shortestWait(chairs) < shortestWait(barbers))
					{
						delayAmount = shortestWait(barbers)-shortestWait(chairs);
					}
					else
					{
						delayAmount = 0;
					}
					
					waitLength = delayAmount + serviceTime;
					
					
					lowestVal = shortestWait(chairs);
					for(int j = 0; j < chairs.length; j++)
					{
						if(lowestVal == chairs[j])
						{
							chairs[j] = lowestVal + nextArrival;
							chairTimes.add(chairs);
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
		
		*/
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
	
	private boolean availableSeat(double arrival, double[] chairs, double serviceTime)
	{
		for(int i = 0; i < chairs.length; i++)
		{
			if(arrival >= chairs[i]+serviceTime)
			{
				return true;
			}
		}
		return false;
	}
	
	private int getSeat(double arrival, double[] chairs, double serviceTime)
	{
		for(int i = 0; i < chairs.length; i++)
		{
			if(arrival >= chairs[i]+serviceTime)
			{
				return i;
			}
		}
		return -1;
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
