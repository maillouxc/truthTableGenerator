package discreteMath;

import java.util.ArrayList;
import java.util.List;

/**
 * An object representing a truth table. Consists of a list of strings containing the column headers,
 * and a list of TruthTableRow objects, representing the rows of the table.
 */
public class TruthTable
{
	private List<TruthTableRow> rows;
	private List<String> header;
	
	/**
	 * Standard constructor that just instantiates the fields of the class.
	 */
	public TruthTable()
	{
		this.rows = new ArrayList<TruthTableRow>();
		this.header = new ArrayList<String>();
	}
	
	/**
	 * An inner class of TruthTable representing a row within the table. Consists of a list of truth values
	 * 'T' and 'F'.
	 */
	class TruthTableRow
	{	
		List<Character> results = new ArrayList<Character>();
		
		/**
		 * Creates an instance of TruthTableRow. Must be provided as a parameter a list of Character objects
		 * representing the truth vales of the row, in the order they are to put inserted into the table in.
		 * 
		 * Once the row is created, it adds itself to a collection of rows in the outer class.
		 */
		TruthTableRow(List<Character> results)
		{
			this.results = results;
			rows.add(this);
		}
		
	}
	
	/**
	 * Returns a string representing the table, in a format that can be printed to the console.
	 * 
	 * Throws an error if the header strings are not initialized.
	 */
	public String toString()
	{
		if (header == null)
		{
			return "Error in TruthTable.toString()! Header is null!";
		}
		String result = "";
		for (String str : header)
		{
			result += str + " ";
		}
		result = result.substring(0, result.length() - 2);
		result += '\n';
		// For each row in the truth table...
		for (int row = 0; row < rows.size(); row++)
		{
			result += '\n';
			// For each element in the row...
			for (int value = 0; value < rows.get(row).results.size(); value++)
			{
				String spaces = "";
				while (spaces.length() < header.get(value).length())
				{
					spaces += " ";
				}
				result += rows.get(row).results.get(value) + spaces;
			}
		}
		return result;
	}
	
	/**
	 * Needed to initialize the header strings, since they are not known when the class is instatiated.
	 * MUST be called before attempting to call toString() and print the table to the console.
	 * @param header
	 */
	public void setHeader(List<String> header)
	{
		this.header = header;
	}
}
