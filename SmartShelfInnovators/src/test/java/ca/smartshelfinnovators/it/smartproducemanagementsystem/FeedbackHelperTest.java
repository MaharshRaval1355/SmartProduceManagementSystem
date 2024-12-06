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
        // Mock the SharedPreferences to return a last submission time of 1 hour ago
        long lastSubmissionTime = System.currentTimeMillis() - (1 * 60 * 60 * 1000); // 1 hour ago
        when(mockSharedPreferences.getLong(eq(FeedbackHelper.LAST_SUBMISSION_TIME + "_user@example.com"), anyLong()))
                .thenReturn(lastSubmissionTime);

        // Create FeedbackHelper with mock context and email
        FeedbackHelper feedbackHelper = new FeedbackHelper(mockContext, mockProgressBar, "user@example.com");

        // Set the submission interval to 2 hours for testing purposes
        long submissionInterval = 2 * 60 * 60 * 1000; // For example, 2 hours

        // When canSubmitFeedback() is called, check if feedback can be submitted based on the time difference
        boolean result = feedbackHelper.canSubmitFeedback();

        // Log the result for debugging purposes
        System.out.println("Can submit feedback? " + result);

        // Assert that feedback cannot be submitted if the submission interval hasn't passed (1 hour < 2 hours)
        assertFalse("Feedback should not be allowed because the interval hasn't passed", result);
    }

    @Test
    public void testGetRemainingTime() {
        // Simulate a case where feedback was submitted 1 hour ago
        long lastSubmissionTime = System.currentTimeMillis() - (1 * 60 * 60 * 1000); // 1 hour ago
        long submissionInterval = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

        when(mockSharedPreferences.getLong(eq(FeedbackHelper.LAST_SUBMISSION_TIME), anyLong()))
                .thenReturn(lastSubmissionTime);

        long remainingTime = feedbackHelper.getRemainingTime();

        // Log for debugging
        System.out.println("Last Submission Time: " + lastSubmissionTime);
        System.out.println("Current Time: " + System.currentTimeMillis());
        System.out.println("Remaining Time: " + remainingTime);

        // Remaining time should be approximately 23 hours
        assertTrue("Remaining time should be greater than or equal to 0", remainingTime >= 0);
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

}
