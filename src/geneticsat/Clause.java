package geneticsat;

/*
 * For this particular variation of Genetic SAT, there is guaranteed to be 3 variables per clause.
 */

public class Clause{
	public int[] variables;

	public Clause(){
		variables = new int[3];
	}

	public Clause(int v1, int v2, int v3){
		variables = new int[3];
		variables[0] = v1;
		variables[1] = v2;
		variables[2] = v3;
	}

	public boolean evaluate(boolean a, boolean b, boolean c){
		for(int i = 0; i < variables.length; i++){
			boolean examvar = false;
			switch(i){
				case 0:
					examvar = a;
					break;
				case 1:
					examvar = b;
					break;
				case 2:
					examvar = c;
					break;
				default:												// This should never occur
					break;
			}
			if(variables[i] < 0){
				if(!examvar)
					return true;
			}
			else{
				if(examvar)
					return true;
			}
		}
		return false;
	}
}