package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class BookTable implements Initializable {

    @FXML
    private TableView<BookInfo> BookTable;

    @FXML
    private TableColumn<BookInfo, String> bookTitle;

    @FXML
    private TableColumn<BookInfo, Integer> bookYear;

    @FXML
    private TableColumn<BookInfo, String> bookCategory;

    @FXML
    private TableColumn<BookInfo, String> bookLanguage;

    @FXML
    private TableColumn<BookInfo, Integer> bookWeight;

    @FXML
    private Label textMinPrice;

    @FXML
    private Label minPrice;

    @FXML
    private Label signMul;

    @FXML
    private Label quanText;

    @FXML
    private TextField quantity;

    @FXML
    private Label textInvQuantity;

    @FXML
    private Label textTotal;

    @FXML
    private Label total;

    @FXML
    private Button purchButton;

    @FXML
    private Label textPurchaseSucc;

    //Singleton Object of central database class
    private SingletonDBModel database = SingletonDBModel.getInstance();

    //store selected row of the book Table
    private BookInfo selrow = null;

    //minimum price returned
    private double price = 0.0;

    //total price for all the books
    private double totalPrice = 0.0;

    //quantity of books bought
    private int qty = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        bookTitle.setCellValueFactory(new PropertyValueFactory<BookInfo, String>("Title"));
        bookYear.setCellValueFactory(new PropertyValueFactory<BookInfo, Integer>("Year"));
        bookCategory.setCellValueFactory(new PropertyValueFactory<BookInfo, String>("Category"));
        bookLanguage.setCellValueFactory(new PropertyValueFactory<BookInfo, String>("Language"));
        bookWeight.setCellValueFactory(new PropertyValueFactory<BookInfo, Integer>("Weight"));


        ObservableList<BookInfo> mybooks = FXCollections.observableArrayList(database.selBooks);
        BookTable.setItems(mybooks);

        BookTable.setRowFactory(tv -> {
            TableRow<BookInfo> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    selrow = row.getItem();
                    //show the hidden gui parts
                    textMinPrice.setVisible(true);


                    minPrice.setVisible(true);
                    price = database.min_price(selrow.getCategory(), selrow.getTitle(), selrow.getYear());
                    minPrice.setText("$" + String.valueOf(price));

                    signMul.setVisible(true);
                    quanText.setVisible(true);
                    quantity.setVisible(true);
                }
            });
            return row;
        });
    }

    @FXML
    public void getTotalPrice() {
        String s = quantity.getText();


        if (isValidQuantity(s)) {

            qty = Integer.parseInt(s);
            totalPrice = price * qty;

            textInvQuantity.setVisible(false);

            textTotal.setVisible(true);
            total.setVisible(true);

            NumberFormat formatter = new DecimalFormat("#0.00");
            total.setText("$" + formatter.format(totalPrice).toString());

            purchButton.setVisible(true);

        } else {

            textTotal.setVisible(false);
            total.setVisible(false);
            purchButton.setVisible(false);
            textInvQuantity.setVisible(false);

            if (!s.isEmpty())
                textInvQuantity.setVisible(true);

        }
    }

    @FXML
    public void makePurchase() {

        database.insert_purchase(database.custId,database.bookClub,selrow.getTitle(),selrow.getYear(),qty);
        textPurchaseSucc.setVisible(true);
        

    }

    private static boolean isValidQuantity(String str) {
        int num;
        try {
            num = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return num > 0 ? true : false;
    }

}
