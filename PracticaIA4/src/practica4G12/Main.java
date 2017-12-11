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

	private static int mutationProbability;
	private static int reproductionProbability;

	public static void main(String[] args) {
		System.out.println("Introduce the name of the file");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();
		file = new File(name);
		getData(file);
		examTurnsGeneticAlgorithmSearch();
	}

	public static void examTurnsGeneticAlgorithmSearch() {
		FitnessFunction<Integer> fitnessFunction = new ExamTurnsFitnessFunction(turns, restrictions, preferences);
		GoalTest goalTest = new ExamTurnsGoalTest(turns, restrictions, preferences);
		/* Generate an initial population */
		Set<Individual<Integer>> population = new HashSet<Individual<Integer>>();
		Boolean infeasible = false;
		for (int i = 0; i < 50; i++) {
			population.add(ExamTurnsUtil.generateRandomIndividual(turns, professors, restrictions, infeasible));
			if (infeasible) {
				System.out.println("-----INFEASIBLE!!!-----");
				break;
			}
		}

		if (!infeasible) {
			GeneticAlgorithm<Integer> ga = new GeneticAlgorithm<Integer>(TOTAL_TURNS,
					ExamTurnsUtil.getFiniteAlphabet(professors), mutationProbability);

			/* Run for a ser amount of time (1 second) */
			Individual<Integer> bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, 1000L);

			printData(bestIndividual, fitnessFunction, goalTest, ga.getPopulationSize(), ga.getIterations(),
					ga.getTimeInMilliseconds());

			/* Run until the goal is achieved */
			bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, 0L);

			printData(bestIndividual, fitnessFunction, goalTest, ga.getPopulationSize(), ga.getIterations(),
					ga.getTimeInMilliseconds());

			ModifiedGeneticAlgorithm1 modGa = new ModifiedGeneticAlgorithm1(TOTAL_TURNS,
					ExamTurnsUtil.getFiniteAlphabet(professors), mutationProbability, reproductionProbability);

			/* Run for a set amount of time (1 second) */
			bestIndividual = modGa.geneticAlgorithm(population, fitnessFunction, goalTest, 1000L);

			printData(bestIndividual, fitnessFunction, goalTest, ga.getPopulationSize(), ga.getIterations(),
					ga.getTimeInMilliseconds());

			/* Run until the goal is achieved */
			bestIndividual = modGa.geneticAlgorithm(population, fitnessFunction, goalTest, 1000L);

			printData(bestIndividual, fitnessFunction, goalTest, ga.getPopulationSize(), ga.getIterations(),
					ga.getTimeInMilliseconds());
		}
	}

	private static void printData(Individual<Integer> bestIndividual, FitnessFunction<Integer> fitnessFunction,
			GoalTest goalTest, int population, int iterations, long time) {

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

		System.out.println("Turns           = " + turns);
		System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Is Goal         = " + goalTest.isGoalState(bestIndividual));
		System.out.println("Population Size = " + population);
		System.out.println("Iterations      = " + iterations);
		System.out.println("Took            = " + time + "ms.");
	}

	/* Gets the data from the standard input */
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
