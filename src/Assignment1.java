
public class Assignment1 {

	public static void main(String[] args) {
		GeneticAlgorithm GA = new GeneticAlgorithm();
		
		int bitArrayLength = 100;
		int nGenerations = 100;
//		boolean randomlyLinked = false;
//		int nSimulations = 25;
//		String[] recombinationOperators = {"UniformCrossover", "TwoPointCrossover"};
//		int[] populationSizes = {50, 100, 250, 500};	

		boolean randomlyLinked = true;
		int nSimulations = 1;
		String[] recombinationOperators = {"TwoPointCrossover"};
		int[] populationSizes = {50};	
		
//		String fitnessFunction = "UniformlyScaledCountingOnesFunction";
		String fitnessFunction = "LinearlyScaledCountingOnesFunction";
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
		
		ResultsStorage.latexReadyPrint(fitnessFunction, results, recombinationOperators, populationSizes);
	}

	

}
