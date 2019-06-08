public class Room {
	String in = "";
	String out = "";
	String decor = "";
	String type = "";
	double lower = -1;
	double upper = -1;
	int rooms = -1;
	int beds = -1;
	int occupants = -1;
	public void print_room() {
		System.out.printf("In: %s Out: %s\n"
				+ "Price: Lower %f Upper %f\n"
				+ "Decor: %s Type: %s\n"
				+ "Beds: %d Occupants: %d", in, out, lower, upper,
				decor, type, beds, occupants);

	}
}
