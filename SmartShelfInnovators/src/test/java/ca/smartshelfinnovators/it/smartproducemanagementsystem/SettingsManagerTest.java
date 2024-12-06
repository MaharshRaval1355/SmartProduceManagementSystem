package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

public class SettingsManagerTest {

    @Mock
    private Context mockContext;

    @Mock
    private SharedPreferences mockSharedPreferences;

    @Mock
    private SharedPreferences.Editor mockEditor;

    @Mock
    private FirebaseAuth mockFirebaseAuth;

    @Mock
    private FirebaseUser mockFirebaseUser;

    @Mock
    private FirebaseFirestore mockFirestore;

    @Mock
    private TextView mockTextView;

    private SettingsManager settingsManager;
    CollectionReference mockCollectionReference;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        settingsManager = new SettingsManager(mockContext, mockSharedPreferences);

        // Mock Firebase components
        mockCollectionReference = mock(CollectionReference.class);
        when(mockFirestore.collection("Users")).thenReturn(mockCollectionReference);
        
    }

    @Test
    public void testIsDarkModeEnabled_DefaultFalse() {
        when(mockSharedPreferences.getBoolean(SettingsManager.DARK_MODE_KEY, false)).thenReturn(false);
        assertFalse(settingsManager.isDarkModeEnabled());
    }

    @Test
    public void testSetDarkMode() {
        settingsManager.setDarkMode(true);
        verify(mockEditor).putBoolean(SettingsManager.DARK_MODE_KEY, true);
        verify(mockEditor).apply();
    }

    @Test
    public void testIsLockScreenPortrait_DefaultFalse() {
        when(mockSharedPreferences.getBoolean(SettingsManager.LOCK_SCREEN_KEY, false)).thenReturn(false);
        assertFalse(settingsManager.isLockScreenPortrait());
    }

    @Test
    public void testSetLockScreenPortrait() {
        settingsManager.setLockScreenPortrait(true);
        verify(mockEditor).putBoolean(SettingsManager.LOCK_SCREEN_KEY, true);
        verify(mockEditor).apply();
    }

    @Test
    public void testIsNotificationEnabled_DefaultTrue() {
        when(mockSharedPreferences.getBoolean(SettingsManager.NOTIFICATIONS_KEY, true)).thenReturn(true);
        assertTrue(settingsManager.isNotificationEnabled());
    }

    @Test
    public void testSetNotificationEnabled() {
        settingsManager.setNotificationEnabled(false);
        verify(mockEditor).putBoolean(SettingsManager.NOTIFICATIONS_KEY, false);
        verify(mockEditor).apply();
    }

    @Test
    public void testFetchUserDetails_NoUserLoggedIn() {
        try (MockedStatic<FirebaseAuth> mockedFirebaseAuth = mockStatic(FirebaseAuth.class)) {
            // Mock FirebaseAuth behavior
            when(FirebaseAuth.getInstance()).thenReturn(mockFirebaseAuth);
            when(mockFirebaseAuth.getCurrentUser()).thenReturn(null);

            // Call the method under test
            settingsManager.fetchUserDetails(mockTextView);

            // Verify TextView behavior
            verify(mockTextView).setText("No user logged in.");
        }
    }
}
