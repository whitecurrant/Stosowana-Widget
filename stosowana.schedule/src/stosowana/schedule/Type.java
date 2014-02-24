package stosowana.schedule;

public enum Type {
	LAB{
		
		@Override
        public String toString() {
            return "lab.";
        }
	},
	WYK {
		@Override
        public String toString() {
            return "wykł.";
        }
		
	},CUS{
		@Override
        public String toString() {
            return "";
        }
		
	}
};
