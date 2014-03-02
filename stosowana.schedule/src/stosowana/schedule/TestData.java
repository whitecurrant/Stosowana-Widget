package stosowana.schedule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.util.Log;

public class TestData {
	
	private Map<Integer, ArrayList<Subject> > schedule; 
	
	public TestData(){
		
		Random rand = new Random();
		String [] testNames = {"Srodowiska i narzędzia wytwórcze","Programowanie obiektowe","Systemy dynamiczne","Podstawy automatyki",
				"Podstawy LaTeX","Jezyki i technologie webowe","Badania operacyjne i teoria złożoności obliczeniowej","Programowanie mikrokontrolerów i mikroprocesorów",
				"Systemy operacyjne","Równania różniczkowe","Bezsensowny przedmiot"};
		String [] testLoc = { "435 B1","102 C2", "224 C2"};
		String [] testHours = {"8:00","9:30","11:00","12:30","14:00","15:30","17:00","19:30"};
		schedule = new HashMap<Integer, ArrayList<Subject>>();
		
		for(int i =0;i<5;i++){
			ArrayList<Subject> testList = new ArrayList<Subject>();
			
			for(int j = 0;j<2;j++){
				
				Log.d("widget", "j = " + j);
				int h = rand.nextInt(testHours.length - 1);
				testList.add(new Subject( testNames[rand.nextInt(testNames.length)], "Mróóówka", testLoc[rand.nextInt(testLoc.length)],
						testHours[h],testHours[h+1],Type.LAB));
				testList.add(new Subject( testNames[rand.nextInt(testNames.length)], "Mróóówka", testLoc[rand.nextInt(testLoc.length)],
						testHours[h],testHours[h+1],Type.WYK));
			} 
	
			schedule.put(i,testList);
			Log.d("widget","nextDay");
		}				
	}
	public Map<Integer, ArrayList<Subject> >  getTestSchedule (){
		return schedule;
	}

}
