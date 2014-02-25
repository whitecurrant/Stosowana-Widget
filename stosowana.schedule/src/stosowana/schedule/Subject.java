package stosowana.schedule;

import java.io.Serializable;


public class Subject implements Comparable<Subject> , Serializable{


	private String name, teacher, classroom;
	private Hour startTime,stopTime;
	private Weekday day;
	private Type type;
	private Week week;
	
	public Subject(String name, String teacher,String classroom, String startTme, String stopTime){
		
		this.name = name;
		this.teacher = teacher;
		this.classroom = classroom;
		this.startTime = new Hour(startTme);
		this.stopTime = new Hour(stopTime);
		this.day = day;
		
		
	}
	
	public Subject() {
		// TODO Auto-generated constructor stub
	}

	public Weekday getDay() {
		return day;
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
		return stopTime;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(String str) {
		switch(Integer.parseInt(str)){
		case 0:
			this.type = Type.LAB;
			break;
		case 1:
			this.type = Type.WYK;
			break;
		case 2:
			this.type = Type.CUS;
			break;
		}
	}

	public Week getWeek() {
		return week;
	}

	public void setWeek(String str) {
		switch(Integer.parseInt(str)){
			case 0:
				this.week = Week.AWEEK;
				break;
			case 1:
				this.week = Week.BWEEK;
				break;
			case 2:
				this.week = Week.EWEEK;
				break;
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	public void setStartTime(String start) {
		this.startTime = new Hour(start);
	}

	public void setStopTime(String stop) {
		this.stopTime = new Hour(stop);
	}

	public void setDay(Weekday day) {
		this.day = day;
	}

	public String toString(){
		return name + " " + classroom;
	}

	@Override
	public int compareTo(Subject s2) {
		return startTime.compareTo(s2.getStartTime());
	}

		
}
