package practica4G12;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aima.core.search.framework.problem.GoalTest;
import aima.core.search.local.FitnessFunction;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.Individual;

public class Main {
	
	/*Falta hacer la entrada/salida para poder probar los casos*/
	
	public static final int TOTAL_TURNS = 16;

	private static int turns;
	private static List<List<Integer>> restrictions;
	private static List<List<Integer>> preferences;
	private static List<Integer> turnsPerTeacher;

	private static int mutationProbability;

	public static void main(String[] args) {
		System.out.println("Hello");
		examTurnsGeneticAlgorithmSearch();
	}

	public static void examTurnsGeneticAlgorithmSearch() {
		FitnessFunction<Integer> fitnessFunction = new ExamTurnsFitnessFunction(turns, restrictions, preferences,
				turnsPerTeacher);
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
		//falta imprimir el estado
		System.out.println("Turns           = " + turns);
		System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Is Goal         = " + goalTest.isGoalState(bestIndividual));
		System.out.println("Population Size = " + ga.getPopulationSize());
		System.out.println("Iterations      = " + ga.getIterations());
		System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");

		/* Run until the goal is achieved */
		bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, 0L);
		//falta imprimir el estado
		System.out.println("Turns           = " + turns);
		System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Is Goal         = " + goalTest.isGoalState(bestIndividual));
		System.out.println("Population Size = " + ga.getPopulationSize());
		System.out.println("Iterations      = " + ga.getIterations());
		System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");
	}
}
