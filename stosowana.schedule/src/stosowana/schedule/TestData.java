package stosowana.schedule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestData {
	
	private Map<Integer, List<Subject> > schedule; 
	
	public TestData(){
		
		schedule = new HashMap<Integer, List<Subject>>();
		for(int i =0;i<5;i++){
			List<Subject> testList = new ArrayList<Subject>();
			for(int j = 0;j<4;j++){
				testList.add(new Subject("przemiot humanistyczny nr"+ i, "Mr", "123 B1"+j, "10:00", "11:30"));
			} 
			schedule.put(i,testList);
		}				
	}
	public Map<Integer, List<Subject> >  getTestSchedule (){
		return schedule;
	}

}
