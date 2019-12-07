package flatten;

import java.util.ArrayList;

public class FlattenArrays {
	
	public static void main(String[] args) 
	{
		
		// Create the main array
		Object[] array = new Object[2];
		
		// Test array.
		array[0] = new Object[5];
		((Object[]) array[0])[0] = 1;
		((Object[]) array[0])[1] = 2;
		((Object[]) array[0])[2] = new Object[1];
		((Object[])((Object[]) array[0])[2])[0] = 3;
		((Object[]) array[0])[3] = 7;
		((Object[]) array[0])[4] = new Object[2];
		((Object[])((Object[]) array[0])[4])[0] = 33;
		((Object[])((Object[]) array[0])[4])[1] = 11;
		array[1] = 14;
		
		// Show the array of arrays composition and the expected result.
		System.out.println("Input send     : [[1,2,[3],7,[33,11]],14]");
		System.out.println("Output expected: [1, 2, 3, 7, 33, 11, 14]");
		
		// Create list of integers.
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		try 
		{
			flatten(array, result);
			
			// Show result.
			System.out.println("Result: " + result);
		} 
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}		
		
	}
	
	private static void flatten(Object[] array, ArrayList<Integer> outputList) throws Exception 
	{
		for(int i = 0; i < array.length; i++) 
		{
			if(array[i] instanceof Object[]) 
			{
				flatten((Object[]) array[i], outputList);
			}			
			else if(array[i] instanceof Integer) 
			{
				outputList.add((Integer)array[i]);
			}
			else 
			{
				throw new Exception("Invalid type of data: only Arrays or Integers");
			}
		}
	}

}
