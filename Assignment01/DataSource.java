import javafx.collections.*;
import java.util.*;
import java.io.*;

public class DataSource
{
	public static ObservableList<Probability> getAllMarks()
	{
		ObservableList<Probability> prob= FXCollections.observableArrayList();
		try
		{
			Scanner sc = new Scanner(new File("WordProbabilities"));
			while (sc.hasNext())
			{
				String fName = sc.next();
				Double val = Double.parseDouble(sc.next());
				String type = "";
				if (val > 0.75 )
				{
					type = "Spam";
				}
				else 
				{
					type = "ham";
				}
				prob.add(new Probability(fName,val,type));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return prob;
	}
}