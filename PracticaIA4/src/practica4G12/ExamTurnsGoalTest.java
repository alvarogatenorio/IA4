package practica4G12;

import java.util.List;

import aima.core.search.framework.problem.GoalTest;
import aima.core.search.local.Individual;

public class ExamTurnsGoalTest implements GoalTest {

	private int turns;
	private List<List<Integer>> restrictions;

	public ExamTurnsGoalTest(int turns, List<List<Integer>> restrictions) {
		this.turns = turns;
		this.restrictions = restrictions;
	}

	@Override
	public boolean isGoalState(Object individual) {
		@SuppressWarnings("unchecked")
		List<Integer> representation = ((Individual<Integer>) individual).getRepresentation();
		int assigned = 0;
		for (int i = 0; i < representation.size(); i++) {
			Integer teacher = representation.get(i);
			if (teacher != 0) {
				List<Integer> teacherRestrictions = restrictions.get(teacher - 1);
				/*
				 * If we assume the restriction list is sorted, we can improve this with binary
				 * search
				 */
				if (teacherRestrictions.contains(i)) {
					return false;
				}
				assigned++;
			}
		}
		return assigned == this.turns;
	}
	
}
