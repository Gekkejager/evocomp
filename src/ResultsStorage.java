// All results of a run with certain parameters and n simulations 
public class ResultsStorage{
	private double[] _results;

	private double _successes = 0;
	
	private double _genFirstHitMean;
	private double _genFirstHitSd = 0;
	
	private double _genConvergeMean;
	private double _genConvergeSd = 0;
	
	private double _fctEvalsMean;
	private double _fctEvalsSd = 0;
	
	private double _CPUtimeMean;
	private double _CPUtimeSd = 0;
	
	// constructor
	// compute means and standard deviations, count number of successes
	public ResultsStorage(singleRunResults[] results) {
		int totalGenFirstHits = 0;
		int totalGenConverges = 0;
		int totalFctEvals = 0;
		int totalCPUtime = 0;
		
		int counter = 0;
		// get means 
		for (int i = 0; i < results.length; i++) {
			if(results[i]._success){
				_successes += 1; 
				totalGenFirstHits += results[i]._genFirstHit;
				totalGenConverges += results[i]._genConverges;
				counter++;
			}
			totalFctEvals += results[i]._fctEvals;
			totalCPUtime += results[i]._CPUtime;
		}
		
		_genFirstHitMean = (double)totalGenFirstHits / (double)counter;
		_genConvergeMean = (double)totalGenConverges / (double)counter;
		_fctEvalsMean = (double)totalFctEvals / (double)results.length;
		_CPUtimeMean = (double)totalCPUtime / (double)results.length;
		
		// get standard deviations
		for (int i = 0; i < results.length; i++) {
			if(results[i]._success){
				_genFirstHitSd += Math.pow((double)results[i]._genFirstHit - _genFirstHitMean, 2);
				_genConvergeSd += Math.pow((double)results[i]._genConverges - _genConvergeMean, 2);
			}
			_fctEvalsSd += Math.pow((double)results[i]._fctEvals - _fctEvalsMean, 2);
			_CPUtimeSd += Math.pow((double)results[i]._CPUtime - _CPUtimeMean, 2);
		}
		
		_genFirstHitSd = Math.sqrt(_genFirstHitSd / (double)counter);
		_genConvergeSd = Math.sqrt(_genConvergeSd / (double)counter);
		_fctEvalsSd = Math.sqrt(_fctEvalsSd / (double)results.length);
		_CPUtimeSd = Math.sqrt(_CPUtimeSd / (double)results.length);
		
		double[] r = {_successes, _genFirstHitMean, _genFirstHitSd, _genConvergeMean, _genConvergeSd, _fctEvalsMean, _fctEvalsSd, _CPUtimeMean, _CPUtimeSd};
		_results = r;
	}
	
	public double[] get(){
		return _results;
	}
	
	public static void latexReadyPrint(String fitnessFunction, double[][][] results, String[] recombinationOperators, int[] populationSizes){
		System.out.println("\\begin{tabular}{ |c|c|c|c|c|c|c| }");
		System.out.println("\t\\hline");
		System.out.printf("\t\\multicolumn{7}{|c|}{%s} \\\\\n", fitnessFunction);
		System.out.println("\t\\hline");
		System.out.println("\tCrossover & PopSize & Successes & Gen.(First Hit) & Gen.(Converge) & Fct Evals & CPU time \\\\");
		
		
		for (int i = 0; i < recombinationOperators.length; i++) {
			System.out.println("\t\\hline");
			String shortHandRecombinationOperator = recombinationOperators[i] == "UniformCrossover"? "UX": "2X";
			System.out.printf("\t\\multirow{4}{*}{%s}\n", shortHandRecombinationOperator);
			for (int j = 0; j < populationSizes.length; j++) {
				System.out.printf("\t\t\t& %d  & %.0f/25 & %.2f (%.2f) & %.2f (%.2f) & %.2f (%.2f) & %.2f (%.2f) \\\\\n", populationSizes[j], results[i][j][0], results[i][j][1], results[i][j][2], results[i][j][3], results[i][j][4], results[i][j][5], results[i][j][6], results[i][j][7], results[i][j][8]);
			}
			System.out.println("\t\\hline");
		}
		System.out.print("\\end{tabular}");
	}
}
