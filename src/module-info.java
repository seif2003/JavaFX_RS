module RS {
	requires javafx.controls;
	requires java.sql;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;
	 requires java.desktop;
	
	exports controllers.auth to javafx.fxml;
	exports controllers.entreprise to javafx.fxml;
	exports controllers.postulator to javafx.fxml;
	
	opens models to javafx.base;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
	
	opens controllers.auth to javafx.fxml;
	opens controllers.entreprise to javafx.fxml, javafx.base;
	opens controllers.postulator to javafx.fxml, javafx.base;
	
}