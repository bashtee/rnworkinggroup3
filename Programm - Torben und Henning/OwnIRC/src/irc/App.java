package irc;

import java.io.IOException;

import irc.view.GuiController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application{
	private Stage _primaryStage;
	private GuiController _gCtrl;
    private AnchorPane _rootLayout;
    
    public App(){
    	
    }
    
    public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this._primaryStage = primaryStage;
        this._primaryStage.setTitle("Nachrichten Ticker");

        initRootLayout();
        
        this._primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
               // close();
            }
        });  
	}
	public void initRootLayout() {
		try {
	        // Load root layout from fxml file.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(App.class.getResource("view/chatgui.fxml"));
	        _rootLayout = (AnchorPane) loader.load();
	        this._gCtrl = loader.getController();
	        _gCtrl.setView(this);
	        Scene scene = new Scene(_rootLayout);
	        _primaryStage.setScene(scene);
	        _primaryStage.setMinHeight(400);
	        _primaryStage.setMinWidth(600);
	        _primaryStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
