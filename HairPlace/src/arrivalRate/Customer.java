package arrivalRate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Customer 
{
	//x = -(1/lambda ) * ln(1-u)
	
	private double lambda;
	private double [] allResults;
	Random randomDouble;
	
	public Customer(double lambda, int seed)
	{
		this.lambda = lambda;
		randomDouble = new Random(seed);
	}
	
	public void getTimes(int amountOfTimes)
	{
		double randomNumber;
		int randomInt;
		this.allResults = new double[amountOfTimes];
		
		randomNumber = this.randomDouble.nextDouble();
//		randomInt = (int) randomNumber * Integer.MAX_VALUE;
		this.allResults[0] = distribution(randomNumber);
		
		for(int i = 1; i < this.allResults.length; i++)
		{
			randomNumber = this.randomDouble.nextDouble();
//			randomInt = (int) randomNumber * Integer.MAX_VALUE;
			this.allResults[i] = distribution(randomNumber);
			this.allResults[i] += this.allResults[i-1];
		}
	}
	
/*
	private double distribution(int randomNumber)
	{
		return ((Math.pow(this.lambda, randomNumber))/Factorial.factorial(randomNumber))*Math.pow(Math.E, (this.lambda * -1));
		//((lambda^n)/(n!))*e^-lambda
	}
*/
	private double distribution(double randomNumber)
	{
		return ((1.0/this.lambda) * Math.log(1.0-randomNumber))*-1;
	}

	public void printResults(String fileName) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("..\\" + fileName + ".txt"));
		
		for(int i = 0; i < this.allResults.length; i++)
		{
			writer.write(Double.toString(allResults[i]) + "\r\n");
		}
		
		
		writer.close();
	}
	
}
