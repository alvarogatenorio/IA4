package practica4G12;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.Individual;

public class ExamTurnsGeneticAlgorithm extends GeneticAlgorithm<Integer> {

	protected int turns;
	private List<List<Integer>> restrictions;

	public ExamTurnsGeneticAlgorithm(int individualLength, Collection<Integer> finiteAlphabet,
			double mutationProbability, int turns, List<List<Integer>> restrictions) {
		super(individualLength, finiteAlphabet, mutationProbability);
		this.turns = turns;
		this.restrictions = restrictions;
	}

	protected Individual<Integer> reproduce(Individual<Integer> x, Individual<Integer> y) {
		int currentTurns = 0;
		int randomPosition = new Random().nextInt(Main.TOTAL_TURNS);
		List<Integer> father1 = x.getRepresentation();
		List<Integer> father2 = y.getRepresentation();
		List<Integer> child = new ArrayList<Integer>();

		/* Filling the child */
		for (int i = 0; i < individualLength; i++) {
			child.add(null);
		}
		/* Copying the head of one father */
		for (int i = 0; i < randomPosition; i++) {
			child.set(i, father1.get(i));
			if (father1.get(i) != null) {
				currentTurns++;
			}
		}
		/* Copying the tail of the other */
		for (int i = randomPosition; i < individualLength && currentTurns < this.turns; i++) {
			child.set(i, father2.get(i));

			if (father2.get(i) != null) {
				currentTurns++;
			}
		}
		/* Filling possible gaps */
		int i = randomPosition;
		while (currentTurns < this.turns) {
			if (child.get(i) == null && father1.get(i) != null) {
				child.set(i, father1.get(i));
				currentTurns++;
			}
			i++;
		}
		return new Individual<Integer>(child);
	}

	/*
	 * Provisional implementation of reproduce, the main disadvantage is that
	 * assigned turns will probably tend to be the first ones
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private Individual<Integer> provisionalReproduce(Individual<Integer> x, Individual<Integer> y) {
		int currentTurns = 0;
		List<Integer> father1 = x.getRepresentation();
		List<Integer> father2 = y.getRepresentation();
		List<Integer> son = new ArrayList<Integer>(father1.size());
		for (int i = 0; i < son.size(); i++) {
			if (currentTurns < turns && father1.get(i) != null) {
				son.add(father1.get(i));
				currentTurns++;
			} else if (currentTurns < turns && father2.get(i) != null) {
				son.add(father2.get(i));
				currentTurns++;
			} else {
				son.add(null);
			}
		}
		return new Individual<Integer>(son);
	}

	protected Individual<Integer> mutate(Individual<Integer> child) {
		List<Integer> representation = new ArrayList<Integer>();
		representation.addAll(child.getRepresentation());
		boolean done = false;
		int currentInfeasibleTurns = 0;
		while (!done && currentInfeasibleTurns <= (individualLength - this.turns)) {
			/* Get a random null turn */
			int randomPosition = new Random().nextInt(individualLength);
			while (representation.get(randomPosition) != null) {
				randomPosition = (randomPosition + 1) % individualLength;
			}
			/* Put a random valid teacher */
			int randomTeacher = new Random().nextInt(finiteAlphabet.size() - 1);
			int aux = randomTeacher;
			boolean infeasible = false;
			while (restrictions.get(randomTeacher) != null
					&& restrictions.get(randomTeacher).contains(randomPosition)) {
				randomTeacher = (randomTeacher + 1) % (finiteAlphabet.size() - 1);
				if (randomTeacher == aux) {
					infeasible = true;
					currentInfeasibleTurns++;
					break;
				}
			}
			if (!infeasible) {
				representation.set(randomPosition, randomTeacher);
				done = true;
			}
		}
		if (done) {
			/* Erase some random turn */
			int randomPosition = new Random().nextInt(individualLength);
			while (representation.get(randomPosition) == null) {
				randomPosition = (randomPosition + 1) % individualLength;
			}
			representation.set(randomPosition, null);
		}
		return new Individual<Integer>(representation);
	}
}
