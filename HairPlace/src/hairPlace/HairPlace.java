package hairPlace;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;


public class HairPlace 
{
	public static final int SEED_GEORGE = 10009;
	public static final int SEED_FRED = 20089;
	public static final int SEED_CUSTOMERS = 30071;
	public static final int SEED_INDECISIVE = 40093;
	
	private static final int AMOUNT_OF_CUSTOMERS = 2661;
	
	
	public HairPlace(String [] args) throws IOException
	{
		double mu1 = Double.parseDouble(args[0]);
		double mu2 = Double.parseDouble(args[1]);
		double lambda = Double.parseDouble(args[2]);
		double [] chairs = new double[Integer.parseInt(args[3])];
		double [] barbers = new double[2];
		
		
		getTimes(chairs,barbers,lambda,mu1,mu2,1,0);
	}
	
	public HairPlace() throws IOException
	{
		double [] barber = new double[2];
		double [][] chairs = new double[8][];
		LinkedList<Integer> lostCustomers = new LinkedList<Integer>();
		
		for(int i = 0; i < chairs.length; i++)
		{
			chairs[i] = new double[i+3];
		}
		
		int [] fred = {1,0};
		int [] george = {0,0};
		double [] lambda = {4.0,4.5};
		double [] mu1 = {3.0,2.0,2.5,5.0};
		double [] mu2 = {2.0,2.0,2.5,0.0};
		
		
		for(int i = 0; i < lambda.length; i++)
		{
			for(int j = 0; j < george.length; j++)
			{
				for(int k = 0; k < mu1.length; k++)
				{
					for(int l = 0; l < chairs.length; l++)
					{
						lostCustomers.add(getTimes(chairs[l],
								barber,
								lambda[i],
								mu1[k],
								mu2[k],
								fred[j],
								george[j]));
						
						if(lostCustomers.get(lostCustomers.size()-1) == 0)
						{
							break;
						}//If there isn't any lost customers at x, then there wont be any >x
					}
				}
			}
		}
		
		
	}
	/**
	 * 
	 * @param chairs how many chairs there is
	 * @param barbers who the barbers are
	 * @param lambda the average amount of customers coming at a given point in time
	 * @param mu1 how much people on average George can handle at any given time
	 * @param mu2 how much people on average Fred can handle at any given time
	 * @param fred needed to test if there is a barber
	 * @param george needed to test if there is a barber
	 * @return the amount of customers, the simulation doesn't need to go on once 0 lost customers is reached
	 * @throws IOException
	 */
	
	public int getTimes(double [] chairs, double [] barbers, double lambda, double mu1, double mu2, int fred, int george) throws IOException
	{
		
		double arrivalTime = 0;
		int lostCustomers = 0;
		double totalDelay = 0;
		double delayAmount = 0;
		double [] departureBarbers = new double[barbers.length];
		double [] departureChairs = new double[chairs.length];
		boolean waitingRoom = false;
		LinkedList<Double> serviceTimes = new LinkedList<Double>();
		boolean customerLeft = false;
		double savedArrival;
		double prevArrival;
		LinkedList<Double> delays = new LinkedList<Double>();
		
		Random randSeedOne = new Random(SEED_GEORGE); //used for George, initially
		Random randSeedTwo = new Random(SEED_FRED); //used for Fred, initially
		Random randSeedThree = new Random(SEED_CUSTOMERS); //used for customers, initially
		Random randSeedFour = new Random(SEED_INDECISIVE); //used for booleans, initially;	
		if(mu2 == 0.0)
		{
			return getTimesSingle(chairs,barbers[0],lambda,mu1,randSeedOne,randSeedThree,randSeedFour);
		}
		
		System.out.println("Results when there are " + chairs.length + " chairs");
		if(fred == george)
		{
			System.out.println("One barber");
			System.out.println("mu being " + mu1);
		}
		else
		{
			System.out.println("Two barbers");
			System.out.println("mu1 being " + mu1);
			System.out.println("mu2 being " + mu2);
		}
		System.out.println("Lambda being " + lambda);
		
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
		

		
		//Get the initial arrival time of the customer
		arrivalTime = distribution(lambda, randSeedThree.nextDouble());
		savedArrival = arrivalTime;
		prevArrival = arrivalTime;
		
		serviceTimes.add(arrivalTime);
		
		//get the initial service times of the barbers
		barbers[fred] = distribution(mu2,randSeedTwo.nextDouble());
		barbers[george] = distribution(mu1,randSeedOne.nextDouble());
		
		int noAvail = 0;
		int bothAvail = 0;
		int fredAvail = 0;
		int georgeAvail = 0;
		
		for(int i = 0; i < AMOUNT_OF_CUSTOMERS; i++)
		{
			//Neither are available, so calculate delay
			if(arrivalTime < departureBarbers[fred] && arrivalTime < departureBarbers[george])
			{
				
				//Fred will get customer first
				if(departureBarbers[fred] < departureBarbers[george])
				{
					delayAmount = departureBarbers[fred] - arrivalTime;
					delays.add(delayAmount);
					totalDelay += delayAmount;
					//Fred was able to take a break
					if(departureBarbers[fred] <= barbers[fred] + arrivalTime)
					{
						departureBarbers[fred] = barbers[fred] + arrivalTime;
					}
					//Someone was waiting and Fred had to take them immediately
					else
					{
						departureBarbers[fred] += barbers[fred];
					}
					barbers[fred] = distribution(mu2,randSeedTwo.nextDouble());					
				}//George gets customer first
				else if(departureBarbers[fred] > departureBarbers[george])
				{
					delayAmount = departureBarbers[george] - arrivalTime;
					delays.add(delayAmount);
					totalDelay += delayAmount;
					//George was able to take a break
					if(departureBarbers[george] <= barbers[george]+arrivalTime)
					{
						departureBarbers[george] = barbers[george] + arrivalTime;
					}
					//Someone was waiting and George had to take them immediately 
					else
					{
						departureBarbers[george] += barbers[george];
					}
					barbers[george] = distribution(mu1,randSeedOne.nextDouble());
					
				}
				else //they finished at the same time
				{
					if(randSeedFour.nextBoolean())
					{
						delayAmount = departureBarbers[fred] - arrivalTime;
						delays.add(delayAmount);
						totalDelay += delayAmount;
						if(departureBarbers[fred] <= barbers[fred] + arrivalTime)
						{
							departureBarbers[fred] = barbers[fred] + arrivalTime;
						}
						else
						{
							departureBarbers[fred] += barbers[fred];
						}
						barbers[fred] = distribution(mu2, randSeedTwo.nextDouble());
					}
					else
					{
						delayAmount = departureBarbers[george] - arrivalTime;
						delays.add(delayAmount);
						totalDelay += delayAmount;
						if(departureBarbers[george] <= barbers[george]+arrivalTime)
						{
							departureBarbers[george] = barbers[george] + arrivalTime;
						}
						else
						{
							departureBarbers[george] += barbers[george];
						}
						barbers[george] = distribution(mu1,randSeedOne.nextDouble());
					}
				}
				
				noAvail++;
				waitingRoom = true;
				
			}
			else if(arrivalTime >= departureBarbers[fred] && arrivalTime >= departureBarbers[george])
			{
				delayAmount = 0;
				if(randSeedFour.nextBoolean())
				{
					departureBarbers[fred] = barbers[fred] + arrivalTime;
					barbers[fred] = distribution(mu2, randSeedTwo.nextDouble());
				}
				else
				{
					departureBarbers[george] = barbers[george] + arrivalTime;
					barbers[george] = distribution(mu1,randSeedOne.nextDouble());
				}
				bothAvail++;
			}
			else if(arrivalTime >= departureBarbers[fred] && arrivalTime < departureBarbers[george])
			{//Fred is available but George is not
					
				delayAmount = 0;
				if(departureBarbers[fred] <= barbers[fred] + arrivalTime)
				{
					departureBarbers[fred] = barbers[fred] + arrivalTime;
				}
				else
				{
					departureBarbers[fred] += barbers[fred];
				}
				barbers[fred] = distribution(mu2,randSeedTwo.nextDouble());
				fredAvail++;
			}
			else if(arrivalTime < departureBarbers[fred] && arrivalTime >= departureBarbers[george])
			{//George is available but Fred is not
				delayAmount = 0;
				if(departureBarbers[george] <= barbers[george]+arrivalTime)
				{
					departureBarbers[george] = barbers[george] + arrivalTime;
				}
				else
				{
					departureBarbers[george] += barbers[george];
				}
				barbers[george] = distribution(mu1,randSeedOne.nextDouble());
				georgeAvail++;
			}
			
			
			//Both are busy
			if(waitingRoom)
			{
				
				if(availableSeat(arrivalTime,departureChairs,shortestWait(barbers,fred,george)))//savedArrival, chairs))
				{
					int openChair = getSeat(arrivalTime,departureChairs,shortestWait(barbers,fred,george));//savedArrival, chairs);
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
			}
			prevArrival = arrivalTime;
			serviceTimes.add(savedArrival);
			
		}
		
		
		System.out.println("Times with no availability: " + noAvail);
		if(fred == 0)
		{
			System.out.println("Times available: " + bothAvail);
		}
		else
		{
			System.out.println("Times when both were available: " + bothAvail);
			System.out.println("Times when Fred was available: " + fredAvail);
			System.out.println("Times when george was available: " + georgeAvail);
		}
		System.out.println("Lost customers: " + lostCustomers);
		
		
		System.out.println("Percentage not available: " + round(noAvail) + "%");
		if(fred == 0)
		{
			System.out.println("Percentage available: " + bothAvail);
		}
		else
		{
			System.out.println("Percentage both were available: " + round(bothAvail) + "%");
			System.out.println("Percentage when Fred was available: " + round(fredAvail) + "%");
			System.out.println("Percentage when George was available: " + round(georgeAvail) + "%");			
		}
		if(round(lostCustomers) < 0.00)
		{
			System.out.println("Percentage of lost customers: negligible");
		}
		else
		{
			System.out.println("Percentage of lost cutomers: " + round(lostCustomers) + "%");
		}

		
		System.out.println();
		
		return lostCustomers;

		
	}
	
	/**
	 * 
	 * @param chairs the amount of chairs in the waiting room
	 * @param barber the barber
	 * @param lambda the average amount of customers arriving
	 * @param mu the average amount of customers the barber can serve
	 * @param randSeedOne the seed to determining how long it will take the barber to serve
	 * @param randSeedThree the seed to determining when a customer will arrive
	 * @param randSeedFour in case a true false situation comes up
	 * @return
	 * @throws IOException
	 */
	
	public int getTimesSingle(double [] chairs, double barber, double lambda, double mu,Random randSeedOne,Random randSeedThree,Random randSeedFour) throws IOException
	{
		double arrivalTime = 0;
		int lostCustomers = 0;
		double totalDelay = 0;
		double delayAmount = 0;
		double departureBarber;
		double [] departureChairs = new double[chairs.length];
		boolean waitingRoom = false;
		LinkedList<Double> serviceTimes = new LinkedList<Double>();
		boolean customerLeft = false;
		double savedArrival;
		double prevArrival;
		LinkedList<Double> delays = new LinkedList<Double>();

		
		System.out.println("Results when there are " + chairs.length + "chairs");

		System.out.println("One barber");
		System.out.println("mu being " + mu);
		System.out.println("Lambda being " + lambda);
		barber = 0;
		departureBarber = 0;
		
		for(int i = 0; i < chairs.length; i++)
		{
			chairs[i] = 0;
			departureChairs[i] = 0;
		}
		
		
		arrivalTime = distribution(lambda, randSeedThree.nextDouble());
		savedArrival = arrivalTime;
		prevArrival = arrivalTime;
		
		serviceTimes.add(arrivalTime);
		barber = distribution(mu,randSeedOne.nextDouble());
		
		int noAvail = 0;
		
		for(int i = 0; i < AMOUNT_OF_CUSTOMERS; i++)
		{
			//Not available, so calculate delay
			if(arrivalTime < departureBarber)
			{
				
				delayAmount = departureBarber - arrivalTime;
				delays.add(delayAmount);
				totalDelay += delayAmount;

				//the barber was able to take a break before the next customer arrived
				if(departureBarber <= barber + arrivalTime)
				{
					departureBarber = barber + arrivalTime;
				}
				//Someone had to be served immediately and the barber couldn't take a break
				else
				{
					departureBarber += barber;
				}
				barber = distribution(mu,randSeedOne.nextDouble());					
				noAvail++;
				waitingRoom = true;
				
			}
			else
			{
				delayAmount = 0;
				if(departureBarber <= barber + arrivalTime)
				{
					departureBarber = barber + arrivalTime;
				}
				else
				{
					departureBarber += barber;
				}
				barber = distribution(mu,randSeedOne.nextDouble());
				waitingRoom = false;
			}
			
			
			//barber is busy
			if(waitingRoom)
			{
				
				if(availableSeat(arrivalTime,departureChairs,barber))//savedArrival, chairs))
				{
					int openChair = getSeat(arrivalTime,departureChairs,barber);//savedArrival, chairs);
					chairs[openChair] = savedArrival;
					departureChairs[openChair] = arrivalTime;
					customerLeft = false;
				}
				else
				{
					lostCustomers++;
					customerLeft = true;
				}
			}
			
			//Get the value, save it, and add it to the next
			
			arrivalTime = distribution(lambda,randSeedThree.nextDouble());
			savedArrival = arrivalTime;
			arrivalTime += prevArrival;//serviceTimes.get(serviceTimes.size()-1);
			
			//If the customer left then they can't be put in the total delay
			if(customerLeft)
			{
				serviceTimes.remove(serviceTimes.size()-1); //If the customer left, the customer after may still stay
			}
			prevArrival = arrivalTime;
			serviceTimes.add(savedArrival);
			
		}
		
		
		System.out.println("Times with no availability: " + noAvail);
		System.out.println("Times available: " + (AMOUNT_OF_CUSTOMERS-noAvail));
		System.out.println("Lost customers: " + lostCustomers);
		
		
		System.out.println("Percentage not available: " + round(noAvail) + "%");
		System.out.println("Percentage available: " + round(AMOUNT_OF_CUSTOMERS-noAvail) + "%");
		if(round(lostCustomers) < 0.00)
		{
			System.out.println("Percentage of lost customers: negligible");
		}
		else
		{
			System.out.println("Percentage of lost cutomers: " + round(lostCustomers) + "%");
		}
		
		System.out.println();
		
		return lostCustomers;

		
	}
	
	/**
	 * Rounds the number to the hundreds place
	 * 
	 * @param initial the initial value coming in
	 * @return the rounded value
	 */
	
	private double round(double initial)
	{
		
		initial = initial/AMOUNT_OF_CUSTOMERS;	
		initial *= 10000;
		initial = Math.round(initial);
		return initial/100;
	}
	
	/**
	 * Checks if there is a seat for a waiting customer to sit in
	 * 
	 * @param arrival the time the customer arrived
	 * @param chairs the amount of chairs
	 * @param serviceTime how long it will take the barber to service
	 * @return true if there is a seat available, otherwise false
	 */
	
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
	/**
	 * Sits the customer into an empty seat
	 * 
	 * @param arrival the arrival time of the customer
	 * @param chairs the amount of chairs
	 * @param serviceTime the time it will take the barber to service
	 * @return the seat going to be sat in, or -1 if no seats available
	 */
	
	
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
	
	/**
	 * Calculates the arrival time of a customer,
	 * and the service times for the barbers 
	 * 
	 * @param lambda the average amount of customers arriving, or the average service time
	 * @param randomNumber a random number to give a distributive average
	 * @return the arrival time or service time
	 */
	
	private double distribution(double lambda, double randomNumber)
	{
		return ((1.0/lambda) * Math.log(1.0-randomNumber))*-1;
	}

	/**
	 * Checks to see what barber will be done first
	 * 
	 * @param barbers the barbers
	 * @param fred
	 * @param george
	 * @return what barber will finish first
	 */
	
	private double shortestWait(double [] barbers, int fred, int george)
	{
		Random rand = new Random(1);
		if(barbers[fred] < barbers[george])
		{
			return barbers[fred];
		}
		else if(barbers[fred] > barbers[george])
		{
			return barbers[george];
		}
		else
		{
			if(rand.nextBoolean())
			{
				return barbers[fred];
			}
			else
			{
				return barbers[george];
			}
		}
	}
	

	
	public static void main(String [] args) throws IOException
	{
		if(args.length < 1)
		{
			new HairPlace();
		}
		else
		{
			new HairPlace(args);
		}
	}
}
