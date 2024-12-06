package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private TextView userName, userEmail, userPhone;
    protected String name, email, phone;
    FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Toolbar setup
        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.profile_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_button_foreground); // Replace with your back icon
        toolbar.setNavigationOnClickListener(v -> {
            // Navigate back to the SettingsFragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Initialize TextViews
        userName = view.findViewById(R.id.profile_user_name);
        userEmail = view.findViewById(R.id.profile_user_email);
        userPhone = view.findViewById(R.id.profile_user_phone);

        // Fetch user details from Firestore
        fetchUserDetails();

        return view;
    }

    private void fetchUserDetails() {
        // Get the current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid(); // Get user UID

            // Reference the Firestore database
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Reference the 'Users' collection and the current user's document
            DocumentReference userRef = db.collection("Users").document(uid);

            // Fetch the user data
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Retrieve fields
                        name = document.getString("name");
                        email = document.getString("email");
                        phone = document.getString("phone");

                        // Display data in TextView
                        userName.setText(name != null ? name : "Guest User");
                        userEmail.setText(email != null ? email : "N/A");
                        userPhone.setText(phone != null ? phone : "N/A");
                    } else {
                        userName.setText("No user data found in the database.");
                        userEmail.setText("N/A");
                        userPhone.setText("N/A");
                        Log.d("Firestore", "No such document for UID: " + uid);
                    }
                } else {
                    Log.e("Firestore", "Error fetching document: ", task.getException());
                    Toast.makeText(requireContext(), "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            userName.setText("No user logged in.");
            userEmail.setText("N/A");
            userPhone.setText("N/A");
        }
    }
}
