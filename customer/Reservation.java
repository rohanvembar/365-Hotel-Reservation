
public class Reservation {
	int user_id;
	int card;
	int room;
	String checkin;
	String checkout;
	int num_adults;
	
	public void print_res() {
		System.out.printf("User: %d Card: %d Room: %d\n"
				+ "Check-in: %s Check-out: %s Adults: %d", user_id, card, room, checkin, checkout, num_adults);
	}
}
