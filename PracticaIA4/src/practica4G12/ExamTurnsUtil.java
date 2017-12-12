package practica4G12;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import aima.core.search.local.Individual;

public class ExamTurnsUtil {
	public static Individual<Integer> generateRandomIndividual(int turns, int professors,
			List<List<Integer>> restrictions) {
		int currentTurns = 0;
		int currentInfeasibleTurns = 0;
		List<Integer> representation = new ArrayList<Integer>();
		for (int i = 0; i < Main.TOTAL_TURNS; i++) {
			representation.add(null);
		}

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
		if (currentInfeasibleTurns > Main.TOTAL_TURNS - turns) {
			return null;
		}
		return new Individual<Integer>(representation);
	}

	public static Collection<Integer> getFiniteAlphabet(int teachers) {
		List<Integer> alphabet = new ArrayList<Integer>();
		for (int i = 0; i < teachers; i++) {
			alphabet.add(i);
		}
		alphabet.add(null);
		return alphabet;
	}
}
