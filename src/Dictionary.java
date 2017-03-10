import java.util.HashMap;
import java.util.Map;
// cache to prevent too many function evals
public class Dictionary {
	public int missCounter = 0;
	Map<String, Double> alreadyEvaluated = new HashMap<String, Double>();
	
	public double get(int[] populationEntry){
		String key = convertPopulationEntryToString(populationEntry);
		if(alreadyEvaluated.containsKey(key)){
			return alreadyEvaluated.get(key);
		}
		missCounter++;
		return -1;
	}
	
	public void put(int[] populationEntry, double score){
		String key = convertPopulationEntryToString(populationEntry);
		alreadyEvaluated.put(key, score);
	}

	
    private String convertPopulationEntryToString(int[] populationEntry){
    	StringBuilder stringBuilder = new StringBuilder();
    	for (int i = 0; i < populationEntry.length; i++) {
    		stringBuilder.append(populationEntry[i]);
		}
    	return stringBuilder.toString();
    }

}
