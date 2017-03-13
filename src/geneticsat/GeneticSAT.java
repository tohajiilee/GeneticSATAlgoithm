package geneticsat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

import geneticsat.Formula;

public class GeneticSAT {

	public static int numFlips;
	public static double timeTotal;

	public static void main(String[] args) {
		Specimen[] spArray = null;
		Scanner input = new Scanner(System.in);
		String command, filename;
		File file = null;
		Formula formula = null;
		char cont = ' ';
		numFlips = 0;
		while (true) {
			System.out.println("A: 20 Variables\nB: 50 Variables\nC: 75 Variables\nD: 100 Variables");
			System.out.print("Please select which number of variables to use: ");
			command = input.nextLine();
			command.toUpperCase();
			switch (command) {
			case "A":
				filename = "uf20-01.cnf";
				file = new File(filename);
				cont = 'a';
				break;
			case "B":
				filename = "uf50-01.cnf";
				file = new File(filename);
				cont = 'b';
				break;
			case "C":
				filename = "uf75-01.cnf";
				file = new File(filename);
				cont = 'c';
				break;
			case "D":
				filename = "uf100-01.cnf";
				file = new File(filename);
				cont = 'd';
				break;
			default:
				System.out.println("Please enter any letter from A-D.");
				continue;
			}
			break;
		}
		for (int x = 2; x < 101; x++) {
			long startTime = 0; long endTime; double msEndTime;
			startTime = System.nanoTime();
			if (file != null) {
				formula = parseFile(file);
				if (formula != null) {
					spArray = new Specimen[10];
					for (int i = 0; i < 10; i++) {
						spArray[i] = new Specimen(formula.variableNum);
					}
					int maxSuccess = 0;
					int penulSuccess = 0;
					int maxIndex = 0;
					int penulIndex = 0;
					while (true) {

						for (int i = 0; i < 10; i++)
							spArray[i].successes = formula.evaluate(spArray[i].getBoolString());
						for (int i = 0; i < 10; i++) {
							if (spArray[i].successes > maxSuccess) {
								penulSuccess = maxSuccess;
								penulIndex = maxIndex;
								maxSuccess = spArray[i].successes;
								maxIndex = i;
							} else if (spArray[i].successes > penulSuccess && spArray[i].successes < maxSuccess) {
								penulSuccess = spArray[i].successes;
								penulIndex = i;
							}
						}
						if (spArray[maxIndex].successes == formula.clauseNum) {
							endTime = System.nanoTime() - startTime;
							msEndTime = (double)endTime / 1000000;
							System.out.println("Complete. Flips: " + numFlips);
							System.out.println("Time: " + msEndTime + "ms");
							timeTotal += msEndTime;
							break;
						}
						double[] normArray = new double[10];
						for (int i = 0; i < 10; i++) {
							if (i > 0)
								normArray[i] = ((double) spArray[i].successes / (double) formula.clauseNum)
										+ normArray[i - 1];
							else
								normArray[i] = ((double) spArray[i].successes / (double) formula.clauseNum);
						}
						Random rand = new Random();
						boolean[][] reprodlistA = new boolean[8][formula.variableNum];
						boolean[][] reprodlistB = new boolean[8][formula.variableNum];
						for (int i = 0; i < 8; i++) {
							double randPickA = rand.nextDouble() * normArray[9];
							double randPickB = rand.nextDouble() * normArray[9];
							for (int j = 0; j < 10; j++)
								if (randPickA <= normArray[j]) {
									reprodlistA[i] = spArray[j].getBoolString();
									break;
								}
							for (int k = 0; k < 10; k++)
								if (randPickB <= normArray[k]) {
									reprodlistA[i] = spArray[k].getBoolString();
									break;
								}
						}
						spArray[0] = spArray[maxIndex];
						maxIndex = 0;
						spArray[1] = spArray[penulIndex];
						penulIndex = 1;

						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < spArray[0].getBoolString().length; j++) {
								int rnum = rand.nextInt(10000);
								if (rnum < 4999)
									reprodlistA[i][j] = reprodlistB[i][j];
							}
							boolean[] newBool = newBoolString(reprodlistA[i]);
							spArray[i + 2].setBoolString(newBool);
						}

						for (int i = 2; i < 10; i++) {
							int rnum = rand.nextInt(10000);
							if (rnum < 8999) {
								for (int j = 0; j < spArray[i].getBoolString().length; j++) {
									rnum = rand.nextInt(10000);
									if (rnum < 4999) {
										spArray[i].flip(j);
										numFlips++;
									}
								}
							}
						}
						hFlip(spArray, formula);
					}
				}
			}
			switch(cont){
			case 'a':
				if(x < 100){
					filename = "uf20-0" + x + " .cnf";
				}
				else
					filename = "uf20-100.cnf";
				break;
			case 'b':
				if(x < 100){
					filename = "uf50-0" + x + " .cnf";
				}
				else
					filename = "uf50-100.cnf";
				break;
			case 'c':
				if(x < 100){
					filename = "uf75-0" + x + " .cnf";
				}
				else
					filename = "uf75-100.cnf";
				break;
			case 'd':
				if(x < 100){
					filename = "uf100-0" + x + " .cnf";
				}
				else
					filename = "uf100-100.cnf";
				break;
			default:
				break;
			}
		}
		System.out.println("Average flips over 100 trials: " + numFlips / 100);
		System.out.println("Average time taken over 100 trials: " + timeTotal / 100 + "ms");
	}

	public static void hFlip(Specimen[] sp, Formula formula) {
		int overallGain = 1;
		Random rand = new Random();
		for (int i = 2; i < 10; i++) {
			while (overallGain > 0) {
				int trackcount = 0;
				overallGain = 0;
				while (trackcount < formula.variableNum) {
					int index = rand.nextInt(sp[i].getBoolString().length);
					if (!sp[i].getReached()[index]) {
						sp[i].getReached()[index] = true;
						int flipgain = 0;
						boolean[] oldBool = newBoolString(sp[i].getBoolString());
						sp[i].flip(index);
						numFlips++;
						flipgain = formula.evaluate(sp[i].getBoolString()) - formula.evaluate(oldBool);
						if (flipgain >= 0) {
							overallGain += flipgain;
						} else {
							sp[i].setBoolString(oldBool);
						}
						trackcount++;
					}
				}
				for (int j = 0; j < sp[i].getBoolString().length; j++)
					sp[i].getReached()[j] = false;
			}
			overallGain = 1;
		}
	}

	public static boolean[] newBoolString(boolean[] boolA) {
		boolean[] newBool = new boolean[boolA.length];
		for (int i = 0; i < boolA.length; i++) {
			newBool[i] = boolA[i];
		}
		return newBool;
	}

	public static Formula parseFile(File f) {
		Formula newFormula = new Formula(0, 0);
		int formulaVariables, formulaSize, formulaIndex = 0;
		try {
			StringTokenizer st;
			String token;
			FileReader fileReader = new FileReader(f);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				st = new StringTokenizer(line);
				while (st.hasMoreTokens()) {
					token = st.nextToken();
					if (token.equals("c"))
						break;
					if (token.equals("p")) {
						st.nextToken();
						formulaVariables = Integer.parseInt(st.nextToken());
						formulaSize = Integer.parseInt(st.nextToken());
						newFormula = new Formula(formulaVariables, formulaSize);
					} else if (newFormula.clauseNum > 0 && newFormula.variableNum > 0) {
						try {
							int newNum = Integer.parseInt(token);
							if (newNum == 0) {
								continue;
							}
							int a = newNum;
							int b = Integer.parseInt(st.nextToken());
							int c = Integer.parseInt(st.nextToken());
							newFormula.setClause(a, b, c, formulaIndex);
							formulaIndex++;
						} catch (NumberFormatException ex) {

						}
					}
				}
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		if (newFormula.clauses != null) {
			return newFormula;
		} else
			return null;
	}
}