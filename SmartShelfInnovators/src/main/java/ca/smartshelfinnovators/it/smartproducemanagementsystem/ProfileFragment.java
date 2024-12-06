package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private EditText editName, editEmail, editPhone, editPassword;
    private ImageView passwordToggle;
    private TextView userProfileName;
    private Button editProfileButton;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // Initialize UI Elements
        editName = view.findViewById(R.id.edit_name);
        editEmail = view.findViewById(R.id.edit_email);
        editPhone = view.findViewById(R.id.edit_phone);
        editPassword = view.findViewById(R.id.edit_password);
        userProfileName = view.findViewById(R.id.profile_name);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        passwordToggle = view.findViewById(R.id.password_toggle);

        // Back Button Listener
        ImageView backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Toggle password visibility
        passwordToggle.setOnClickListener(v -> togglePasswordVisibility());

        // Fetch and display user data
        fetchUserDetails();

        // Save Button Listener
        editProfileButton.setOnClickListener(v -> validateAndSaveChanges());

        return view;
    }

    private void togglePasswordVisibility() {
        if (editPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            // Show password
            editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordToggle.setImageResource(R.drawable.ic_visibility); // Eye open icon
        } else {
            // Hide password
            editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordToggle.setImageResource(R.drawable.ic_visibility_off); // Eye closed icon
        }
        editPassword.setSelection(editPassword.getText().length()); // Move cursor to the end
    }

    private void fetchUserDetails() {
        if (currentUser == null) {
            Toast.makeText(getContext(), getString(R.string.no_user_logged_in), Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();
        DocumentReference userRef = db.collection(getString(R.string.users)).document(uid);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String name = document.getString(getString(R.string.name));
                    String email = document.getString(getString(R.string.email));
                    String phone = document.getString(getString(R.string.phone));
                    String password = document.getString(getString(R.string.password));

                    userProfileName.setText(name != null ? name : getString(R.string.guest_user));
                    editName.setText(name != null ? name : getString(R.string.space));
                    editEmail.setText(email != null ? email : getString(R.string.space));
                    editPhone.setText(phone != null ? phone : getString(R.string.space));
                    editPassword.setText(password != null ? password : getString(R.string.space));
                } else {
                    Toast.makeText(getContext(), getString(R.string.user_data_not_found), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), getString(R.string.no_user_data_found_in_the_database), Toast.LENGTH_SHORT).show();
                Log.e(getString(R.string.firestore), getContext().getString(R.string.error_fetching_user_details), task.getException());
            }
        });
    }

    private void validateAndSaveChanges() {
        if (currentUser == null) {
            Toast.makeText(getContext(), getString(R.string.no_user_logged_in), Toast.LENGTH_SHORT).show();
            return;
        }

        String newName = editName.getText().toString().trim();
        String newEmail = editEmail.getText().toString().trim();
        String newPhone = editPhone.getText().toString().trim();
        String newPassword = editPassword.getText().toString().trim();

        if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.isEmpty() && !isPasswordStrong(newPassword)) {
            Toast.makeText(getContext(), getString(R.string.password_must_include_uppercase_lowercase_numbers_and_special_characters), Toast.LENGTH_LONG).show();
            return;
        }

        // Save to Firestore
        saveToFirestore(newName, newEmail, newPhone, newPassword);
    }

    private void saveToFirestore(String newName, String newEmail, String newPhone, String newPassword) {
        String userId = currentUser.getUid();
        DocumentReference docRef = db.collection(getString(R.string.users)).document(userId);

        docRef.update(getString(R.string.name), newName, getString(R.string.email), newEmail, getString(R.string.phone), newPhone)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.failed_to_update_profile), Toast.LENGTH_SHORT).show();
                        Log.e(getString(R.string.firestore), getString(R.string.error_updating_profile), task.getException());
                    }
                });

        if (!newPassword.isEmpty()) {
            reauthenticateAndUpdatePassword(newPassword);
        }
    }

    private void reauthenticateAndUpdatePassword(String newPassword) {
        String email = currentUser.getEmail();
        if (email == null) {
            Toast.makeText(getContext(), getString(R.string.user_email_is_missing), Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an AlertDialog to prompt for the current password
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.re_authenticate));
        builder.setMessage(R.string.enter_your_current_password_to_proceed);

        // Input field for current password
        final EditText currentPasswordInput = new EditText(requireContext());
        currentPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(currentPasswordInput);

        // Set up Confirm and Cancel buttons
        builder.setPositiveButton(R.string.confirm, null); // We will handle validation later
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());

        // Show the dialog and validate input when the Confirm button is clicked
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button confirmButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            confirmButton.setOnClickListener(v -> {
                String currentPassword = currentPasswordInput.getText().toString().trim();

                if (currentPassword.isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.current_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate current password using isPasswordStrong()
                if (!isPasswordStrong(currentPassword)) {
                    Toast.makeText(getContext(), getString(R.string.current_password_is_not_strong_please_check_its_format), Toast.LENGTH_LONG).show();
                    return;
                }

                // Re-authenticate the user
                AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword);
                currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(getString(R.string.re_authentication), getString(R.string.successful_re_authentication));

                        // Proceed to update the password
                        updatePassword(newPassword, dialog);

                    } else {
                        Toast.makeText(getContext(), getString(R.string.re_authentication_failed_please_check_your_current_password), Toast.LENGTH_SHORT).show();
                        Log.e(getString(R.string.re_authentication), getString(R.string.error_during_re_authentication), task.getException());
                    }
                });
            });
        });

        dialog.show();
    }

    private void updatePassword(String newPassword, AlertDialog dialog) {
        if (newPassword.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.new_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate the new password using isPasswordStrong()
        if (!isPasswordStrong(newPassword)) {
            Toast.makeText(getContext(), getString(R.string.new_password_must_include_uppercase_lowercase_numbers_and_special_characters), Toast.LENGTH_LONG).show();
            return;
        }

        // Update the password in Firebase Authentication
        currentUser.updatePassword(newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), getString(R.string.password_updated_successfully), Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // Close the dialog
            } else {
                Toast.makeText(getContext(), getString(R.string.failed_to_update_password_please_try_again), Toast.LENGTH_SHORT).show();
                Log.e(getString(R.string.password_update), getString(R.string.error_updating_password), task.getException());
            }
        });
    }





    private boolean isPasswordStrong(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(passwordPattern);
    }
}
