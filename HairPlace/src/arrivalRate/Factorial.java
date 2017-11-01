package arrivalRate;


public class Factorial  
{ 
	private int finalAnswer; 
	
	private Factorial(int number)  
	{ 
		this.finalAnswer = fFactorial(number); 
	} 
	 
	public static int factorial(int number) 
	{ 
		Factorial factor = new Factorial(number); 
		return factor.finalAns(); 
	} 
	 
	private int finalAns() 
	{ 
		return this.finalAnswer; 
	} 
	 
	private int fFactorial(int number) 
	{ 
		if(number > 0) 
		{ 
			return number *= fFactorial(number-1); 
		} 
		else 
		{ 
			return 1; 
		} 
	} 
} 
