package geneticsat;

import java.util.Random;

public class Specimen{
	int size;
	int successes;
	private double[] normVal;
	private boolean[] boolString;
	private boolean[] reached;

	public Specimen(int sizeIn){
		Random rand = new Random();
		size = sizeIn;
		successes = 0;
		normVal = new double[10];
		setBoolString(new boolean[size]);
		setReached(new boolean[size]);
		for(int i = 0; i < getBoolString().length; i++){
			int currNum = rand.nextInt(10000);
			if(currNum >= 4999)
				getBoolString()[i] = true;
			else
				getBoolString()[i] = false;
		}
	}

	public void flip(int i){
		if(getBoolString()[i])
			getBoolString()[i] = false;
		else
			getBoolString()[i] = true;
	}

	public void printString(){
		for(int i = 0; i < size; i++){
			if(getBoolString()[i])
				System.out.print("T");
			else
				System.out.print("F");
		}
		System.out.print("\n");
	}

	public boolean[] getBoolString() {
		return boolString;
	}

	public void setBoolString(boolean[] boolString) {
		this.boolString = boolString;
	}

	public boolean[] getReached() {
		return reached;
	}

	public void setReached(boolean[] reached) {
		this.reached = reached;
	}

	public double getNormVal(int i) {
		return normVal[i];
	}

	public void setNormVal(double normVal, int i) {
		this.normVal[i] = normVal;
	}
}