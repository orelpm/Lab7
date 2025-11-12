package aydin.firebasedemo;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.storage.Acl;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.cloud.firestore.DocumentSnapshot;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.concurrent.ExecutionException;

public class WelcomeController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void onRegisterClicked() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Missing Input", "Please enter both email and password");
            return;
        }
        try{
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);

            UserRecord userRecord = DemoApp.fauth.createUser(request);

            showAlert("Success", "User Registered: " + userRecord.getEmail());
        } catch (FirebaseAuthException e) {
            showAlert("Registration Error", e.getMessage());
        }
    }

    @FXML
    private void onSignInClicked() throws IOException  {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Missing Input", "Please enter both email and password");
            return;
        }

        try {
            showAlert("Success", "Login Successful!");
            DemoApp.setRoot("primary");
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not load main screen: " + e.getMessage());
        }
        catch (Exception e ){
            showAlert("Error", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
