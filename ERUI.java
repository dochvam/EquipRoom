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
import java.io.*;
import java.util.Date;


public class ERUI extends Application {
    String password = "ATV123";
    String currentAdmin = "";
    String currentUser = "";

    public static void main(String[] args) {
		launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {


		EquipRoom allInfo = new EquipRoom();

		GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Button login = new Button("Scan and login");
        grid.add(login, 1, 1);
        TextField nameField = new TextField();
        grid.add(nameField, 1, 0);


        TextField itemField = new TextField();

        Text nameHead = new Text("Your ID");
        Text itemHead = new Text("Item:");

        Text statusHead = new Text("Status:");
        Text status = new Text("Welcome to the ATV Equipment Room!");
        
        Button outButton = new Button("Check item in");
        outButton.setOnAction(actionEvent -> {
        	String update = allInfo.checkInItem(itemField.getText());
            status.setText(update);
            logAction(update);
        	itemField.setText("");
        });

        Button inButton = new Button("Check item out");
        inButton.setOnAction(actionEvent -> {
            String update = allInfo.checkOutItem(itemField.getText(), currentUser);
            status.setText(update);
            logAction(update);
            itemField.setText("");
        });

        Button printButton = new Button("Print to terminal");
        printButton.setOnAction(actionEvent -> {
        	System.out.println(allInfo.readOut());
        	System.out.println("No. items = " + allInfo.noItems);
        });

        Button backupButton = new Button("Save record");
        backupButton.setOnAction(actionEvent -> {
        	File file = new File ("EquipRoom.txt");
		    try
		    {
		        PrintWriter printWriter = new PrintWriter(file);
		        printWriter.print(allInfo.readOut());
		        printWriter.close();
		    }
		    catch (Exception ignored){
		    }

            File file2 = new File ("users.txt");
            try
            {
                PrintWriter printWriter = new PrintWriter(file2);
                printWriter.print(allInfo.readOutUserDatabase());
                printWriter.close();
            }
            catch (Exception ignored){
            }

        });

        Button addUser = new Button("Add user");
        addUser.setOnAction(actionEvent -> {
            String id = nameField.getText();
            String squished = itemField.getText();
            String[] name = squished.split("\\s+");
            allInfo.userDatabase.put(id, name);
        });

        //Admin-only buttons: add item, admin logout, review late fees
       	Button latesButton = new Button("Late returns");
       	Button addItemButton = new Button("Add item");
        Button adminLogOut = new Button("Admin log out");
        Button logout = new Button("Log out");


        Button adminLogIn = new Button ("Admin");
        adminLogIn.setOnAction(actionEvent -> {
        	if (itemField.getText().equals(password)) {
                currentAdmin = currentUser;
        		status.setText("Admin code accepted. Welcome, "+currentAdmin);
        		grid.add(addItemButton, 2, 7);
        		grid.add(adminLogOut, 3, 7);
        		grid.add(latesButton, 4, 7);
                grid.add(addUser, 4, 6);
                grid.getChildren().remove(logout);

            } else {
        		status.setText("Admin code invalid");
        	}
        	itemField.setText("");
        });

       	adminLogOut.setOnAction(actionEvent -> {
       		status.setText("Admin logged out");
            currentAdmin = "";
       		grid.getChildren().remove(addItemButton);
       		grid.getChildren().remove(adminLogOut);
        	grid.getChildren().remove(latesButton);
            grid.getChildren().remove(addUser);
            grid.add(logout, 4, 6);
       	});

       	addItemButton.setOnAction(actionEvent -> {
       		String item = itemField.getText();
       		allInfo.addItem(item);
       		itemField.setText("");
       	});

        login.setOnAction(actionEvent -> {
            String name = allInfo.getUser(nameField.getText());
            if (!name.equals("USER NOT RECOGNIZED")){
                grid.getChildren().remove(login);
                grid.add(itemField, 1, 1);
                grid.add(nameHead, 0, 0);
                grid.add(itemHead, 0, 1);
                grid.add(statusHead, 1, 9);
                grid.add(outButton, 2, 5);
                grid.add(inButton, 3, 5);
                grid.add(printButton, 4, 5);
                grid.add(backupButton, 2, 6);
                grid.add(adminLogIn, 3, 6);
                grid.add(logout, 4, 6);
                status.setText("Welcome "+name);
                currentUser = name;
            }
            else status.setText("User not recognized");
        });

        logout.setOnAction(actionEvent -> {
            currentUser = "";
            grid.add(login, 1, 1);
            grid.getChildren().remove(itemField);
            grid.getChildren().remove(nameHead);
            grid.getChildren().remove(statusHead);
            grid.getChildren().remove(outButton);
            grid.getChildren().remove(inButton);
            grid.getChildren().remove(printButton);
            grid.getChildren().remove(backupButton);
            grid.getChildren().remove(adminLogIn);
            grid.getChildren().remove(logout);
            grid.getChildren().remove(itemHead);
            status.setText("Goodbye");
        });

        grid.add(status, 1, 10);

        stage.setScene(new Scene(grid, 600, 400));
        stage.show();


    }

    public static void logAction(String action) {
        File file = new File("transactionhistory.txt");
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, true), "UTF-8"));
        } catch (Exception e){System.out.println("Didn't work");}

        Date time = new Date();
        String timestamp = time.toString();
        try {
            if (writer != null)
                writer.write("\n" + action +"\t " + timestamp);
            writer.close();
        } catch (Exception e) {System.out.println("Didn't work");}
    }
}

