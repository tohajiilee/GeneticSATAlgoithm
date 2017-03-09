package geneticsat;

import java.util.Random;

public class Specimen{
	int size;
	public boolean[] boolString;

	public Specimen(int sizeIn){
		Random rand = new Random();
		size = sizeIn;
		boolString = new boolean[size];
		for(int i = 0; i < boolString.length; i++){
			int currNum = rand.nextInt(10000);
			if(currNum >= 4999)
				boolString[i] = true;
			else
				boolString[i] = false;
		}
	}
}