package practicaIA4;

import java.util.ArrayList;
import java.util.List;


import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;


public class ExamTurnsGeneticAlg {

	public ExamTurnsGeneticAlg() {
		
	}
	
	
	static class ExamTurnsFitnessFunction implements FitnessFunction<Integer> {

		/*
		 * Representamos la solución como un array de 16 posiciones las cuales cada una
		 * representa el profesor que tiene asignado, es decir si tiene 0 no tiene
		 * ningun profesor asignado pero sitiene un numero k con 1 <= k <=
		 * numMaxProfesores. Entonces el profesor k tendrá ese turno asignado.
		 */

		/*
		 * Es necesario asignar a cada característica de la solucion un valor, para
		 * deterimnar que soluciones son mas optimas.
		 * 
		 * Si el profesor esta asignado en un
		 * turno que ni esta en sus preferencias ni en sus restricciones se le suma 1 Y
		 * si el profesor se encuentra en un puesto asignado que es una de sus
		 * preferencias se le suma 2
		 * 
		 * Luego se coge el numero de profesores distintos que hay y se multiplica el
		 * valor obtenido por ese numero, y el resultado es la funcion fitness. De esta
		 * manera cuantas mas restricciones se cumplan (es decir cuando mas +2 haya) y
		 * mas numeros de profesores distintos haya *DistProf
		 * 
		 * Hay un problema que todavia sigue sin ser del todo equilibrado.Porque puede ocurrir, 
		 * porque podria darse el caso de que un profesor tenga mas turnos asignados que el resto a pesar de que al menos todos tengan uno.
		 */

		private int teachers;
		private List<List<Integer>> preferences;
		private int turns;

		public ExamTurnsFitnessFunction(int numTeachers, int turns, List<List<Integer>> preferences) {
			this.teachers = numTeachers;
			this.preferences = preferences;
			this.turns = turns;
		}

		public double apply(Individual<Integer> indi) {
			double fitness = 0;
			int differProff = 0;
			int value;
			List<Integer> diffProf = new ArrayList<Integer>(teachers);
			
			for(int i=0; i<teachers; i++) {
				diffProf.set(i,0);
			}
			
			for (int i = 0; i < turns; i++) {
				int proff = indi.getRepresentation().get(i);
				if (proff != 0) {
					
					if((value = diffProf.get(i))==0) {//Asi contamos el numero de profesores distintos
						diffProf.set(i,value+1);
						differProff++;
					}
					
					if (preferences.get(proff - 1).contains(i)) { // Primero comprobamos si es una preferencia
						fitness = fitness + 2;
					}
					else 
						fitness = fitness + 1;
				}
			}
			
			return fitness*differProff;
		}
	}
}
