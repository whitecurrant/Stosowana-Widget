package stosowana.schedule;

	
public class Subject {

	private String name, teacher, classroom;
	private Hour startTime,stopTime;
	private Weekday day;
	boolean everyWeek;
	
	public Subject(String name, String teacher,String classroom, String startTme, String stopTime, Weekday day, boolean everyWeek){
		
		this.name = name;
		this.teacher = teacher;
		this.classroom = classroom;
		this.startTime = new Hour(startTme);
		this.stopTime = new Hour(stopTime);
		this.day = day;
		this.everyWeek = everyWeek;
		
	}
	
	public Weekday getDay() {
		return day;
	}

	public boolean isEveryWeek() {
		return everyWeek;
	}

	public String getName() {
		return name;
	}

	public String getTeacher() {
		return teacher;
	}

	public String getClassroom() {
		return classroom;
	}

	public Hour getStartTime() {
		return startTime;
	}
	
	public Hour getStopTime() {
		return startTime;
	}
	
	public String toString(){
		return startTime + " - "+ stopTime+ " " +name + " " + classroom;
	}
		
}
