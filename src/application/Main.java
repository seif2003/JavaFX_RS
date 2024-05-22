package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import models.User;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	private static Scene scene; 
	private static User currentUser;
	
	@Override
	public void start(Stage stage) throws Exception{
		scene = new Scene(loadFXML("/ressources/auth/Login"));
		stage.setScene(scene);
		stage.show();
	}

	public static void setRoot(String fxml) {
		try {
			scene.setRoot(loadFXML(fxml));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml+".fxml"));
		return fxmlLoader.load();
	}

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
    
	public static void main(String[] args) {
		launch(args);
	}
	
    public static void logout() {
    	Main.currentUser = null;
		Main.setRoot("/ressources/auth/Login");
    }

	
}
	

