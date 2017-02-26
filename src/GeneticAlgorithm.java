import java.util.Arrays;
import java.util.Comparator;

/*
 * General algorithmic Functions
 * 
 * 
 */
public class GeneticAlgorithm {
	public void quickTest(int nSamples, String fitnessFunction){
		int arraySize = 12;
		int[][] sortedInitialPopulation = sortPopulationByScore(generateInitialValues(nSamples, arraySize), fitnessFunction);
		prettyPrintPopulation(sortedInitialPopulation, fitnessFunction);
	}
	
	
	public int[][] runPermutations(int nSamples, int arraySize, String fitnessFunction, int nIterations){
		int[][] currentValues = generateInitialValues(nSamples, arraySize);
		prettyPrintPopulation(sortPopulationByScore(currentValues, fitnessFunction), fitnessFunction);
		for (int i = 0; i < nIterations; i++) {
			currentValues = setupAndRunSingleGeneration(currentValues, fitnessFunction);
			prettyPrintPopulation(sortPopulationByScore(currentValues, fitnessFunction), fitnessFunction);
		}
//		prettyPrintPopulation(currentValues);
		return currentValues;
	}
	
	
	private int[][] setupAndRunSingleGeneration(int[][] population, String fitnessFunction){
		int[][] sortedPopulation = sortPopulationByScore(population, fitnessFunction);
		int[][] newPopulation = new int[sortedPopulation.length][sortedPopulation[0].length];
		// Make use twice of the same for loop
		for (int i = 0; i < sortedPopulation.length / 4; i++) {
			// To add the new permutations...
			int iPermutations[][] = permute(sortedPopulation[i*2], sortedPopulation[i*2 + 1]);
			newPopulation[i*2] = iPermutations[0];
			newPopulation[i*2 + 1] = iPermutations[1];
			// ...and to add the best first half of the previous one
			newPopulation[sortedPopulation.length/2 + i*2] = sortedPopulation[i*2];
			newPopulation[sortedPopulation.length/2 + i*2 + 1] = sortedPopulation[i*2 + 1];
		}		
		return newPopulation;
	}
	
	// TODO: This will change depending on whether 2X or UX is chosen this is a stub
	// I think it should look somewhat similar to this
	private int[][] permute(int[] sample1, int[] sample2){
		int [][] returnValue = new int[2][sample1.length];
		for (int i = 0; i < sample1.length / 2; i++) {
			returnValue[0][i] = sample1[i];
			returnValue[1][i] = sample2[i];
		}
		for (int i = sample1.length / 2; i < sample1.length; i++) {
			returnValue[0][i] = sample2[i];
			returnValue[1][i] = sample1[i];
		}
		return returnValue;
	}
	

	
	// sorts population descending by score given a certain fitness function
	private int[][] sortPopulationByScore(int[][] population, String fitnessFunction){
		double ranks[][] = populationRanks(population, fitnessFunction);
		
		Arrays.sort(ranks, new Comparator<double[]>() {
            @Override
            public int compare(final double[] entry1, final double[] entry2) {
            	// compare scores (entry2 first to ensure descendingness)
                return Double.compare(entry2[0], entry1[0]);
            }
        });
		
		int[][] sortedPopulation = new int[ranks.length][ranks[0].length];
		for(int i=0; i < ranks.length; i++){
			// populate with correct samples
			sortedPopulation[i] = population[(int)ranks[i][1]];
		}
		return sortedPopulation;
	}
	
	
	// returns a 2-d array with [score, index] so that we can later sort the population by score
	private double[][] populationRanks(int[][] population, String fitnessFunction){
		Evaluators fitnessFunctions = new Evaluators();
		double ranks[][] = new double[population.length][2];
		for(int i=0; i < population.length; i++){
			switch(fitnessFunction) {
			case "UniformlyScaledCountingOnesFunction":
				ranks[i][0] = fitnessFunctions.UniformlyScaledCountingOnesFunction(population[i]);
				ranks[i][1] = i;
				break;
			case "LinearlyScaledCountingOnesFunction":
				ranks[i][0] = fitnessFunctions.LinearlyScaledCountingOnesFunction(population[i]);
				ranks[i][1] = i;
				break;
			case "DeceptiveTrapFunction":
				ranks[i][0] = fitnessFunctions.DeceptiveTrapFunction(population[i]);
				ranks[i][1] = i;
				break;
			case "NonDeceptiveTrapFunction":
				ranks[i][0] = fitnessFunctions.NonDeceptiveTrapFunction(population[i]);
				ranks[i][1] = i;
				break;
			default:
				throw new IllegalArgumentException("Invalid fitness function: " + fitnessFunction);
			}
		}
		return ranks;
	}
	
	// Overload to set arrayLength default to 100
	private int[][] generateInitialValues(int nSamples){
		int arrayLength = 100;
		return generateInitialValues(nSamples, arrayLength);
	}
	
	// Generates a initial starting population 
	private int[][] generateInitialValues(int nSamples, int arrayLength){
		int initialValues[][] = new int[nSamples][arrayLength];
		for(int i=0; i < nSamples; i++){
			for(int j=0; j < arrayLength; j++){
				initialValues[i][j] = (int)Math.round(Math.random());
			}
		}
		return initialValues;
	}
	
	
	public void prettyPrintPopulation(int[][] population, String fitnessFunction){
		System.out.println("***");
		System.out.printf("Average score for this iteration: %.2f\n", averageScoreForPopulation(population, fitnessFunction));
		for(int i=0; i < population.length; i++){
			for(int j=0; j < population[i].length; j++){
				System.out.print(population[i][j]);
				// format per 4 bits
				if((j+1) % 4 == 0)
					System.out.print(" ");
			}
			System.out.print("\n");
		}
	}
	
	private double averageScoreForPopulation(int[][] population, String fitnessFunction){
		Evaluators fitnessFunctions = new Evaluators();
		double totalScore = 0;
		for (int i = 0; i < population.length; i++) {
			switch(fitnessFunction) {
			case "UniformlyScaledCountingOnesFunction":
				totalScore += fitnessFunctions.UniformlyScaledCountingOnesFunction(population[i]);
				break;
			case "LinearlyScaledCountingOnesFunction":
				totalScore += fitnessFunctions.LinearlyScaledCountingOnesFunction(population[i]);
				break;
			case "DeceptiveTrapFunction":
				totalScore += fitnessFunctions.DeceptiveTrapFunction(population[i]);
				break;
			case "NonDeceptiveTrapFunction":
				totalScore += fitnessFunctions.NonDeceptiveTrapFunction(population[i]);
				break;
			default:
				throw new IllegalArgumentException("Invalid fitness function: " + fitnessFunction);
			}
		}
		return totalScore/population.length;
	}
}
