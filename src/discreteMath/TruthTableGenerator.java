package discreteMath;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

/**
 * A simple command line based truth table generator for boolean expressions.
 * 
 * Created as an honors-contract project for Discrete Mathematics, 
 * MAD 3107 at Florida Gulf Coast University.
 *
 * This software was created for educational purposes, 
 * and may be freely modified or distrubted without restriction.
 */
public class TruthTableGenerator
{	
	private static Scanner inputScanner;
	
	public static void main(String[] args)
	{
		System.out.println("Simple Truth Table Generator - By Chris Mailloux");
		
		for(;;) // Forever
		{
			System.out.println("\nEnter an expression, 'help' for instructions, or 'exit' to quit:");
			String input = getCommandLineInput();
			switch(input)
			{
				case "help":
					printHelpText();
					break;
				case "exit":
					inputScanner.close();
					System.out.println("Program Terminated.");
					System.exit(0);
				case "" :
					System.out.println("Input cannot be empty!");
					break;
				default:
					ExpressionTree tree = new ExpressionTree(input);
					TruthTable truthTable = new TruthTable();
					int numberOfRowsNeeded = (int)Math.pow(2, tree.leafs.size()); // (2^n) not including header row
					for(int i = 0; i < numberOfRowsNeeded; i++)
					{
						String binaryForm = Integer.toBinaryString(i);
						while (binaryForm.length() < tree.leafs.size())
						{
							binaryForm = "0" + binaryForm;
						}
						Queue<Character> operands = new ArrayDeque<Character>();
						for (char operand : binaryForm.toCharArray())
						{
							operand = (operand == '1') ? ('T') : ('F');
							operands.add(operand);
						}
						tree.setLeafOperands(operands);
						//System.out.println("");
						tree.evaluate(tree.root);
						truthTable.new TruthTableRow(tree.getLastEvaluationResults());
					}
					truthTable.setHeader(tree.getEvaluationOrder());
					System.out.println(truthTable.toString());
					System.out.println("");
					break;
			}
		}
	}
	
	/**
	 * Displays the program instructions.
	 */
	private static void printHelpText()
	{
		System.out.println("");
		System.out.println("Use ! for NOT, & for AND, and | for OR");
		System.out.println("Both arguments of a binary operator MUST have complete set of parentheses.");
		System.out.println("E.g. \"a|b\" is not valid, but \"(a)|(b)\" is.");
		System.out.println("Pathentheses are required for terms after ! operator as well, "
				+ "except when term following is single variable.");
		System.out.println("Only single letter, lowercase variables are allowed, with no whitespace permitted anywhere.");
		System.out.println("At the current time, multiple uses of the same variable are NOT supported." 
				+ " All variables are treated as unique. E.g. the expression \"(a)|(a)\" would be evaluated the same as "
				+ "\"(a)|(b)\"");
		System.out.println("");
	}
	
	/**
	 * Returns a single line of user input from the command line.
	 */
	private static String getCommandLineInput()
	{
		inputScanner = new Scanner(System.in);
		String text = inputScanner.nextLine();
		return text;
	}
}