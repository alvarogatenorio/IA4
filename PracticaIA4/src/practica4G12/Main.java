package practica4G12;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

	public static final int TOTAL_TURNS = 16;

	private static int turns;
	private static List<List<Integer>> restrictions;
	private static List<List<Integer>> preferences;
	private static List<String> professorsNames;
	private static int professors;
	private static File file;

	private static double mutationProbability;
	private static double reproductionProbability;

	private static Individual<Integer> best;
	private static double worst;
	private static double average;
	private static int executions;
	private static long timeLimit;
	private static double fitnessChampion;

	public static void main(String[] args) {
		System.out.println("Introduce the name of the file");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();
		file = new File(name);
		getData(file);
		best = null;
		worst = -1;
		average = 0;
		fitnessChampion = 0;
		executions = 50;
		timeLimit = 250L;
		reproductionProbability = 0.7;
		mutationProbability = 0.15;
		examTurnsGeneticAlgorithmSearch();
	}

	public static void examTurnsGeneticAlgorithmSearch() {
		FitnessFunction<Integer> fitnessFunction = new ExamTurnsFitnessFunction(turns, restrictions, preferences);
		GoalTest goalTest = new ExamTurnsGoalTest(turns, restrictions, preferences);
		Set<Individual<Integer>> population = generateRandomPopulation();
		if (population != null) {
			execute(fitnessFunction, goalTest, new ExamTurnsGeneticAlgorithm(TOTAL_TURNS,
					ExamTurnsUtil.getFiniteAlphabet(professors), mutationProbability, turns, restrictions), "NORMAL");
			execute(fitnessFunction, goalTest,
					new ModifiedGeneticAlgorithm1(TOTAL_TURNS, ExamTurnsUtil.getFiniteAlphabet(professors),
							mutationProbability, turns, restrictions, reproductionProbability),
					"MOD1");
			execute(fitnessFunction, goalTest,
					new ModifiedGeneticAlgorithm2(TOTAL_TURNS, ExamTurnsUtil.getFiniteAlphabet(professors),
							mutationProbability, turns, restrictions, reproductionProbability),
					"MOD2");
			execute(fitnessFunction, goalTest,
					new ModifiedGeneticAlgorithm3(TOTAL_TURNS, ExamTurnsUtil.getFiniteAlphabet(professors),
							mutationProbability, turns, restrictions, reproductionProbability),
					"MOD3");
		} else {
			System.out.println("-----INFEASIBLE!!!-----");
		}
	}

	private static void execute(FitnessFunction<Integer> fitnessFunction, GoalTest goalTest,
			GeneticAlgorithm<Integer> ga, String name) {
		/* Executing */
		Individual<Integer> bestIndividual;
		for (int i = 0; i < executions; i++) {
			Set<Individual<Integer>> population = generateRandomPopulation();
			bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, timeLimit);
			double fitnessBest = fitnessFunction.apply(bestIndividual);
			average += fitnessBest;
			/* Checking for the best and the worst */
			if (best == null) {
				best = bestIndividual;
				fitnessChampion = fitnessBest;
				worst = fitnessBest;
			} else if (fitnessBest >= fitnessChampion) {
				best = bestIndividual;
				fitnessChampion = fitnessBest;
			}
			/* Checking for the worst */
			if (worst >= fitnessBest) {
				worst = fitnessBest;
			}
		}

		/* Printing data */
		printData(fitnessFunction, goalTest, ga, name, false);

		/* Keeping the values coherent for further calls */
		best = null;
		worst = -1;
		average = 0;
		fitnessChampion = 0;
	}

	private static Set<Individual<Integer>> generateRandomPopulation() {
		Set<Individual<Integer>> population = new HashSet<Individual<Integer>>();
		for (int i = 0; i < 50; i++) {
			Individual<Integer> randomIndividual = ExamTurnsUtil.generateRandomIndividual(turns, professors,
					restrictions);
			if (randomIndividual != null) {
				population.add(randomIndividual);
			} else {
				return null;
			}
		}
		return population;
	}
	
	/*-----PRINT SYSTEM-----*/
	
	private static void printData(FitnessFunction<Integer> fitnessFunction, GoalTest goalTest,
			GeneticAlgorithm<Integer> ga, String name, boolean extensive) {
		average /= executions;
		System.out.println("-----" + name + "-----");
		printDataAux(best, fitnessFunction, goalTest, ga.getPopulationSize(), ga.getIterations(),
				ga.getTimeInMilliseconds(), extensive);
	}
	
	private static void printDataAux(Individual<Integer> bestIndividual, FitnessFunction<Integer> fitnessFunction,
			GoalTest goalTest, int population, int iterations, long time, boolean extensive) {

		if (extensive) {
			printIndividual(bestIndividual);
		}

		System.out.println("Fitness best    : " + fitnessFunction.apply(bestIndividual));
		System.out.println("Fitness worst   : " + worst);
		System.out.println("Average fitness : " + average);
		System.out.println("Goal reached    : " + goalTest.isGoalState(bestIndividual));
		System.out.println("Iterations best : " + iterations);
		System.out.println("Time best       : " + time + "ms.");
		System.out.println();
	}

	private static void printIndividual(Individual<Integer> bestIndividual) {
		System.out.println("The final turns assignation is:");
		int numTurn = 0;
		int numProfessor = 0;
		for (int i = 0; i < bestIndividual.getRepresentation().size(); i++) {
			numTurn = i + 1;
			if (bestIndividual.getRepresentation().get(i) == null) {
				System.out.println("Turn " + numTurn + " No Professor");
			} else {
				numProfessor = bestIndividual.getRepresentation().get(i) + 1;
				System.out.println("Turn " + numTurn + " Professor " + numProfessor);
			}
		}
	}

	/*-----INPUT SYSTEM-----*/

	private static void getData(File file) {
		FileReader fileR = null;
		BufferedReader buffRead = null;

		try {
			fileR = new FileReader(file);
			buffRead = new BufferedReader(fileR);
		} catch (FileNotFoundException e) {
			System.out.println("File" + file.getName() + " not in scope.");
		}

		try {
			turns = Integer.parseInt(buffRead.readLine());

			professorsNames = new ArrayList<String>();

			parseLineListOfProfessors(buffRead, professorsNames);

			professors = professorsNames.size();

			restrictions = new ArrayList<List<Integer>>();
			preferences = new ArrayList<List<Integer>>();

			parseLineListOfNumbers(buffRead, restrictions);

			parseLineListOfNumbers(buffRead, preferences);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void parseLineListOfNumbers(BufferedReader buffRead, List<List<Integer>> list) throws IOException {
		String line;
		String delims = "[ :,]+";
		String[] tokens;
		for (int i = 0; i < professors; i++) {
			line = buffRead.readLine();
			tokens = line.split(delims);
			List<Integer> littleList = new ArrayList<Integer>(tokens.length);
			list.add(littleList);
			for (int j = 1; j < tokens.length; j++) {
				list.get(i).add((Integer.parseInt(tokens[j])));
			}
		}
	}

	private static void parseLineListOfProfessors(BufferedReader buffRead, List<String> list) throws IOException {
		String line;
		String delims = "[,]+";
		String[] tokens;
		line = buffRead.readLine();
		tokens = line.split(delims);
		for (int j = 0; j < tokens.length; j++) {
			list.add(tokens[j]);
		}
	}

}
