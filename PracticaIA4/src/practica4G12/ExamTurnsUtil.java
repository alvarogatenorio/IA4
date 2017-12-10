package practica4G12;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import aima.core.search.local.Individual;

public class ExamTurnsUtil {
	public static Individual<Integer> generateRandomIndividual(int teachers) {
		List<Integer> representation = new ArrayList<Integer>(Main.TOTAL_TURNS);
		for (int i = 0; i < Main.TOTAL_TURNS; i++) {
			representation.set(i, new Random().nextInt(teachers));
		}
		return new Individual<Integer>(representation);
	}

	public static Collection<Integer> getFiniteAlphabet(int teachers) {
		List<Integer> alphabet = new ArrayList<Integer>(teachers);
		for (int i = 0; i < teachers; i++) {
			alphabet.set(i, i);
		}
		return alphabet;
	}
}
