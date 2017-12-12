package practica4G12;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;

public class ModifiedGeneticAlgorithm1 extends ExamTurnsGeneticAlgorithm {

	protected double reproductionProbability;

	public ModifiedGeneticAlgorithm1(int individualLength, Collection<Integer> finiteAlphabet,
			double mutationProbability, int turns, List<List<Integer>> restrictions, double reproductionProbability) {
		super(individualLength, finiteAlphabet, mutationProbability, turns, restrictions);
		this.reproductionProbability = reproductionProbability;
	}

	protected List<Individual<Integer>> nextGeneration(List<Individual<Integer>> population,
			FitnessFunction<Integer> fitnessFn) {
		List<Individual<Integer>> newPopulation = new ArrayList<Individual<Integer>>(population.size());
		for (int i = 0; i < population.size(); i++) {
			Individual<Integer> x = randomSelection(population, fitnessFn);
			Individual<Integer> y = randomSelection(population, fitnessFn);
			if (random.nextDouble() <= reproductionProbability) {
				Individual<Integer> child = reproduce(x, y);
				if (random.nextDouble() <= mutationProbability) {
					child = mutate(child);
				}
				newPopulation.add(child);
			} else {
				newPopulation.add(x);
				if (newPopulation.size() < population.size()) {
					newPopulation.add(y);
					i++;
				}
			}
		}
		return newPopulation;
	}
}
