public class ManagerRoom {
    private String room;
    private int[] revenues = new int[12];
    private int total;

    public ManagerRoom(String r){
        room = r;
    }

    public void addMonthRevenue(int month, int rev){
        revenues[month] = rev;
    }

    public void setTotal(){
        for (int i : revenues){
            total += i;
        }
    }

    public int getTotal(){
        return total;
    }

    public int[] getRevenues(){
        return revenues;
    }

    public String getRoom(){
        return room;
    }

    private String getStringArray(){
        String s = "";
        for (int i : revenues){
            s += String.format("%10s", "$" + Integer.toString(i));
        }
        return s;
    }

    public String toString(){
        return String.format("%27s",room) + getStringArray() + "     $" + Integer.toString(total);
    }
}
