package practica4G12;

import java.util.List;

import aima.core.search.framework.problem.GoalTest;
import aima.core.search.local.Individual;

public class ExamTurnsGoalTest implements GoalTest {

	private int turns;
	private List<List<Integer>> restrictions;
	private List<List<Integer>> preferences;

	public ExamTurnsGoalTest(int turns, List<List<Integer>> restrictions, List<List<Integer>> preferences) {
		this.turns = turns;
		this.restrictions = restrictions;
		this.preferences = preferences;
	}

	/* One interpretation of optimality */
	@Override
	public boolean isGoalState(Object individual) {
		@SuppressWarnings("unchecked")
		Individual<Integer> indiv = ((Individual<Integer>) individual);
		return new ExamTurnsFitnessFunction(this.turns, this.restrictions, this.preferences).apply(indiv) == 3 * turns;
	}
}
