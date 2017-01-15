import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;


/**
 * Created by dochvam on 1/10/17.
 */
public class EquipRoom{

	protected int noItems;
	protected String[] equipArray;
	protected Date[][] timeArray;
	protected String[] whoArray;

	protected HashMap<String, String[]> userDatabase;

	public EquipRoom() {
		readIn();
	}

	public EquipRoom(String[] equip, Date[][] time, String[] people){
		this.noItems = equip.length;
		this.equipArray = equip;
		this.timeArray = time;
		this.whoArray = people;
	}

    public static void main(String[] args) {
    	EquipRoom er = new EquipRoom();
    	System.out.println(er.userDatabase.toString());
    }




	public void readIn() {

		File file = new File("EquipRoom.txt");

		int noItems = 0;
		String[] equipArray = null;
		Date[][] timeArray = null;
		String[] whoArray = null;

		try {
			Scanner scan = new Scanner(file);
			
			noItems = scan.nextInt();
			equipArray = new String[noItems];
			timeArray = new Date[noItems][2];
			whoArray = new String[noItems];


			for (int i = 0; i < noItems; i++){
				equipArray[i] = scan.next();
				
				long x = scan.nextLong();
				if (x != 1) timeArray[i][0] = new Date(x);

				x = scan.nextLong();
				if (x != 1) timeArray[i][1] = new Date(x);
				
				whoArray[i] = scan.next();
			}

		} catch (Exception ignored) {
			System.out.println("Read-in not working");
		}		

		this.noItems = noItems;
		this.equipArray = equipArray;
		this.timeArray = timeArray;
		this.whoArray = whoArray;

		readInUserDatabase();

	}


	public String readOut() {
		String outString = "";

		outString += this.noItems + "\n";

		for (int i=0; i < noItems; i++){
			outString += (equipArray[i] + " ");
			if (timeArray[i][0] != null){
				outString += (timeArray[i][0].getTime() + " ");
				outString += (timeArray[i][1].getTime() + " ");
			} else {
				outString += (1 + " ");
				outString += (1 + " ");
			}
			outString += (whoArray[i]) + "\n";
		}

		return outString;
	}

	public String readOutUserDatabase() {
		String outString = "";

		Iterator iter = userDatabase.entrySet().iterator();

		while (iter.hasNext()) {
			Map.Entry pair = (Map.Entry)iter.next();
			outString += pair.getKey();
			String[] name = (String[]) pair.getValue();
			outString += "\t " + name[0] + " " + name[1];
		}

		return outString;
	}


	public String checkOutItem(String item, String person){
		Date time = new Date();

		int i = this.getItemIndex(item);
		if (i < 0) return "Item "+item+" not in database";

		if (timeArray[i][0] != null){
			return "Item " + item + " already checked out";
		}


		this.timeArray[i][0] = time;
		this.timeArray[i][1] = new Date(time.getTime() + 172800000);
		this.whoArray[i] = person;
		return "Successfully checked out " + item + " to " + person;
	}

	public String addItem(String item){

		noItems++;

		String[] newEquipArray = new String[noItems];
		Date[][] newTimeArray = new Date[noItems][2];
		String[] newWhoArray = new String[noItems];

		for (int i = 0; i<noItems-1; i++){
			newEquipArray[i] = equipArray[i];
			newTimeArray[i][0] = timeArray[i][0];
			newTimeArray[i][1] = timeArray[i][1];
			newWhoArray[i] = whoArray[i];
		}

		newEquipArray[noItems-1] = item;
		newTimeArray[noItems-1][0] = null;
		newTimeArray[noItems-1][1] = null;
		newWhoArray[noItems-1] = "N/A";

		this.equipArray = newEquipArray;
		this.timeArray = newTimeArray;
		this.whoArray = newWhoArray;

		return "Added item " + item + " to database";

	}

	public void readInUserDatabase() {
		File file = new File("users.txt");
		HashMap<String, String[]> userDatabase = new HashMap<>();

		try {
			Scanner scanner = new Scanner(file);

			while (scanner.hasNext()){
				String id = (String) scanner.next();
				String firstname = scanner.next();
				String lastname = scanner.next();
				String[] name = new String[2];
				name[0] = firstname; name[1] = lastname;
				userDatabase.put(id, name);
			}

		} catch (Exception ignored) {
			System.out.println("No one taught me exceptions");
		}
		this.userDatabase = userDatabase;
	}

	public String checkInItem(String item) {
		Date time = new Date();

		int i = this.getItemIndex(item);
		if (i < 0) return "Item "+item+" not in database";

		if (timeArray[i][0] == null) {
			return "Item " + item + " not checked out";
		}

		if (time.getTime() > timeArray[i][1].getTime()) System.out.println("Equipment is late.");

		this.timeArray[i][0] = null;
		this.timeArray[i][1] = null;
		this.whoArray[i] = "N/A";

		return "Successfully checked in " + item;
	}

	public void inventoryItem(String item) {
		int i = this.getItemIndex(item);
		System.out.println("Item: "+item);
		//Figure out the best way to display due dates
		System.out.println("Checked out by: " + this.whoArray[i]);
	}

	public int getItemIndex(String item) {
		int index = -1;
		for (int i = 0; i<noItems; i++){
			if (item.equals(this.equipArray[i])) index = i;
		}
		return index;
	}

	public String getUser(String id) {
		try {
			String[] name = (String[]) userDatabase.get(id);
			return name[0] + " " + name[1];
		} catch (Exception ignored) {
			return "USER NOT RECOGNIZED";
		}
	}
}


