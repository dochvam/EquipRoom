import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.HashMap;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;


public class EquipRoom{

	protected int noItems;
	protected String[] equipArray;
	protected Date[][] timeArray;
	protected String[] whoArray;

	protected HashMap<Long, String> userDatabase;

	public EquipRoom(int noItems){
		this.noItems = noItems;
		this.equipArray = new String[noItems];
		this.timeArray = new Date[noItems][2];
		this.whoArray = new String[noItems];
	}

	public EquipRoom(String[] equip, Date[][] time, String[] people){
		this.noItems = equip.length;
		this.equipArray = equip;
		this.timeArray = time;
		this.whoArray = people;
	}



	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		int noItems = scan.nextInt();

		EquipRoom allInfo = new EquipRoom(noItems);

		allInfo.readIn(scan);





		allInfo.readOut();

	}


	public void readIn(Scanner scan) {

		for (int i = 0; i < noItems; i++){
			this.equipArray[i] = scan.next();
			
			long x = scan.nextLong();
			if (x != 1) this.timeArray[i][0] = new Date(x);

			x = scan.nextLong();
			if (x != 1) this.timeArray[i][1] = new Date(x);
			
			this.whoArray[i] = scan.next();
		}
		
	}


	public void readOut(){

		System.out.println(this.noItems);
		System.out.println("");

		for (int i=0; i < noItems; i++){
			System.out.print(equipArray[i] + " ");
			if (timeArray[i][0] != null){
				System.out.print(timeArray[i][0].getTime() + " ");
				System.out.print(timeArray[i][1].getTime() + " ");
			} else {
				System.out.print(1 + " ");
				System.out.print(1 + " ");
			}
			System.out.println(whoArray[i]);
		}
	}


	public void checkOutItem(String item, String person){
		Date time = new Date();

		int i = this.getItemIndex(item);

		this.timeArray[i][0] = time;
		this.timeArray[i][1] = new Date(time.getTime() + 172800000);
		this.whoArray[i] = person;
	}


	public void checkInItem(String item){
		Date time = new Date();

		int i = this.getItemIndex(item);

		if (time.getTime() > timeArray[i][1].getTime()) System.out.println("Equipment is late.");

		this.timeArray[i][0] = null;
		this.timeArray[i][1] = null;

	}

	public void inventoryItem(String item){
		int i = this.getItemIndex(item);

		System.out.println("Item: "+item);
		//Figure out the best way to display due dates
		System.out.println("Checked out by: " + this.whoArray[i]);

	}

	public int getItemIndex(String item){
		int index = -1;
		for (int i = 0; i<noItems; i++){
			if (item == this.equipArray[i]) index = i;
		}
		if (index == -1) System.out.println("Item " + item + " not in database.");
		return index;
	}

}