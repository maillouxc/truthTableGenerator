package discreteMath;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ExpressionTree
{
	public Node root;
	public List<Node> leafs;
	
	private List<Character> evaluationResults;	// The results of the last call to evaluate() on root.
	private List<String> evaluationOrder;		// The order that the nodes were evaluated.
	
	/**
	 * Builds a binary expression tree representing the expression to be evaluated.
	 * @param initialExpression The expression to build the tree from.
	 */
	public ExpressionTree(String initialExpression)
	{
		leafs = new ArrayList<Node>();
		evaluationOrder = new ArrayList<String>();
		root = new Node(initialExpression);
	}
	
	/**
	 * A node on the binary expression tree.
	 */
	class Node
	{
		char operand;
		Node left;
		Node right;
	
		/**
		 * Creates a node on the binary expression tree, from the input expression.
		 * If the input expression is not in base form, 
		 * it recursively creates child nodes for the left and right branches.
		 * 
		 * Also keeps track of the order that nodes are created in, since this is useful information
		 * if using this tree to generate a truth table.
		 */
		Node(String expression)
		{
			String leftExpression;
			String rightExpression;
			String[] elements;
	
			// Divide the original expression into individual elements
			elements = parseExpression(expression);
			// Retrieve the individual parsed elements as the elements of an array (e.g. left, middle, right)
			leftExpression = elements[0];
			this.operand = elements[1].charAt(0);
			rightExpression = elements[2];
			// If not the base case, recurse again for left and right branches
			if (leftExpression != null)
			{
				this.left = new Node(leftExpression);
			}
			if (rightExpression != null)
			{
				this.right = new Node(rightExpression);
			}
			// Determine if this Node is terminal or has branches
			if (leftExpression == null && rightExpression == null)
			{
				// Keep track of the leafs so that class users can modify the leaf nodes or set their values.
				leafs.add(this);
			}
			evaluationOrder.add(this.toString());
		}
		
		public String toString()
		{
			String result = "";
			result += (left == null) ? ("") : ("(" + left.toString() + ")");
			result += operand;
			result += (right == null) ? ("") : ("(" + right.toString() + ")");
			return result;
		}
		
		void setOperand(char newOperand)
		{
			this.operand = newOperand;
		}
	}
	
	/**
	 * Returns an expression parsed into three parts:
	 * The left branch, the operand, and the right branch, respectively,
	 * in the form of a 3 element array of Strings.
	 * 
	 * @param expression The expression to parse.
	 * @return A 3 element array of Strings with the parsed elements.
	 */
	private String[] parseExpression(String expression)
	{
		String[] result = new String[3];
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		
		// Begin by checking the case of an isolated variable.
		if (alphabet.contains(String.valueOf(expression.charAt(0))))
		{
			if (expression.length() == 1)
			{
				result[0] = null;
				result[1] = expression;
				result[2] = null;
				return result;
			}
			else
			{
				// I should really throw an exception here, but this is just a toy program.
				System.err.println("Syntax Error!" + "\n" + "Exiting Program.");
				System.exit(-1);
			}
		}
		if (expression.startsWith("("))
		{
			// Determine if the expression has unneeded parentheses...
			int count = 1; // The number of unmatched open parentheses.
			int position;
			for (position = 1; position < expression.length(); position++)
			{
				if (expression.charAt(position) == '(')
				{
					count++;
				}
				else if (expression.charAt(position) == ')')
				{
					count--;
				}
				// If we reach the end of the original open parentheses
				if (count == 0)
				{
					// check if there is another set after...
					if (expression.substring(position+1).contains("("))
					{
						result[0] = expression.substring(0, position + 1);
						result[1] = String.valueOf(expression.charAt(position+1));
						result[2] = expression.substring(position+2);
						return result;
					}
					else
					{
						// Remove the unneeded parentheses and recurse...
						expression = expression.substring(1, expression.length() - 1);
						return parseExpression(expression);
					}
				}
			}
		}
		else if(expression.startsWith("!"))
		{
			// If in the form of !var
			if(alphabet.contains(String.valueOf(expression.charAt(1))))
			{
				result[0] = null;
				result[1] = "!";
				result[2] = expression.substring(1);
				return result;
			}
			// If in the form of !(Expression)
			else if((expression.charAt(1) == '(') && (expression.charAt(expression.length() - 1) == ')'))
			{
				result[0] = null;
				result[1] = "!";
				result[2] = expression.substring(1);
			}
			else
			{
				// I should really throw an exception here, but this is just a toy program.
				System.err.println("Syntax Error!" + "\n" + "Exiting Program.");
				System.exit(-1);
			}
		}
		return result;
	}
	
	/**
	 * Returns the boolean result of the expression starting from the passed node.
	 * 
	 * Typical use would to pass the root node of the tree in the first call; the method will then
	 * recursively iterate through the tree until reaching the base case, and then return it's results up
	 * the tree, evaluating as it goes.
	 * 
	 * This method also stores it's results in evaluationResults, because we need to know the results
	 * of each sub-expression within the tree if we want to build a truth table.
	 */
	public boolean evaluate(Node node)
	{
		// Reset the evaluation results list every time root is passed.
		if (node == this.root)
		{
			evaluationResults = new ArrayList<Character>();
		}
		// First, check if we already know the answer...
		if (node.operand == 'T') 
		{
			evaluationResults.add(Boolean.toString(true).toUpperCase().toCharArray()[0]);
			return true; 
		} 
		else if (node.operand == 'F') 
		{
			evaluationResults.add(Boolean.toString(false).toUpperCase().toCharArray()[0]);
			return false;
		}
		// Otherwise, continue recursion.
		boolean result;
		switch (node.operand)
		{	
			// Note that we have to AVOID short-circuit evaluation here by using | and & instead of || and &&
			case '&': // AND
				result = (evaluate(node.left) & evaluate(node.right));
				break;
			case '|': // OR
				result = (evaluate(node.left)) | (evaluate(node.right));
				break;
			case '!': // NOT
				result = !(evaluate(node.right));
				break;
			default:
				// Should never reach here.
				result = false; // Default value
				break;
		}
		evaluationResults.add(Boolean.toString(result).toUpperCase().toCharArray()[0]);
		return result;
	}
	
	/**
	 * When passed a Queue of Operands, this iterates throught the list of leaf-level nodes in the tree
	 * and sets the values of their operands from the ones in the queue.
	 */
	public void setLeafOperands(Queue<Character> operands)
	{
		if (operands.size() == leafs.size())
		{
			for (Node leaf : leafs)
			{
				leaf.setOperand(operands.poll());
			}
		}
		else
		{
			// I should really throw an exception here, but this is just a toy program.
			System.err.println("Internal error!" + "\n" + "Exiting Program.");
			System.exit(-1);
		}
	}
	
	public List<Character> getLastEvaluationResults()
	{
		return evaluationResults;
	}
	
	
	public List<String> getEvaluationOrder()
	{
		return evaluationOrder;
	}
}