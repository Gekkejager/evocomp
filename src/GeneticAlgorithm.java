import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Genetic algorithm framework
 * 
 * 
 */
public class GeneticAlgorithm {
    public singleRunResults run(int bitArrayLength, int populationSize, int nGenerations, String fitnessFunction, String recombinationOperator, boolean randomlyLinked) {
    	Evaluators fitnessFunctions = new Evaluators();
    	boolean success = false;
    	int genFirstHit = -1;
    	int genConverge = -1;
    	long start = System.currentTimeMillis();
        int[][] currentPopulation = generateInitialPopulation(populationSize, bitArrayLength);
        for (int i = 0; i < nGenerations; i++) {
            int[][] nextGenerationPopulation = generateNextGeneration(currentPopulation, fitnessFunction, recombinationOperator, randomlyLinked);
            // exit if no new offspring entered the population
            if (Arrays.deepEquals(currentPopulation, nextGenerationPopulation)) {
            	genConverge = i-1;
            	if(fitnessFunctions.UniformlyScaledCountingOnesFunction(nextGenerationPopulation[0]) == bitArrayLength){
            		success = true;
            	}
                break;
            }
            
            // if the first hit hasnt been updated yet and the first row has no 0's (so only 1's)
            if(genFirstHit == -1 && fitnessFunctions.UniformlyScaledCountingOnesFunction(nextGenerationPopulation[0]) == bitArrayLength){
            	genFirstHit = i;
            }
            
            currentPopulation = nextGenerationPopulation;
        }
        int CPUtime = (int)(System.currentTimeMillis() - start);
        return new singleRunResults(success, genFirstHit, genConverge, 0, CPUtime);
    }

    // generate a new population based on the fitness function and recombination operator
    private int[][] generateNextGeneration(int[][] parentPopulation, String fitnessFunction, String recombinationOperator, boolean randomlyLinked) {
        // shuffle population, generate offspring and combine the two
        int[][] shuffledParentPopulation = shufflePopulation(parentPopulation);
        int[][] offspringPopulation = generateOffspring(shuffledParentPopulation, recombinationOperator, randomlyLinked);
        int[][] combinedPopulation = append(shuffledParentPopulation, offspringPopulation);

        // select the best from the population
        int[][] newPopulation = selectBestFromPopulation(combinedPopulation, parentPopulation.length, fitnessFunction);
        return newPopulation;

    }

    // combine two int arrays
    public static int[][] append(int[][] a, int[][] b) {
        int[][] result = new int[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    // shuffle in random order
    private int[][] shufflePopulation(int[][] population) {

        // Implementing Fisher Yates shuffle
        // https://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
        Random rnd = ThreadLocalRandom.current();
        for (int i = population.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int[] a = population[index];
            population[index] = population[i];
            population[i] = a;
        }

        return population;

    }

    // generate offspring from the parent population based on the recombination operator
    private int[][] generateOffspring(int[][] population, String recombinationOperator, boolean randomlyLinked) {
        int[][] offspring = new int[population.length][population[0].length];

        for (int i = 0; i < population.length; i += 2) {
            int[][] children = recombine(population[i], population[i + 1], recombinationOperator, randomlyLinked);
            offspring[i] = children[0];
            offspring[i + 1] = children[1];
        }

        return offspring;
    }

    private int[][] recombine(int[] parent1, int[] parent2, String recombinationOperator, boolean randomlyLinked) {

        int[][] offspring;

        // encode if randomly linked
        if (randomlyLinked) {
            parent1 = encodeRepresentation(parent1);
            parent2 = encodeRepresentation(parent2);
        }

        Recombinators recombine = new Recombinators();

        switch (recombinationOperator) {
            case "TwoPointCrossover":
                offspring = recombine.twoPointCrossover(parent1, parent2);
                break;
            case "UniformCrossover":
                offspring = recombine.uniformCrossover(parent1, parent2);
                break;
            default:
                throw new IllegalArgumentException("Invalid recombination operator: " + recombinationOperator);
        }

        // decode offspring if randomly linked
        if (randomlyLinked) {
            offspring[0] = decodeRepresentation(offspring[0]);
            offspring[0] = decodeRepresentation(offspring[0]);
        }

        return offspring;
    }

    protected static int[] randomlyLinkedOrder;

    private int[] encodeRepresentation(int[] decodedSolution) {

        int[] encodedSolution = new int[decodedSolution.length];

        // generate shuffled order if not exists
        if (this.randomlyLinkedOrder == null) {
            this.randomlyLinkedOrder = generateRandomlyLinkedOrder(decodedSolution.length);
        }

        // put the solution in the new order
        for (int i = 0; i < this.randomlyLinkedOrder.length; i++) {
            encodedSolution[i] = decodedSolution[this.randomlyLinkedOrder[i]];
        }

        return encodedSolution;
    }

    private int[] decodeRepresentation(int[] encodedSolution) {

        int[] decodedSolution = new int[encodedSolution.length];

        // put the solution in the new order
        for (int i = 0; i < this.randomlyLinkedOrder.length; i++) {
            decodedSolution[this.randomlyLinkedOrder[i]] = encodedSolution[i];
        }
        return decodedSolution;
    }

    private int[] generateRandomlyLinkedOrder(int length) {

        randomlyLinkedOrder = new int[length];

        for (int i = 0; i < length; i++) {
            randomlyLinkedOrder[i] = i;
        }
        // Implementing Fisher–Yates shuffle
        // https://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
        Random rnd = ThreadLocalRandom.current();
        for (int i = randomlyLinkedOrder.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = randomlyLinkedOrder[index];
            randomlyLinkedOrder[index] = randomlyLinkedOrder[i];
            randomlyLinkedOrder[i] = a;
        }

        return randomlyLinkedOrder;
    }

    // select the best bitstrings from the population by a fitness function
    private int[][] selectBestFromPopulation(int[][] population, int desiredPopulationSize, String fitnessFunction) {
        int[][] sortedPopulation = sortPopulationByFitnessScore(population, fitnessFunction);
        int[][] newPopulation = Arrays.copyOfRange(sortedPopulation, 0, desiredPopulationSize);

        return newPopulation;
    }


    // sorts population descending by score given a certain fitness function
    private int[][] sortPopulationByFitnessScore(int[][] population, String fitnessFunction) {
        double ranks[][] = populationRanks(population, fitnessFunction);

        Arrays.sort(ranks, new Comparator<double[]>() {
            @Override
            public int compare(final double[] entry1, final double[] entry2) {
                // compare scores (entry2 first to ensure descendingness)
                return Double.compare(entry2[0], entry1[0]);
            }
        });

        int[][] sortedPopulation = new int[ranks.length][ranks[0].length];
        for (int i = 0; i < ranks.length; i++) {
            // populate with correct samples
            sortedPopulation[i] = population[(int) ranks[i][1]];
        }
        return sortedPopulation;
    }


    // returns a 2-d array with [score, index] so that we can later sort the population by score
    private double[][] populationRanks(int[][] population, String fitnessFunction) {
        Evaluators fitnessFunctions = new Evaluators();
        double ranks[][] = new double[population.length][2];
        for (int i = 0; i < population.length; i++) {
            switch (fitnessFunction) {
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
    private int[][] generateInitialPopulation(int populationSize) {
        int bitArrayLength = 100;
        return generateInitialPopulation(populationSize, bitArrayLength);
    }

    // Generates a initial starting population
    private int[][] generateInitialPopulation(int populationSize, int bitArrayLength) {
        int initialPopulation[][] = new int[populationSize][bitArrayLength];
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < bitArrayLength; j++) {
                initialPopulation[i][j] = (int) Math.round(Math.random());
            }
        }
        return initialPopulation;
    }


    public void prettyPrintPopulation(int[][] population, String fitnessFunction) {
        System.out.println("***");
        System.out.printf("Average score for this iteration: %.2f\n", averageScoreForPopulation(population, fitnessFunction));
        for (int i = 0; i < population.length; i++) {
            for (int j = 0; j < population[i].length; j++) {
                System.out.print(population[i][j]);
                // format per 4 bits
                if ((j + 1) % 4 == 0)
                    System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

    private double averageScoreForPopulation(int[][] population, String fitnessFunction) {
        Evaluators fitnessFunctions = new Evaluators();
        double totalScore = 0;
        for (int i = 0; i < population.length; i++) {
            switch (fitnessFunction) {
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
        return totalScore / population.length;
    }
}
