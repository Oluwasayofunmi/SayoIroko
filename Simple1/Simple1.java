//standard javafx imports  
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Scene;

//Imports for layout
import javafx.scene.layout.BorderPane;

//Imports for menus
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

//Imports for components in this application
 import javafx.scene.control.TextArea;
 
 //Support for quitting the application.
 import javafx.application.Platform;


public class Simple1 extends Application {

// Declare components at class scope.
	TextArea txtMain;
	//Menus, menu bar
	MenuBar mBar;
	
	public Simple1() {
		// TODO Auto-generated constructor stub
	}// constructor()

	 
	@java.lang.Override
	public void init() {
		//Instantiate Components
		txtMain = new TextArea();
		
		//Create menus and menu items
		Menu mnuFile = new Menu("File");
		MenuItem fileNewItem = new MenuItem("New");
		mnuFile.getItems().add(fileNewItem);
		
		MenuItem fileOpenItem = new MenuItem("Open");
		mnuFile.getItems().add(fileOpenItem);
		
		MenuItem fileSaveItem = new MenuItem ("Save");
		mnuFile.getItems().add(fileSaveItem);
		
		MenuItem fileSaveAsItem = new MenuItem ("Save As...");
		mnuFile.getItems().add(fileSaveAsItem);
		
		MenuItem fileExitItem = new MenuItem ("Exit");
		mnuFile.getItems().add(fileExitItem);
		fileExitItem.setOnAction(ae -> Platform.exit());
		
		//Instantiate a menu bar.
		mBar = new MenuBar();
		mBar.getMenus().add(mnuFile);
		
	}//init()
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Set Width and Height.
		primaryStage.setWidth(400);
		primaryStage.setHeight(300);
		
		//Create a layout.
		BorderPane bp = new BorderPane();
		
		//Add components to layout.
		bp.setCenter(txtMain);
		bp.setTop(mBar);
		
		//Create a scene.
		Scene s = new Scene (bp);
		
		//Set the scene.
		primaryStage.setScene(s);
		
		//Show the stage.
		primaryStage.show();

	}//start()
    @Override
    public void stop() {
    	
    }//stop()
    
	
	public static void main(String[] args) {
		//Launch the application.
		launch();
		
		
	}//main()

}//class


