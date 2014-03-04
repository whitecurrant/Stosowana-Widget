package stosowana.schedule;

import java.io.Serializable;

public class Hour implements Comparable<Hour>, Serializable {

	private static final long serialVersionUID = 4549785082026860738L;
	protected int hours;
	protected int minutes;

	public Hour(int hours, int minutes) {
		this.hours = hours;
		this.minutes = minutes;
	}

	public Hour(String txt) {
		try {
			String[] s = txt.split(":");
			hours = Integer.parseInt(s[0]);
			minutes = Integer.parseInt(s[1]);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			hours = 0;
			minutes = 0;
		}
	}

	@Override
	public int compareTo(Hour h2) {

		if (this.hours * 60 + this.minutes < h2.hours * 60 + h2.minutes)
			return -1;
		else if (this.hours * 60 + this.minutes > h2.hours * 60 + h2.minutes)
			return 1;
		else
			return 0;
	}

	public String toString() {

		String tmp = (hours < 10) ? "0" + hours + ":" : hours + ":";
		tmp += (minutes < 10) ? "0" + minutes : minutes;

		return tmp;

	}

}
