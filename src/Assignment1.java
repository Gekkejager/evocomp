/********************
 *
 * Dennis Craandijk 6002986 & Mick van het Nederend 6032125
 * 10-03-2017
 * 
 * Main class
 * 
 * The parameters below can be altered
 */
public class Assignment1 {

	public static void main(String[] args) {
		GeneticAlgorithm GA = new GeneticAlgorithm();
		
		int bitArrayLength = 100;
		// This should be a reasonable upper bound so that it is never hit. This to prevent infinite loops while coding.
		int nGenerations = 400;
		boolean randomlyLinked = true;
		int nSimulations = 25;
		String[] recombinationOperators = {"TwoPointCrossover", "UniformCrossover"};
		int[] populationSizes = {50, 100, 250, 500};	
		
		String fitnessFunction = "UniformlyScaledCountingOnesFunction";
//		String fitnessFunction = "LinearlyScaledCountingOnesFunction";
//		String fitnessFunction = "DeceptiveTrapFunction";
//		String fitnessFunction = "NonDeceptiveTrapFunction";



		double[][][] results = new double[recombinationOperators.length][populationSizes.length][9];
		
		for (int i = 0; i < recombinationOperators.length; i++) {
			for (int j = 0; j < populationSizes.length; j++) {
				singleRunResults[] innerResults = new singleRunResults[nSimulations];
				for (int k = 0; k < nSimulations; k++) {
					innerResults[k] = GA.run(bitArrayLength, populationSizes[j], nGenerations, fitnessFunction, recombinationOperators[i], randomlyLinked);					
				}
				ResultsStorage rs = new ResultsStorage(innerResults);
				results[i][j] = rs.get();
			}
		}
		// print into latex ready table
		ResultsStorage.latexReadyPrint(fitnessFunction, results, recombinationOperators, populationSizes, randomlyLinked);
	}

	

}
