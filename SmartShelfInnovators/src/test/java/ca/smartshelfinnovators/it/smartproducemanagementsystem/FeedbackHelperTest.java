package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;

@RunWith(RobolectricTestRunner.class)  // Use Robolectric Test Runner
public class FeedbackHelperTest {

    @Mock
    private FirebaseFirestore mockFirestore;  // Mock FirebaseFirestore

    @Mock
    private CollectionReference mockCollection;  // Mock CollectionReference

    @Mock
    private DocumentReference mockDocRef;  // Mock DocumentReference

    @Mock
    private Context mockContext;

    @Mock
    private SharedPreferences mockSharedPreferences;

    @Mock
    private SharedPreferences.Editor mockEditor;

    @Mock
    private ProgressBar mockProgressBar;

    @Captor
    private ArgumentCaptor<Map<String, Object>> feedbackCaptor;

    private FeedbackHelper feedbackHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);  // Initialize mocks
        when(mockContext.getSharedPreferences(anyString(), eq(Context.MODE_PRIVATE))).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        feedbackHelper = new FeedbackHelper(mockContext, mockProgressBar,"user_email");
        when(mockFirestore.collection("Feedback")).thenReturn(mockCollection);
        when(mockCollection.document(anyString())).thenReturn(mockDocRef);
    }

    @Test
    public void testCanSubmitFeedback() {
        // Set up a case where feedback was submitted 25 hours ago (greater than SUBMISSION_INTERVAL)
        long lastSubmissionTime = System.currentTimeMillis() - (25 * 60 * 60 * 1000); // 25 hours ago
        when(mockSharedPreferences.getLong(eq(FeedbackHelper.LAST_SUBMISSION_TIME), anyLong())).thenReturn(lastSubmissionTime);

        assertTrue(feedbackHelper.canSubmitFeedback());
    }

    @Test
    public void testCannotSubmitFeedback() {
        // Set up a case where feedback was submitted 1 hour ago (less than SUBMISSION_INTERVAL)
        long lastSubmissionTime = System.currentTimeMillis() - (1 * 60 * 60 * 1000); // 1 hour ago
        when(mockSharedPreferences.getLong(eq(FeedbackHelper.LAST_SUBMISSION_TIME), anyLong())).thenReturn(lastSubmissionTime);

        assertFalse(feedbackHelper.canSubmitFeedback());
    }

    @Test
    public void testGetRemainingTime() {
        // Set up a case where feedback was submitted 1 hour ago
        long lastSubmissionTime = System.currentTimeMillis() - (1 * 60 * 60 * 1000); // 1 hour ago
        when(mockSharedPreferences.getLong(eq(FeedbackHelper.LAST_SUBMISSION_TIME), anyLong())).thenReturn(lastSubmissionTime);

        long remainingTime = feedbackHelper.getRemainingTime();
        assertTrue(remainingTime > 0);  // The remaining time should be positive and greater than 0
    }

    @Test
    public void testGetDeviceModel() {
        String expectedDeviceModel = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        String deviceModel = feedbackHelper.getDeviceModel();
        assertEquals(expectedDeviceModel, deviceModel); // Should match the expected device model
    }

    @Test
    public void testSaveFeedbackToFirestore_Success() {
        // Mock Firebase Firestore
        when(mockFirestore.collection("Feedback")).thenReturn(mockCollection);
        // Mock success listener
        doAnswer(invocation -> {
            feedbackCaptor.capture();
            return null;
        }).when(mockCollection).add(feedbackCaptor.capture());

        // Prepare data to save
        String name = "John Doe";
        String phone = "1234567890";
        String email = "johndoe@example.com";
        String comment = "Great product!";
        float rating = 4.5f;
        String deviceModel = "Samsung Galaxy S20";

        // Call the method to save feedback
        feedbackHelper.saveFeedbackToFirestore(name, phone, email, comment, rating, deviceModel);

        // Capture and verify the feedback data
        Map<String, Object> savedFeedback = feedbackCaptor.getValue();
        assertNotNull(savedFeedback);
        assertEquals(name, savedFeedback.get("name"));
        assertEquals(phone, savedFeedback.get("phone"));
        assertEquals(email, savedFeedback.get("email"));
        assertEquals(comment, savedFeedback.get("comment"));
        assertEquals(rating, savedFeedback.get("rating"));
        assertEquals(deviceModel, savedFeedback.get("deviceModel"));

        // Verify progress bar visibility
        verify(mockProgressBar).setVisibility(View.GONE);
    }

    @Test
    public void testSaveFeedbackToFirestore_Failure() {
        // Simulate a failure scenario (e.g., Firestore throws an exception)
        doThrow(new RuntimeException("Firestore failure")).when(mockDocRef).set(any());

        // Call the method you are testing
        feedbackHelper.saveFeedbackToFirestore("test feedback", mockContext, mockProgressBar,"comment");

        // Verify visibility change on failure
        verify(mockProgressBar).setVisibility(View.GONE); // Ensure visibility change is invoked

        // Verify Toast is shown
        //xverify(mockContext).makeText(mockContext, "Failed to submit feedback", Toast.LENGTH_SHORT);
    }
}
