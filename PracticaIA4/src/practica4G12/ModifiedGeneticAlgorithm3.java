package practica4G12;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;

public class ModifiedGeneticAlgorithm3 extends ModifiedGeneticAlgorithm2 {

	public ModifiedGeneticAlgorithm3(int individualLength, Collection<Integer> finiteAlphabet,
			double mutationProbability, int turns, List<List<Integer>> restrictions, double reproductionProbability) {
		super(individualLength, finiteAlphabet, mutationProbability, turns, restrictions, reproductionProbability);
	}

	protected List<Individual<Integer>> nextGeneration(List<Individual<Integer>> population,
			FitnessFunction<Integer> fitnessFn) {
		List<Individual<Integer>> newPopulation = new ArrayList<Individual<Integer>>(population.size());
		for (int i = 0; i < population.size(); i += 2) {
			Individual<Integer> x = randomSelection(population, fitnessFn);
			Individual<Integer> y = randomSelection(population, fitnessFn);
			if (random.nextDouble() <= reproductionProbability) {
				Individual<Integer> child1 = reproduce(x,y);
				Individual<Integer> child2 = reproduce(y,x);
				if (random.nextDouble() <= mutationProbability) {
					child1 = mutate(child1);
					child2 = mutate(child2);
					x = mutate(x);
					y = mutate(y);
				}
				if (fitnessFn.apply(x) + fitnessFn.apply(y) < fitnessFn.apply(child1) + fitnessFn.apply(child2)) {
					newPopulation.add(child1);
					newPopulation.add(child2);
				} else {
					newPopulation.add(x);
					newPopulation.add(y);
				}
			} else {
				newPopulation.add(x);
				newPopulation.add(y);
			}
		}
		return newPopulation;
	}

}
