package irc.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import irc.App;
import irc.classes.IRCUtils;
import irc.controller.ControllerManager;
import irc.enums.FieldType;
import irc.interfaces.IServerController;
import irc.interfaces.ISimpleChat;
import irc.interfaces.ISimpleContent;
import irc.interfaces.ISimpleMessage;
import irc.interfaces.ISimpleUser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class GuiController {
	
	@FXML
	private TextField _textField;
	
	@FXML
	private ListView<String> _listViewUserlist;
	
	@FXML
	private TabPane _tabPane;
	
	@FXML
	private ListView<String> _messageLW;
	
	private App _view;

	private IServerController _serv;
	
	public void setView(App root){
		this._view = root;
	}
	
	public GuiController(){
		
	}
	

	/*
		#Sofern ein neuer User sich am System anmeldet
		_listViewUserList.setItems(users);	

		#Sofern ein User sich vom System abmeldet
		_listViewUserList.remove(index);
		
		#Sofern ein Tab geschlossen wird
		_tabPane.getOnClosed(), setOnClosed(new EventHandler<Event>(){
			@Override void handle(Event e){
				// Verbindung beenden
			}
		});

	*/

	@FXML
	public void startNewChat(){
		if(_serv==null){
			Pair<String,String> meData = createDialog("Please lend me ur strength, i need ur IP and the wantet Port","IP","Port");
			if(meData == null){
				return;
			}
			Integer port = null;
			try{
			port = Integer.parseInt(meData.getValue());
			} catch (NumberFormatException e) {
				return;
			}
			_serv = ControllerManager.createServerController(meData.getKey(), port,this);
			_serv.startServer();
			
			Pair<String,String> youData = createDialog("Want to Connect to somebody?","IP","Port");
			
			if(youData == null){
				return;
			}
			
			Integer youPort;
			try{
				youPort = Integer.parseInt(youData.getValue());
			} catch (NumberFormatException e) {
					return;
			}
			
			//IP OF others
			_serv.sendLogin(youData.getKey(), youPort);
			
		}
		
		/*
		1) Ausgewählte User aus der UserList nehmen (selected)
			_listViewUserList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			
			ObservableList selectedUsers = _listViewUserList.getSelectionModel().getSelectedIndices();

            for(Object users : selectedUsers){
                
            }
			
		2) Neuen tab mit Chat starten (Konnte ich nicht Testen, kann eventuell zu einem optischem Verschub kommen.)
			Tab tab = new Tab();
			tab.setText("Nickname");
			tab.setId("_tabId_i")
			
			Pane tabPane = new Pane();
			tabPane.setLayoutX(8.0);
			tabPane.setLayoutY(7.0);
			tabPane.setPrefHeight(603.0);
			tabPane.setPrefWidth(934.0);

			ListView listView = new ListView();
			listView.setId("_listCiewChat_id_i");
			listView.setLayoutX(1.0);
			listView.setLayoutY(1.0);
			listView.setPrefHeight(601.0);
			listView.setPrefWidth(932.0);
				
			tabPane.setContent(listView);
			tab.setContent(tabPane);
			tabPane.getTabs().add(tab);
		
		3) Verknüpfe irgendwie Tab/Chat mit der dazugehörigen Userlist
		*/
	}	
		
	@FXML
	public void sendMessage(){
		
		if(_serv==null){
			return;
		}
		List<ISimpleChat> chatList = _serv.getChatlist();
		int index = _tabPane.getSelectionModel().getSelectedIndex();
		_serv.sendText(chatList.get(index), _textField.getText());
		_textField.setText("");
		
	}
	
	@FXML
	public void logout(){
		if(_serv == null){
			return;
		}
		_serv.sendLogout();
		_serv.stopAllWorker();
		_serv.stopServer();
		this._serv = null;
	}
	
	private Pair<String,String> createDialog(String hText,String f1String,String f2String){
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("I need something...");
		dialog.setHeaderText(hText);

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Validate", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField field1 = new TextField();
		field1.setPromptText(f1String);
		TextField field2 = new TextField();
		field2.setPromptText(f2String);

		grid.add(new Label(f1String+":"), 0, 0);
		grid.add(field1, 1, 0);
		grid.add(new Label(f2String+":"), 0, 1);
		grid.add(field2, 1, 1);

		// Enable/Disable login button depending on whether a username was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		field1.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> field1.requestFocus());

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == loginButtonType) {
		        return new Pair<>(field1.getText(), field2.getText());
		    }
		    return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		if(result.isPresent()){
			return result.get();
		}
		return null;
		
	}

	public void syncUsers() {
		ObservableList<String> newOnes = FXCollections.observableList(new ArrayList<>());
		List<ISimpleUser> allUser = _serv.getAllUser();
		synchronized (allUser) {
			for(ISimpleUser elem : allUser){
				newOnes.add(elem.getIP()+":"+elem.getPort());
			}

		}
		Platform.runLater(() -> {
			_listViewUserlist.setItems(newOnes);
		});
		
	}

	public void syncChat(ISimpleChat chat) {
		synchronized(chat){
			int index = chat.getReadIndex();
			for(int i = index; i<chat.countMessages();i++){
				ISimpleMessage mes = chat.getMessage(i);
				ISimpleUser sender = mes.getSender();
				ISimpleContent con = IRCUtils.contentsOfContents(mes.getWholeMessage(), FieldType.TEXT);
				if(con!=null)
				_messageLW.getItems().add(sender.getIP()+"/"+sender.getPort()+" : "+con.contentAsString());
			}
			chat.setReadIndex(chat.countMessages());
		}
	}
			
}
