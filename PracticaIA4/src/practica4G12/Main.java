package practica4G12;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import aima.core.search.framework.problem.GoalTest;
import aima.core.search.local.FitnessFunction;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.Individual;

public class Main {

	/* Falta hacer la entrada/salida para poder probar los casos */

	public static final int TOTAL_TURNS = 16;

	private static int turns;
	private static List<List<Integer>> restrictions;
	private static List<List<Integer>> preferences;
	private static List<Integer> turnsPerProfessor;
	private static List<String> professors;
	private static int numProfessors;

	private static int mutationProbability;
	private static int reproductionProbability;

	public static void main(String[] args) {
		System.out.println("Hello");
		getData();
		examTurnsGeneticAlgorithmSearch();
	}

	public static void examTurnsGeneticAlgorithmSearch() {
		FitnessFunction<Integer> fitnessFunction = new ExamTurnsFitnessFunction(turns, restrictions, preferences,
				turnsPerProfessor);
		GoalTest goalTest = new ExamTurnsGoalTest(turns, restrictions);
		/* Generate an initial population */
		Set<Individual<Integer>> population = new HashSet<Individual<Integer>>();
		int teachers = restrictions.size();
		for (int i = 0; i < 50; i++) {
			population.add(ExamTurnsUtil.generateRandomIndividual(teachers));
		}

		GeneticAlgorithm<Integer> ga = new GeneticAlgorithm<Integer>(turns, ExamTurnsUtil.getFiniteAlphabet(teachers),
				mutationProbability);

		/* Run for a ser amount of time (1 second) */
		Individual<Integer> bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, 1000L);

		printData(bestIndividual, fitnessFunction, goalTest, ga.getPopulationSize(), ga.getIterations(),
				ga.getTimeInMilliseconds());

		/* Run until the goal is achieved */
		bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, 0L);

		printData(bestIndividual, fitnessFunction, goalTest, ga.getPopulationSize(), ga.getIterations(),
				ga.getTimeInMilliseconds());

		ModifiedGeneticAlgorithm<Integer> modGa = new ModifiedGeneticAlgorithm<Integer>(turns,
				ExamTurnsUtil.getFiniteAlphabet(teachers), mutationProbability, reproductionProbability);

		/* Run for a ser amount of time (1 second) */
		bestIndividual = modGa.geneticAlgorithm(population, fitnessFunction, goalTest, 1000L);

		printData(bestIndividual, fitnessFunction, goalTest, ga.getPopulationSize(), ga.getIterations(),
				ga.getTimeInMilliseconds());

		/* Run until the goal is achieved */
		bestIndividual = modGa.geneticAlgorithm(population, fitnessFunction, goalTest, 1000L);

		printData(bestIndividual, fitnessFunction, goalTest, ga.getPopulationSize(), ga.getIterations(),
				ga.getTimeInMilliseconds());

	}

	private static void printData(Individual<Integer> bestIndividual, FitnessFunction<Integer> fitnessFunction,
			GoalTest goalTest, int population, int iterations, long time) {

		System.out.println("The final turns assignation is:");

		for (int i = 0; i < bestIndividual.getRepresentation().size(); i++) {
			int numTurn = i + 1;
			if (bestIndividual.getRepresentation().get(i) != 0) {
				System.out.println("Turn " + numTurn + "Professor " + bestIndividual.getRepresentation().get(i));
			} else {
				System.out.println("Turn " + numTurn + "No Professor");
			}
		}

		System.out.println("Turns           = " + turns);
		System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Is Goal         = " + goalTest.isGoalState(bestIndividual));
		System.out.println("Population Size = " + population);
		System.out.println("Iterations      = " + iterations);
		System.out.println("Took            = " + time + "ms.");
	}

	private static void getData() {
		Scanner sc = new Scanner(System.in);

		System.out.print("Introduce the number of turns needed to fill: ");

		turns = sc.nextInt();

		System.out.println("Introduce the number of professors: ");
		
		numProfessors = sc.nextInt();
		
		restrictions = new ArrayList<List<Integer>>();
		preferences = new ArrayList<List<Integer>>();
		
		sc.nextLine();
		System.out.println("Introduce the restricctions: ");
		parseLineListOfNumbers(sc, restrictions);
	
		System.out.println("Introduce the preferences: ");
		parseLineListOfNumbers(sc, preferences);

		sc.close();
	}


	private static void parseLineListOfNumbers(Scanner sc, List<List<Integer>> list) {
		String line;
		String delims = "[,]+";
		String[] tokens;
		for (int i = 0; i < numProfessors; i++) {
			line = sc.nextLine();
			tokens = line.split(delims);
			List<Integer> littleList = new ArrayList<Integer>(tokens.length);
			list.add(littleList);
			for (int j = 0; j < tokens.length; j++) {
				list.get(i).add((Integer.parseInt(tokens[j])));
			}
		}
	}

}
