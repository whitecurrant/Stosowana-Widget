package stosowana.schedule;

public enum Type {
	LAB {

		@Override
		public String toString() {
			return "lab";
		}
	},
	WYK {
		@Override
		public String toString() {
			return "wyk";
		}

	},
	CUS {
		@Override
		public String toString() {
			return "ind";
		}

	}
};
