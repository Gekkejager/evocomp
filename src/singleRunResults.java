// Results of a single simulation
public class singleRunResults {
	public boolean _success;
	public int _genFirstHit;
	public int _genConverges;
	public int _fctEvals;
	public int _CPUtime;

	// constructor
	singleRunResults(boolean success, int genFirstHit, int genConverges, int fctEvals, int CPUtime){
		_success = success;
		_genFirstHit = genFirstHit;
		_genConverges = genConverges;
		_fctEvals = fctEvals;
		_CPUtime = CPUtime;
	}
}