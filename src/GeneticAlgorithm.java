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
		int[][] sortedInitialPopulation = sortPopulationByScore(generateInitialValues(nSamples, arraySize), fitnessFunction, nSamples);
		prettyPrintPopulation(sortedInitialPopulation);
	}
	
	// sorts population descending by score given a certain fitness function
	private int[][] sortPopulationByScore(int[][] population, String fitnessFunction, int nSamples){
		double ranks[][] = populationRanks(population, fitnessFunction);
		
		Arrays.sort(ranks, new Comparator<double[]>() {
            @Override
            public int compare(final double[] entry1, final double[] entry2) {
            	// compare scores (entry2 first to ensure descendingness)
                return Double.compare(entry2[0], entry1[0]);
            }
        });
		
		int[][] sortedPopulation = new int[nSamples][ranks.length];
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
	
	
	public void prettyPrintPopulation(int[][] toPrint){
		for(int i=0; i < toPrint.length; i++){
			for(int j=0; j < toPrint[i].length; j++){
				System.out.print(toPrint[i][j]);
				// format per 4 bits
				if((j+1) % 4 == 0)
					System.out.print(" ");
			}
			System.out.print("\n");
		}
	}
}
