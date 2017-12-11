package practica4G12;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;

public class ExamTurnsFitnessFunction implements FitnessFunction<Integer> {

	private int turns;
	private List<List<Integer>> restrictions;
	private List<List<Integer>> preferences;
	private List<Integer> turnsPerTeacher;

	public ExamTurnsFitnessFunction(int turns, List<List<Integer>> restrictions, List<List<Integer>> preferences) {
		this.turns = turns;
		this.restrictions = restrictions;
		this.preferences = preferences;
	}

	@Override
	public double apply(Individual<Integer> individual) {
		List<Integer> representation = individual.getRepresentation();
		int fitness = turnsAssigned(representation);
		if (fitness == turns) {
			fitness = penalizeImbalance(fitness);
			fitness -= bonusPreferences(representation);
		}
		return (double) fitness;
	}

	/*
	 * Returns the number of turns "really" assigned (counting restrictions), also
	 * updates the turnsPerTeacher list.
	 */
	private int turnsAssigned(List<Integer> representation) {
		this.turnsPerTeacher = new ArrayList<Integer>();
		for (int i=0; i< this.restrictions.size(); i++) {
			this.turnsPerTeacher.add(null);
		}
		int turns = 0;
		for (int i = 0; i < representation.size(); i++) {
			Integer teacher = representation.get(i);
			if (teacher != null) {
				List<Integer> teacherRestrictions = restrictions.get(teacher);
				/*
				 * If we assume the restriction list is sorted, we can improve this with binary
				 * search
				 */
				if (!teacherRestrictions.contains(i)) {
					turns++;
					if (this.turnsPerTeacher.get(teacher) != null) {
						this.turnsPerTeacher.set(teacher, this.turnsPerTeacher.get(teacher) + 1);
					} else {
						this.turnsPerTeacher.set(teacher, 1);
					}
				}
			}
		}
		return turns;
	}

	/* Penalize those individuals with imbalances */
	private int penalizeImbalance(int fitness) {
		fitness *= 2;
		int teachers = this.turnsPerTeacher.size();
		int balance = turns % teachers == 0 ? turns / teachers : turns / teachers + 1;
		for (int i = 0; i < teachers; i++) {
			if (this.turnsPerTeacher.get(i) != null) {
				fitness -= Math.max(0, this.turnsPerTeacher.get(i) - balance);
			}
		}
		return fitness;
	}

	/* Bonus those individuals with matching preferences */
	private int bonusPreferences(List<Integer> representation) {
		int bonus = 0;
		for (int i = 0; i < representation.size(); i++) {
			Integer teacher = representation.get(i);
			if (teacher != null) {
				List<Integer> teacherPreferences = preferences.get(teacher);
				/*
				 * If we assume the restriction list is sorted, we can improve this with binary
				 * search
				 */
				if (teacherPreferences.contains(i)) {
					bonus++;
				}
			}
		}
		return bonus;
	}
}
