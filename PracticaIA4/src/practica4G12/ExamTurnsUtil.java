package practica4G12;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import aima.core.search.local.Individual;

public class ExamTurnsUtil {
	public static Individual<Integer> generateRandomIndividual(int turns, int professors,
			List<List<Integer>> restrictions, Boolean infeasible) {
		int currentTurns = 0;
		int currentInfeasibleTurns = 0;
		infeasible = false;
		List<Integer> representation = new ArrayList<Integer>(Main.TOTAL_TURNS);

		while (currentTurns < turns && currentInfeasibleTurns <= (Main.TOTAL_TURNS - turns)) {
			/* Pick a random null turn */
			int randomPosition = new Random().nextInt(Main.TOTAL_TURNS);
			while (representation.get(randomPosition) != null) {
				randomPosition = (randomPosition + 1) % Main.TOTAL_TURNS;
			}

			/* Pick a random valid teacher for the turn */
			int randomProfessor = new Random().nextInt(professors);
			int aux = randomProfessor;
			boolean turnInfeasible = false;
			while (restrictions.get(randomProfessor).contains(randomPosition)) {
				randomProfessor = (randomProfessor + 1) % professors;
				if (randomProfessor == aux) {
					turnInfeasible = true;
					currentInfeasibleTurns++;
					break;
				}
			}

			/* Update stuff */
			if (!turnInfeasible) {
				representation.set(randomPosition, randomProfessor);
				currentTurns++;
			}
		}

		return new Individual<Integer>(representation);
	}

	public static Collection<Integer> getFiniteAlphabet(int teachers) {
		List<Integer> alphabet = new ArrayList<Integer>(teachers + 1);
		for (int i = 0; i < teachers; i++) {
			alphabet.set(i, i);
		}
		alphabet.set(teachers, null);
		return alphabet;
	}
}
