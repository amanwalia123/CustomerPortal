package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Custid {

    @FXML
    private TextField textID;

    @FXML
    private Button buttonID;

    @FXML
    private Label errorLabel;

    //Singleton Object of central database class
    private SingletonDBModel database = SingletonDBModel.getInstance();

    @FXML
    void handleEventGetID(ActionEvent event) throws IOException {

        String a = "vdvdv";

        if (textID.getText().isEmpty() || !isValidID(textID.getText())) {
            //empty or Illegal ID value
            errorLabel.setText("Illegal ID Value");
            textID.clear();
        } else {

            Customer c = database.find_customer(Integer.parseInt(textID.getText()));

            if (c != null) {

                Parent home_page_parent = FXMLLoader.load(getClass().getResource("catSelect.fxml"));
                Scene home_page_scene = new Scene(home_page_parent);
                Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                app_stage.hide();
                app_stage.setScene(home_page_scene);
                app_stage.show();


            } else {
                errorLabel.setText("No such customer exists!");
                textID.clear();
            }
        }
    }

    private static boolean isValidID(String str) {
        int num;
        try {
            num = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return num > 0 ? true : false;
    }

}
