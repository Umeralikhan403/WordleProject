module Wordle {
    requires javafx.controls;
    requires javafx.fxml;

    // allow FXMLLoader to reflect into these packages:
    opens wordle.Controller to javafx.fxml;
    opens wordle.Models    to javafx.fxml;

    // if you want other code to use your models:
    exports wordle.Models;

    
    
    opens wordle to javafx.fxml;
    exports wordle;
}
