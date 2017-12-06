package practicaIA4;

import java.util.List;

import aima.core.search.framework.problem.GoalTest;
import aima.core.search.local.Individual;

public class ExamTurnsGoalTest implements GoalTest {

	private int turns;
	private List<List<Integer>> restrictions;

	public ExamTurnsGoalTest( int turns, List<List<Integer>> restrictions) {
		this.turns = turns;
		this.restrictions = restrictions;

	}
	
	/* 
	 * La función objetivo consiste en recorrer al individuo, comprobando si algunos de los profesores asignados,
	 * esta asignado a un turno perteneciente a sus restricciones, y con lo cual erroneo. 
	 * Además se comprueba si el numero de turnos coindice con el numero de turnos necesarrios es igual al numero de turnos asignados.
	 * 
	 */

	public boolean isGoalState(Object arg) {
		@SuppressWarnings("unchecked")
		Individual<Integer> indi = (Individual<Integer>) arg;  
		int assigned=0;
		
		for (int i = 0; i < turns; i++) {
			int proff = indi.getRepresentation().get(i);
			if (proff != 0) {
				assigned++;
				if (restrictions.get(proff - 1).contains(i)) {
					return false;
				}
			}
		}
		return turns == assigned;
	}
	
}
