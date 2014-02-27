package stosowana.schedule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TestData {
	
	private Map<Integer, List<Subject> > schedule; 
	
	public TestData(){
		
		Random rand = new Random();
		String [] testNames = {"Srodowiska i narzędzia wytwórcze","Programowanie obiektowe","Systemy dynamiczne","Podstawy automatyki",
				"Podstawy LaTeX","Jezyki i technologie webowe","Badania operacyjne i teoria złożoności obliczeniowej","Programowanie mikrokontrolerów i mikroprocesorów",
				"Systemy operacyjne","Równania różniczkowe","Bezsensowny przedmiot"};
		String [] testLoc = { "435 B1","102 C2", "224 C2"};
		String [] testHours = {"8:00","9:30","11:00","12:30","14:00","15:30","17:00","19:30"};
		schedule = new HashMap<Integer, List<Subject>>();
		for(int i =0;i<5;i++){
			List<Subject> testList = new ArrayList<Subject>();
			
			for(int j = 0;j<4;j++){
				int h = rand.nextInt(testHours.length - 1);
				testList.add(new Subject( testNames[rand.nextInt(testNames.length)], "Mróóówka", testLoc[rand.nextInt(testLoc.length)],
						testHours[h],testHours[h+1]));
			} 
			schedule.put(i,testList);
		}				
	}
	public Map<Integer, List<Subject> >  getTestSchedule (){
		return schedule;
	}

}
