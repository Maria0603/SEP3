package com.example.data_server.service;

import com.example.data_server.converters.NotificationEntityGrpcConverter;
import com.example.data_server.repository.NotificationRepository;
import com.example.sep3.grpc.*;
import com.example.shared.entities.domainEntities.Notification;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Test_CreateNotification_Success() {
        // Mock Request
        NotificationRequestResponse request = NotificationRequestResponse.newBuilder()
                .setUserId("user123")
                .setUserRole("CUSTOMER")
                .setContent("Test notification")
                .setTimestamp(convertDateTime_To_TimeStamp(2024,12,20,12,0))
                .setSubjectId("67532a036d0c1a41da0addf3")
                .setType("purchase")
                .build();

        // Mock ResponseObserver
        StreamObserver<EmptyMessage> responseObserver = mock(StreamObserver.class);

        // Call the method under test
        notificationService.createNotification(request, responseObserver);

        // Verify interactions
        verify(notificationRepository).save(any(Notification.class));
        verify(responseObserver).onNext(EmptyMessage.newBuilder().build());
        verify(responseObserver).onCompleted();
    }

    @Test
    void Test_GetNotifications_Success() {
        // Mock Request
        NotificationsRequest request = NotificationsRequest.newBuilder()
                .setUserId("user123")
                .setUserRole("USER")
                .build();

        // Mock Notifications
        Notification notification1 = new Notification();
        notification1.setUserId("user123");
        notification1.setUserRole("USER");
        notification1.setContent("Notification 1");
        notification1.setTimestamp(LocalDateTime.of(2024, 12, 20, 12, 0));
        notification1.setType("purchase");
        notification1.setSubjectId("67532a036d0c1a41da0addf3");

        Notification notification2 = new Notification();
        notification2.setUserId("user123");
        notification2.setUserRole("USER");
        notification2.setContent("Notification 2");
        notification2.setTimestamp(LocalDateTime.of(2024, 12, 19, 12, 0)); // Earlier timestamp
        notification2.setType("purchase");
        notification2.setSubjectId("67532a036d0c1a41da0addf3");

        List<Notification> notifications = new ArrayList<>(Arrays.asList(notification1, notification2));

        // Mock repository behavior
        when(notificationRepository.findByUserIdAndUserRole("user123", "USER")).thenReturn(notifications);

        // Mock ResponseObserver
        StreamObserver<NotificationListResponse> responseObserver = mock(StreamObserver.class);

        // Call the method under test
        notificationService.getNotifications(request, responseObserver);

        // Verify interactions
        verify(notificationRepository).findByUserIdAndUserRole("user123", "USER");
        verify(responseObserver).onNext(argThat(response -> {
            List<NotificationRequestResponse> responseNotifications = response.getNotificationsList();
            return responseNotifications.size() == 2 &&
                    responseNotifications.get(0).getContent().equals("Notification 1") && // Newer timestamp
                    responseNotifications.get(1).getContent().equals("Notification 2");  // Older timestamp
        }));
        verify(responseObserver).onCompleted();
    }


    @Test
    void Test_GetNotifications_EmptyList() {
        // Mock Request
        NotificationsRequest request = NotificationsRequest.newBuilder()
                .setUserId("user123")
                .setUserRole("USER")
                .build();

        // Mock repository behavior
        when(notificationRepository.findByUserIdAndUserRole("user123", "USER")).thenReturn(new ArrayList<>());

        // Mock ResponseObserver
        StreamObserver<NotificationListResponse> responseObserver = mock(StreamObserver.class);

        // Call the method under test
        notificationService.getNotifications(request, responseObserver);

        // Verify interactions
        verify(notificationRepository).findByUserIdAndUserRole("user123", "USER");
        verify(responseObserver).onNext(argThat(response -> response.getNotificationsCount() == 0));
        verify(responseObserver).onCompleted();
    }

    private Timestamp convertDateTime_To_TimeStamp(int year, int month, int day, int hour, int minute) {
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, 0);
        return Timestamp.newBuilder()
                .setSeconds(dateTime.toEpochSecond(ZoneOffset.UTC))
                .build();
    }
}
