package practica4G12;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;

public class ModifiedGeneticAlgorithm2 extends ModifiedGeneticAlgorithm1 {

	public ModifiedGeneticAlgorithm2(int individualLength, Collection<Integer> finiteAlphabet,
			double mutationProbability, int turns) {
		super(individualLength, finiteAlphabet, mutationProbability, turns);
	}

	protected List<Individual<Integer>> nextGeneration(List<Individual<Integer>> population,
			FitnessFunction<Integer> fitnessFn) {
		List<Individual<Integer>> newPopulation = new ArrayList<Individual<Integer>>(population.size());
		for (int i = 0; i < population.size(); i += 2) {
			Individual<Integer> x = randomSelection(population, fitnessFn);
			Individual<Integer> y = randomSelection(population, fitnessFn);
			if (random.nextDouble() <= reproductionProbability) {
				Individual<Integer> children = reproduce(x, y);
				List<Integer> representation = children.getRepresentation();
				Individual<Integer> child1 = new Individual<Integer>(representation.subList(0, individualLength));
				Individual<Integer> child2 = new Individual<Integer>(
						representation.subList(individualLength, representation.size()));
				if (random.nextDouble() <= mutationProbability) {
					child1 = mutate(child1);
					child2 = mutate(child2);
				}
				newPopulation.add(child1);
				newPopulation.add(child2);
			} else {
				newPopulation.add(x);
				newPopulation.add(y);
			}
		}
		return newPopulation;
	}

	protected Individual<Integer> reproduce(Individual<Integer> x, Individual<Integer> y) {
		int currentTurns = 0;
		int randomPosition = new Random().nextInt(Main.TOTAL_TURNS);
		List<Integer> father1 = x.getRepresentation();
		List<Integer> father2 = y.getRepresentation();
		List<Integer> child = new ArrayList<Integer>();
		/* Child1 process */
		for (int i = 0; i < randomPosition; i++) {
			child.add(father1.get(i));
			if (father1.get(i) != null) {
				currentTurns++;
			}
		}
		for (int i = randomPosition; i < individualLength && currentTurns < this.turns; i++) {
			child.add(father2.get(i));
			if (father2.get(i) != null) {
				currentTurns++;
			}
		}
		int k = randomPosition;
		while (currentTurns < this.turns) {
			if (child.get(k) == null && father1.get(k) != null) {
				child.set(k, father1.get(k));
				currentTurns++;
			}
			k++;
		}

		currentTurns = 0;
		/* Child2 process */
		for (int i = randomPosition; i < individualLength; i++) {
			child.add(father2.get(i));
			if (father2.get(i) != null) {
				currentTurns++;
			}
		}
		for (int i = 0; i < randomPosition && currentTurns < this.turns; i++) {
			child.add(father1.get(i));
			if (father1.get(i) != null) {
				currentTurns++;
			}
		}
		k = individualLength + randomPosition;
		while (currentTurns < this.turns) {
			if (child.get(k) == null && father2.get(k - individualLength) != null) {
				child.set(k, father2.get(k - individualLength));
				currentTurns++;
			}
			k++;
		}
		return new Individual<Integer>(child);
	}

}
