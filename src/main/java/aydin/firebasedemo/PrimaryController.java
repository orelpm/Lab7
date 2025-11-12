package aydin.firebasedemo;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class PrimaryController {

    @FXML
    private TextField ageTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private javafx.scene.control.Button readButton;

    @FXML
    private javafx.scene.control.Button writeButton;

    @FXML
    private javafx.scene.control.Button addPersonButton;

    private ObservableList<Person> listOfUsers = FXCollections.observableArrayList();

    public ObservableList<Person> getListOfUsers() {
        return listOfUsers;
    }

    @FXML
    void readButtonClicked(ActionEvent event) {
        readFirebase();
    }

    @FXML
    void writeButtonClicked(ActionEvent event) {
        addData();
    }

    @FXML
    void addPersonButtonClicked(ActionEvent event) {
        addData();

        // Optionally clear the fields after adding
        nameTextField.clear();
        ageTextField.clear();
        phoneTextField.clear();
    }

    public boolean readFirebase() {
        boolean key = false;

        try {
            ApiFuture<QuerySnapshot> future = DemoApp.fstore.collection("Persons").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            listOfUsers.clear();
            outputTextArea.clear();

            for (QueryDocumentSnapshot doc : documents) {
                String name = doc.getString("Name");
                Integer age = doc.getLong("Age") != null ? doc.getLong("Age").intValue() : null;
                String phone = doc.getString("Phone");

                // Only display non-null entries
                if (name != null || age != null || phone != null) {
                    outputTextArea.appendText(
                            (name != null ? name : "N/A") +
                                    " , Age: " + (age != null ? age : "N/A") +
                                    " , Phone: " + (phone != null ? phone : "N/A") +
                                    "\n"
                    );

                    Person person = new Person(name, age != null ? age : 0, phone);
                    listOfUsers.add(person);
                }
            }
            key = true;

        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }

        return key;
    }

    public void addData() {
        try {
            DocumentReference docRef = DemoApp.fstore.collection("Persons")
                    .document(UUID.randomUUID().toString());

            Map<String, Object> data = new HashMap<>();
            data.put("Name", nameTextField.getText());
            data.put("Age", Integer.parseInt(ageTextField.getText()));
            data.put("Phone", phoneTextField.getText());

            ApiFuture<WriteResult> result = docRef.set(data);

        } catch (NumberFormatException e) {
            System.out.println("Invalid age input. Please enter a number.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
