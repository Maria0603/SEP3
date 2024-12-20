package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.repository.CustomerRepository;
import com.example.sep3.grpc.*;
import com.example.shared.entities.userEntities.Business;
import com.example.shared.entities.userEntities.Customer;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Test_GetUserByEmail_BusinessFound() {
        // Mock Request
        UserByEmailRequest request = UserByEmailRequest.newBuilder()
                .setEmail("business@example.com")
                .build();

        // Mock Business
        Business business = new Business();
        business.setId("business123");
        business.setEmail("business@example.com");
        business.setPassword("hashedPassword123");
        business.setRole("BUSINESS");

        // Mock repository behavior
        when(businessRepository.findByEmail("business@example.com")).thenReturn(Optional.of(business));

        // Mock ResponseObserver
        StreamObserver<UserResponse> responseObserver = mock(StreamObserver.class);

        // Call the method under test
        userService.getUserByEmail(request, responseObserver);

        // Verify interactions
        verify(businessRepository).findByEmail("business@example.com");
        verify(customerRepository, never()).findByEmail(anyString());
        verify(responseObserver).onNext(argThat(response ->
                response.getId().equals("business123") &&
                        response.getEmail().equals("business@example.com") &&
                        response.getHashedPassword().equals("hashedPassword123") &&
                        response.getRole().equals("BUSINESS")
        ));
        verify(responseObserver).onCompleted();
    }

    @Test
    void Test_GetUserByEmail_CustomerFound() {
        // Mock Request
        UserByEmailRequest request = UserByEmailRequest.newBuilder()
                .setEmail("customer@example.com")
                .build();

        // Mock Customer
        Customer customer = new Customer();
        customer.setId("customer123");
        customer.setEmail("customer@example.com");
        customer.setPassword("hashedPassword123");
        customer.setRole("CUSTOMER");

        // Mock repository behavior
        when(businessRepository.findByEmail("customer@example.com")).thenReturn(Optional.empty());
        when(customerRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(customer));

        // Mock ResponseObserver
        StreamObserver<UserResponse> responseObserver = mock(StreamObserver.class);

        // Call the method under test
        userService.getUserByEmail(request, responseObserver);

        // Verify interactions
        verify(businessRepository).findByEmail("customer@example.com");
        verify(customerRepository).findByEmail("customer@example.com");
        verify(responseObserver).onNext(argThat(response ->
                response.getId().equals("customer123") &&
                        response.getEmail().equals("customer@example.com") &&
                        response.getHashedPassword().equals("hashedPassword123") &&
                        response.getRole().equals("CUSTOMER")
        ));
        verify(responseObserver).onCompleted();
    }

    @Test
    void Test_GetUserByEmail_UserNotFound() {
        // Mock Request
        UserByEmailRequest request = UserByEmailRequest.newBuilder()
                .setEmail("nonexistent@example.com")
                .build();

        // Mock repository behavior to return empty for both
        when(businessRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        when(customerRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Mock ResponseObserver
        StreamObserver<UserResponse> responseObserver = mock(StreamObserver.class);

        // Call the method under test
        userService.getUserByEmail(request, responseObserver);

        // Verify interactions
        verify(businessRepository).findByEmail("nonexistent@example.com");
        verify(customerRepository).findByEmail("nonexistent@example.com");
        verify(responseObserver, never()).onNext(any());
        verify(responseObserver).onError(argThat(error ->
                error instanceof IllegalArgumentException &&
                        error.getMessage().equals("User not found with email: nonexistent@example.com")
        ));
    }
}
