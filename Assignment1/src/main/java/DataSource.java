import javafx.collections.*;
import java.util.*;
import java.io.*;

public class DataSource
{
	public static ObservableList<TestFile> getAllTestFiles()
	{
		ObservableList<TestFile> prob= FXCollections.observableArrayList();
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
				prob.add(new TestFile(fName,type,val));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return prob;
	}
}