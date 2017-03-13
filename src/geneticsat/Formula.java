package geneticsat;

import geneticsat.Clause;
import geneticsat.Specimen;

public class Formula{
	int variableNum, clauseNum;
	public Clause[] clauses;

	public Formula(int varIn, int clauseIn){
		variableNum = varIn;
		clauseNum = clauseIn;
		if(varIn == 0 && clauseIn == 0)
			this.clauses = null;
		else
			clauses = new Clause[clauseNum];
	}

	public void setClause(int v1, int v2, int v3, int i){
		clauses[i] = new Clause(v1, v2, v3);
	}

	public int evaluate(boolean[] boolString){
		int successes = 0;
		boolean a, b, c;
		for(int i = 0; i < clauseNum; i++){
			a = boolString[Math.abs(clauses[i].variables[0]) - 1];
			b = boolString[Math.abs(clauses[i].variables[1]) - 1];
			c = boolString[Math.abs(clauses[i].variables[2]) - 1];
			if(clauses[i].evaluate(a, b, c)){
				successes++;
			}
		}
		return successes;
	}
}