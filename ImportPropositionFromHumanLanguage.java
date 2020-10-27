import java.util.ArrayList;

public class ImportPropositionFromHumanLanguage
{
	public static void main(String[] args)
	{
		ArrayList<String> propositionModel = new ArrayList<String>();
		String currentElement;
		boolean prefix = true;
		
		for (int i = 0; i < args.length; i++)
		{
			currentElement = args[i];
			if (currentElement.equals("not"))
			{
				prefix = false;
			}
			else if (prefix == false)
			{
				prefix = true;
			}
			else if (!currentElement.equals("and") && !currentElement.equals("or"))
			{
				propositionModel.add(null);
			}
			propositionModel.add(currentElement);
		}

		int logicalOperationTimes = (propositionModel.size() - 2) / 3;
		int booleanNumbers = logicalOperationTimes + 1;
		int possibleCombinations = (int) Math.pow(2, booleanNumbers);
		
		Boolean[][] booleanTable = new Boolean[possibleCombinations][booleanNumbers + 1];
		String cover = Integer.toBinaryString(1 << booleanNumbers).substring(1);
		
		for (int i = 0; i < possibleCombinations; i++)
		{
			String rawValues = Integer.toBinaryString(i);
			String booleanValues = rawValues.length() < booleanNumbers ? cover.substring(rawValues.length()) + rawValues : rawValues;
			for (int j = 0; j < booleanNumbers; j++)
			{
				if (booleanValues.charAt(j) == '1')
				{
					booleanTable[possibleCombinations - 1 - i][j] = true;
				}
				else
				{
					booleanTable[possibleCombinations - 1 - i][j] = false;
				}
			}
		}
		
		boolean prevBoolean;
		boolean nextBoolean;
		
		for (int i = 0; i < possibleCombinations; i++)
		{
			prevBoolean = propositionModel.get(0) == null ? booleanTable[i][0] : !booleanTable[i][0];
			for (int j = 0; j < booleanNumbers - 1; j++)
			{
				nextBoolean = propositionModel.get(3 * j + 3) == null ? booleanTable[i][j + 1] : !booleanTable[i][j + 1];
				prevBoolean = propositionModel.get(3 * j + 2).equals("and") ? prevBoolean&&nextBoolean : prevBoolean||nextBoolean;
				System.out.print(String.valueOf(booleanTable[i][j])+"	");
			}
			booleanTable[i][booleanNumbers] = prevBoolean;
			System.out.print(String.valueOf(booleanTable[i][booleanNumbers - 1]+"	|	"));
			System.out.println(String.valueOf(booleanTable[i][booleanNumbers]));
		}
		
	}
}