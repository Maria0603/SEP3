package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.shared.entities.userEntities.Business;
import com.example.sep3.grpc.IdRequestResponse;
import com.example.sep3.grpc.RegisterBusinessRequest;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BusinessServiceImplTest {

    @Mock
    private BusinessRepository businessRepository;

    @InjectMocks
    private BusinessServiceImpl businessService; // The service being tested

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Test_RegisterBusiness_CorrectCase() {
        // Mock Request
        RegisterBusinessRequest request = RegisterBusinessRequest.newBuilder()
                .setBusinessName("Test Business")
                .setEmail("test@example.com")
                .setLogoPath("test-logo-path")
                .build();

        // Mock Business
        Business business = new Business();
        business.setId("1234");
        business.setBusinessName("Test Business");
        business.setEmail("test@example.com");
        business.setLogoPath("test-logo-path");

        // Mock BusinessRepository behavior
        when(businessRepository.save(any(Business.class))).thenReturn(business);

        // Mock ResponseObserver
        StreamObserver<IdRequestResponse> responseObserver = mock(StreamObserver.class);

        // Call the method under test
        businessService.registerBusiness(request, responseObserver);

        // Verify interactions
        verify(businessRepository).save(any(Business.class));
        verify(responseObserver).onNext(argThat(response ->
                response.getId().equals("1234")
        ));
        verify(responseObserver).onCompleted();
    }

    @Test
    void Test_RegisterBusiness_MissingFields() {
        // Mock request with missing fields
        RegisterBusinessRequest request = RegisterBusinessRequest.newBuilder()
                .setBusinessName("")
                .setEmail("test@example.com")
                .build();

        StreamObserver<IdRequestResponse> responseObserver = mock(StreamObserver.class);

        assertThrows(NullPointerException.class, () -> {
            businessService.registerBusiness(request, responseObserver);
        });

        verify(responseObserver, never()).onNext(any());
        verify(responseObserver, never()).onCompleted();
    }


    @Test
    void Test_RegisterBusiness_DatabaseError() {
        // Mock Request
        RegisterBusinessRequest request = RegisterBusinessRequest.newBuilder()
                .setBusinessName("Test Business")
                .setEmail("test@example.com")
                .setLogoPath("test-logo-path")
                .build();

        // Mock BusinessRepository to throw an exception
        when(businessRepository.save(any(Business.class))).thenThrow(new RuntimeException("Database error"));

        // Mock ResponseObserver
        StreamObserver<IdRequestResponse> responseObserver = mock(StreamObserver.class);

        // Call the method and expect an exception
        assertThrows(RuntimeException.class, () -> {
            businessService.registerBusiness(request, responseObserver);
        });

        // Verify no calls to responseObserver.onNext() or onCompleted()
        verify(responseObserver, never()).onNext(any());
        verify(responseObserver, never()).onCompleted();
    }
}
