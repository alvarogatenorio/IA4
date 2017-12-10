package practica4G12;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.Individual;

public class ModifiedGeneticAlgorithm<A> extends GeneticAlgorithm<A> {

	protected double reproductionProbability;

	public ModifiedGeneticAlgorithm(int individualLength, Collection<A> finiteAlphabet, double mutationProbability,
			double reproductionProbability) {
		super(individualLength, finiteAlphabet, mutationProbability);
		this.reproductionProbability = reproductionProbability;
	}

	public ModifiedGeneticAlgorithm(int individualLength, Collection<A> finiteAlphabet, double mutationProbability,
			Random random, double reproductionProbability) {
		super(individualLength, finiteAlphabet, mutationProbability, random);
		this.reproductionProbability = reproductionProbability;
	}

	protected List<Individual<A>> nextGeneration(List<Individual<A>> population, FitnessFunction<A> fitnessFn) {
		// return nextGeneration1(population, fitnessFn);
		// return nextGeneration2(population, fitnessFn);
		return nextGeneration3(population, fitnessFn);
	}

	protected Individual<A> reproduce(Individual<A> x, Individual<A> y) {
		return reproduce1(x, y);
	}

	/*
	 * Just takes into account the reproduction probability, with the original
	 * reproduction method.
	 */
	private List<Individual<A>> nextGeneration1(List<Individual<A>> population, FitnessFunction<A> fitnessFn) {
		List<Individual<A>> newPopulation = new ArrayList<Individual<A>>(population.size());
		for (int i = 0; i < population.size(); i++) {
			Individual<A> x = randomSelection(population, fitnessFn);
			Individual<A> y = randomSelection(population, fitnessFn);
			if (random.nextDouble() <= reproductionProbability) {
				Individual<A> child = super.reproduce(x, y);
				if (random.nextDouble() <= mutationProbability) {
					child = mutate(child);
				}
				newPopulation.add(child);
			} else {
				newPopulation.add(x);
				newPopulation.add(y);
				i++;
			}
		}
		return newPopulation;
	}

	private Individual<A> reproduce1(Individual<A> x, Individual<A> y) {
		int c = randomOffset(individualLength);
		// return APPEND(SUBSTRING(x, 1, c), SUBSTRING(y, c+1, n))
		List<A> childrenRepresentation = new ArrayList<A>();
		/* first child */
		childrenRepresentation.addAll(x.getRepresentation().subList(0, c));
		childrenRepresentation.addAll(y.getRepresentation().subList(c, individualLength));
		/* second child */
		childrenRepresentation.addAll(x.getRepresentation().subList(c, individualLength));
		childrenRepresentation.addAll(y.getRepresentation().subList(0, c));
		Individual<A> children = new Individual<A>(childrenRepresentation);
		return children;
	}

	/*
	 * Just takes into account the reproduction probability, with the new
	 * reproduction method.
	 */
	private List<Individual<A>> nextGeneration2(List<Individual<A>> population, FitnessFunction<A> fitnessFn) {
		List<Individual<A>> newPopulation = new ArrayList<Individual<A>>(population.size());
		for (int i = 0; i < population.size(); i += 2) {
			Individual<A> x = randomSelection(population, fitnessFn);
			Individual<A> y = randomSelection(population, fitnessFn);
			if (random.nextDouble() <= reproductionProbability) {
				Individual<A> children = reproduce(x, y);
				if (random.nextDouble() <= mutationProbability) {
					children = mutate(children);
				}
				List<A> representation = children.getRepresentation();
				Individual<A> child1 = new Individual<A>(representation.subList(0, individualLength));
				Individual<A> child2 = new Individual<A>(
						representation.subList(individualLength, representation.size()));
				newPopulation.add(child1);
				newPopulation.add(child2);
			} else {
				newPopulation.add(x);
				newPopulation.add(y);
			}
		}
		return newPopulation;
	}

	/*
	 * Just takes into account the reproduction probability, with the new
	 * reproduction method and a non destructive strategy
	 */
	private List<Individual<A>> nextGeneration3(List<Individual<A>> population, FitnessFunction<A> fitnessFn) {
		List<Individual<A>> newPopulation = new ArrayList<Individual<A>>(population.size());
		for (int i = 0; i < population.size(); i += 2) {
			Individual<A> x = randomSelection(population, fitnessFn);
			Individual<A> y = randomSelection(population, fitnessFn);
			if (random.nextDouble() <= reproductionProbability) {
				Individual<A> children = reproduce(x, y);
				if (random.nextDouble() <= mutationProbability) {
					children = mutate(children);
				}
				List<A> representation = children.getRepresentation();
				Individual<A> child1 = new Individual<A>(representation.subList(0, individualLength));
				Individual<A> child2 = new Individual<A>(
						representation.subList(individualLength, representation.size()));
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

	// las partes opcionales 4 5 6 están a huevo y no hacerlas sería de gilipollas
}
