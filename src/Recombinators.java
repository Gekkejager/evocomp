import java.util.Random;

public class Recombinators {
	
    private Random random = new Random();

    public int[][] twoPointCrossover(int[] parent1, int[] parent2) {
        int[][] offspring = new int[2][parent1.length];

        int crossoverPoint1 = random.nextInt(parent1.length-1);
        int crossoverPoint2 = random.nextInt(parent1.length-1);
        int firstCrossoverPoint = Math.min(crossoverPoint1, crossoverPoint2);
        int secondCrossoverPoint = Math.max(crossoverPoint1, crossoverPoint2);

        for (int i = 0; i < parent1.length - 1; i++) {
            // if in crossover zone
            boolean crossover = ((firstCrossoverPoint <= i) && (i <= secondCrossoverPoint));

            offspring[0][i] = (crossover) ? parent1[i] : parent2[i];
            offspring[1][i] = (crossover) ? parent2[i] : parent1[i];
        }
  
        return offspring;
    }

    public int[][] uniformCrossover(int[] parent1, int[] parent2) {
        int[][] offspring = new int[2][parent1.length];

        for (int i = 0; i < parent1.length -1; i++) {
            // coin flip to chose parent inheritance
            boolean coinFlip = random.nextBoolean();
            offspring[0][i] = (coinFlip) ? parent1[i] : parent2[i];
            offspring[1][i] = (coinFlip) ? parent2[i] : parent1[i];
        }

        return offspring;
    }
}
