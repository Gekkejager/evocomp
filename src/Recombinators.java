/**
 * Created by Dennis on 27/02/2017.
 */
public class Recombinators {

    public int[] twoPointCrossover(int[] parent1, int[] parent2) {
        int[] child = new int[parent1.length];

        return child;
    }

    public int[] uniformCrossover(int[] parent1, int[] parent2) {
        int[] child = new int[parent1.length];

        for (int i = 0; i < parent1.length; i++) {
            int coinFlip = (int) Math.round(Math.random());
            child[i] = (coinFlip == 1) ? parent1[i] : parent2[i];
        }

        return child;
    }
}
