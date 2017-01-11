import java.util.Scanner;
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
import java.io.PrintWriter;
import java.io.File;


public class ERUI extends Application {

	String password = "ATV123";

    public static void main(String[] args) {
		launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
   		Scanner scan = new Scanner(System.in);
		int noItems = scan.nextInt();

		EquipRoom allInfo = new EquipRoom(noItems);

		allInfo.readIn(scan);

		GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        

        TextField itemField = new TextField();
        grid.add(itemField, 0, 1);

        TextField nameField = new TextField();
        grid.add(nameField, 0, 0);

        Text statusHead = new Text("Status:");
        grid.add(statusHead, 0, 9);
        Text status = new Text("Welcome to the ATV Equipment Room!");
        grid.add(status, 0, 10);
        
        Button outButton = new Button("Check item in");
        grid.add(outButton, 2, 5);
        outButton.setOnAction(actionEvent -> {
        	status.setText(allInfo.checkInItem(itemField.getText()));
        	itemField.setText("");
        });

        Button inButton = new Button("Check item out");
        grid.add(inButton, 3, 5);
        inButton.setOnAction(actionEvent -> {
        	status.setText(allInfo.checkOutItem(itemField.getText(), nameField.getText()));
        	itemField.setText("");
        });

        Button printButton = new Button("Print to terminal");
        grid.add(printButton, 4, 5);
        printButton.setOnAction(actionEvent -> {
        	System.out.println(allInfo.readOut());
        	System.out.println("No. items = " + allInfo.noItems);
        });

        Button backupButton = new Button("Save record");
        grid.add(backupButton, 2, 6);
        backupButton.setOnAction(actionEvent -> {
        	File file = new File ("EquipRoom.txt");
		    try
		    {
		        PrintWriter printWriter = new PrintWriter(file);
		        printWriter.print (allInfo.readOut());
		        printWriter.close();
		    }
		    catch (Exception ex){
		    }
        });


        //Admin-only buttons: add item, admin logout, review late fees
       	Button latesButton = new Button("Late returns");
       	Button addItemButton = new Button("Add item");
        Button adminLogOut = new Button("Admin log out");

        Button adminLogIn = new Button ("Admin");
        grid.add(adminLogIn, 3, 6);
        adminLogIn.setOnAction(actionEvent -> {
        	if (itemField.getText().equals(password)){
        		status.setText("Admin code accepted");
        		grid.add(addItemButton, 2, 7);
        		grid.add(adminLogOut, 3, 7);
        		grid.add(latesButton, 4, 7);
        	}
        	else {
        		status.setText("Admin code invalid");
        	}
        	itemField.setText("");
        });

       	adminLogOut.setOnAction(actionEvent -> {
       		status.setText("Admin logged out");
       		grid.getChildren().remove(addItemButton);
       		grid.getChildren().remove(adminLogOut);
        	grid.getChildren().remove(latesButton);
       	});

       	addItemButton.setOnAction(actionEvent -> {
       		String item = itemField.getText();
       		allInfo.addItem(item);
       		itemField.setText("");
       	});

        stage.setScene(new Scene(grid, 600, 400));
        stage.show();

    }
}






