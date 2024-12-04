package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUtils {

    public interface FirebaseCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public static void registerUser(FirebaseAuth auth, String email, String password, String name, String phone, FirebaseCallback callback) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String userId = auth.getCurrentUser().getUid();
                saveUserDetails(userId, email, name, phone, password, callback);
            } else {
                callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Unknown error");
            }
        });
    }

    static void saveUserDetails(String userId, String email, String name, String phone, String password, FirebaseCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("phone", phone);
        user.put("password", password);

        firestore.collection("Users").document(userId).set(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onSuccess();
            } else {
                callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Unknown error");
            }
        });
    }
}
