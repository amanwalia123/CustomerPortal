package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class catSelect implements Initializable {

    @FXML
    private ComboBox<String> categoryList;

    @FXML
    private TextField bookTitle;

    @FXML
    private Button getInfo;

    @FXML
    private Label welcomeCustomer;

    @FXML
    private Label errorCat;

    //Singleton Object of central database class
    private SingletonDBModel database = SingletonDBModel.getInstance();

    @FXML
    void getBookInfo(ActionEvent event) throws IOException {

        //Selection model for combo box
        SingleSelectionModel s = categoryList.getSelectionModel();
        //Checking for correct values from input
        if (s != null) {
            //A legal choice from category combo box has been made
            if (s.getSelectedItem() != null) {
                String selCategory = s.getSelectedItem().toString();    //get category

                if (!bookTitle.getText().isEmpty()) {
                    //legal choice for book title
                    String selBookTitle = bookTitle.getText();      // get book title

                    List<BookInfo> bookResults = database.find_book(selBookTitle, selCategory);    //get book results from database  in the list

                    // check if we get any book
                    if (!bookResults.isEmpty()) {

                        Parent home_page_parent = FXMLLoader.load(getClass().getResource("BookTable.fxml"));
                        Scene home_page_scene = new Scene(home_page_parent);
                        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        app_stage.hide();
                        app_stage.setScene(home_page_scene);
                        app_stage.show();


                    }
                    else {
                        // Display error message and refresh input fields
                        categoryList.getItems().removeAll(categoryList.getItems());
                        initialize(null, null);
                        errorCat.setText("No such Book in Database!! Try Again");
                    }
                } else {
                    errorCat.setText("Enter title for book!");
                }


            } else {
                errorCat.setText("Choose a category first!");
            }

        }


//
//        initialize(null,null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Displaying Customer Name in welcome message
        Customer c = database.find_customer(database.custId);
        welcomeCustomer.setText("Welcome " + c.getName().split(" ")[0]);

        //fetching categories from database
        List<String> categories = database.fetch_categories();

        //Add categories to the combo box
        for (String s : categories) {
            categoryList.getItems().add(s);
        }

        //Clearing book title field
        bookTitle.clear();


    }
}
