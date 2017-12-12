package practica4G12;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import aima.core.search.local.Individual;

public class ExamTurnsUtil {
	
	public static Individual<Integer> generateRandomIndividual(int turns, int professors,
			List<List<Integer>> restrictions) {
		/* Initializing stuff */
		int currentTurns = 0;
		List<Integer> representation = new ArrayList<Integer>();
		List<Boolean> infeasibleTurns = new ArrayList<Boolean>();
		int currentInfeasibleTurns = 0;
		for (int i = 0; i < Main.TOTAL_TURNS; i++) {
			infeasibleTurns.add(false);
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
			while (restrictions.get(randomProfessor).contains(randomPosition) && !infeasibleTurns.get(randomPosition)) {
				randomProfessor = (randomProfessor + 1) % professors;
				if (randomProfessor == aux) {
					infeasibleTurns.set(randomPosition, true);
					currentInfeasibleTurns++;
				}
			}

			/* Update stuff */
			if (!infeasibleTurns.get(randomPosition)) {
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
