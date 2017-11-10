package hairPlace;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;


public class HairPlace 
{
	public static final int SEED_ONE = 10009;
	public static final int SEED_TWO = 20089;
	public static final int SEED_THREE = 30071;
	public static final int SEED_FOUR = 40093;
	
	private static final int AMOUNT_OF_TIMES = 2661;
	
	
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
//		Barber fred = new Barber(3.0, SEED_ONE);
//		Barber george = new Barber(2.0, SEED_TWO);
//		Customer customers = new Customer(4.0, SEED_THREE);
		
//		fred.getTimes(AMOUNT_OF_TIMES);
//		george.getTimes(AMOUNT_OF_TIMES);
//		customers.getTimes(AMOUNT_OF_TIMES);
		
//		fred.printResults("Fred");
//		george.printResults("George");
//		customers.printResults("customers");
		double [] barber = new double[2];
		double [][] chairs = new double[8][];
		LinkedList<Integer> lostCustomers = new LinkedList<Integer>();
		
//		int lostCustomers;
		
		for(int i = 0; i < chairs.length; i++)
		{
			chairs[i] = new double[i+3];
		}
		
		int [] fred = {1,0};
		int [] george = {0,0};
		double [] lambda = {4.0,4.5};
		double [] mu1 = {3.0,2.0,5.0};
		double [] mu2 = {2.0,2.0,0.0};
		
		
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
						}
					}
				}
			}
		}
		
/*		
		lostCustomers = getTimes(chairs[0],
				barber,
				4.5,
				2.0,
				2.0,
				0,
				0);*/	
		//		double lambda = 4.0;
//		double mu1 = 3.0;
//		double mu2 = 2.0;
		
		
		
		
//		getTimes(chairs,barber,lambda,mu1,mu2,1,0);
		
		
	}
	
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
		
		Random randSeedOne = new Random(SEED_ONE); //used for George, initially
		Random randSeedTwo = new Random(SEED_TWO); //used for Fred, initially
		Random randSeedThree = new Random(SEED_THREE); //used for customers, initially
		Random randSeedFour = new Random(SEED_FOUR); //used for booleans, initially;	
		if(mu2 == 0.0)
		{
			return getTimesSingle(chairs,barbers[0],lambda,mu1,randSeedOne,randSeedThree,randSeedFour);
		}
		
		System.out.println("Results when there are " + chairs.length + "chairs");
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
		

		
		
		arrivalTime = distribution(lambda, randSeedThree.nextDouble());
		savedArrival = arrivalTime;
		prevArrival = arrivalTime;
		
		serviceTimes.add(arrivalTime);
		barbers[fred] = distribution(mu2,randSeedTwo.nextDouble());
		barbers[george] = distribution(mu1,randSeedOne.nextDouble());
		
		int noAvail = 0;
		int bothAvail = 0;
		int fredAvail = 0;
		int georgeAvail = 0;
		
		for(int i = 0; i < AMOUNT_OF_TIMES; i++)
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
					if(departureBarbers[fred] <= barbers[george] + arrivalTime)
					{
						departureBarbers[fred] = barbers[fred] + arrivalTime;
					}
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
			{//Fred is available but george is not
					
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
//				arrivalTime -= savedArrival;
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
	
	public int getTimesSingle(double [] chairs, double barber, double lambda, double mu,Random randSeedOne,Random randSeedThree,Random randSeedFour) throws IOException
	{
		
//		double [] barbersServiceRate = new double[barbers.length];
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
		
		for(int i = 0; i < AMOUNT_OF_TIMES; i++)
		{
			//Not available, so calculate delay
			if(arrivalTime < departureBarber)
			{
				
				delayAmount = departureBarber - arrivalTime;
				delays.add(delayAmount);
				totalDelay += delayAmount;
				if(departureBarber <= barber + arrivalTime)
				{
					departureBarber = barber + arrivalTime;
				}
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
			
			
			//Both are busy
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
		System.out.println("Times available: " + (AMOUNT_OF_TIMES-noAvail));
		System.out.println("Lost customers: " + lostCustomers);
		
		
		System.out.println("Percentage not available: " + round(noAvail) + "%");
		System.out.println("Percentage available: " + round(AMOUNT_OF_TIMES-noAvail) + "%");
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
	
	private double round(double initial)
	{
		
		initial = initial/AMOUNT_OF_TIMES;	
		initial *= 10000;
		initial = Math.round(initial);
		return initial/100;
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
	

	
	private double distribution(double lambda, double randomNumber)
	{
		return ((1.0/lambda) * Math.log(1.0-randomNumber))*-1;
	}

	
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
