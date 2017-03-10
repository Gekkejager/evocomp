/*
 * Fitness Functions
 * 
 */
public class Evaluators {
	
	// Counts the number of ones
	public double UniformlyScaledCountingOnesFunction(int[] input){
		double counter = 0;
		for(int i = 0; i < input.length; i++){
			if(input[i] == 1)
				counter++;
		}
		return counter;
	}
	
	// Sums the indices of the ones
	public double LinearlyScaledCountingOnesFunction(int[] input){
		double sum = 0;
		for(int i = 0; i < input.length; i++){
			if(input[i] == 1)
				sum += i + 1;
		}
		return sum;
	}
	
	// Very difficult function to optimize from
	public double DeceptiveTrapFunction(int[] input){
		return TrapFunction(input, 1);
	}
	
	// Somewhat difficult function to optimize from
	public double NonDeceptiveTrapFunction(int[] input){
		return TrapFunction(input, 2.5);
	}
	
	
	// Overload to set k=4
	private double TrapFunction(int[] input, double deceptionParam){
		int k = 4;
		return TrapFunction(input, k, deceptionParam);
	}
	
	// Chunks array into pieces of size K and evaluates them
	private double TrapFunction(int[] input, int k, double deceptionParam){
		double sum = 0;
		
		for(int i=0; i < Math.floor(input.length/(double) k); i++){
			int subArray[] = new int[k];
			for(int j=0; j < k; j++){
				subArray[j] = input[i*k + j];
			}
			sum += TrapSubArray(subArray, k, deceptionParam);
		}
				
		return sum;
		
	
	}
	
	// Evaluate sub array according to the trap function
	private double TrapSubArray(int[] subArray, int k, double deceptionParam){
		double COScore = UniformlyScaledCountingOnesFunction(subArray);
		if(COScore == k)
			return k;
		
		return k - deceptionParam - ((k - deceptionParam)/(k - 1)) * COScore;
	}

}
