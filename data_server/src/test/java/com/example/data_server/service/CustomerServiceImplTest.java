package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.repository.CustomerRepository;
import com.example.sep3.grpc.*;
import com.example.shared.entities.userEntities.Customer;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BusinessRepository businessRepository;

    @InjectMocks
    private CustomerServiceImpl customerService; // The service being tested

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Test_RegisterCustomer_Success() {
        // Mock Request
        RegisterCustomerRequest request = RegisterCustomerRequest.newBuilder()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@example.com")
                .build();

        // Mock Customer
        Customer customer = new Customer();
        customer.setId("1234");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");

        // Mock repository behavior
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Mock ResponseObserver
        StreamObserver<IdRequestResponse> responseObserver = mock(StreamObserver.class);

        // Call the method under test
        customerService.registerCustomer(request, responseObserver);

        // Verify interactions
        verify(customerRepository).save(any(Customer.class));
        verify(responseObserver).onNext(argThat(response ->
                response.getId().equals("1234")
        ));
        verify(responseObserver).onCompleted();
    }

    @Test
    void Test_RegisterCustomer_EmailAlreadyExists() {
        // Mock Request
        RegisterCustomerRequest request = RegisterCustomerRequest.newBuilder()
                .setFirstName("DummyFirstName")
                .setLastName("DummyLastName")
                .setEmail("dummyemail173257114@example.com")
                .build();

        // Mock repository behavior to throw duplicate key exception
        when(customerRepository.save(any(Customer.class))).thenThrow(new org.springframework.dao.DuplicateKeyException("Duplicate email"));

        // Mock ResponseObserver
        StreamObserver<IdRequestResponse> responseObserver = mock(StreamObserver.class);

        // Call the method under test and expect IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.registerCustomer(request, responseObserver);
        });

        // Verify no interactions with responseObserver.onNext()
        verify(responseObserver, never()).onNext(any());
        verify(responseObserver, never()).onCompleted();
    }

    @Test
    void Test_GetCustomerByEmail_Success() {
        // Mock Request
        EmailRequestResponse request = EmailRequestResponse.newBuilder()
                .setEmail("john.doe@example.com")
                .build();

        // Mock Customer
        Customer customer = new Customer();
        customer.setId("1234");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");

        // Mock repository behavior
        when(customerRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(customer));

        // Mock ResponseObserver
        StreamObserver<CustomerResponse> responseObserver = mock(StreamObserver.class);

        // Call the method under test
        customerService.getCustomerByEmail(request, responseObserver);

        // Verify interactions
        verify(customerRepository).findByEmail("john.doe@example.com");
        verify(responseObserver).onNext(argThat(response ->
                response.getId().equals("1234") &&
                        response.getFirstName().equals("John") &&
                        response.getLastName().equals("Doe") &&
                        response.getEmail().equals("john.doe@example.com")
        ));
        verify(responseObserver).onCompleted();
    }

    @Test
    void Test_GetCustomerByEmail_NotFound() {
        // Mock Request
        EmailRequestResponse request = EmailRequestResponse.newBuilder()
                .setEmail("nonexistent@example.com")
                .build();

        // Mock repository behavior to return empty
        when(customerRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Mock ResponseObserver
        StreamObserver<CustomerResponse> responseObserver = mock(StreamObserver.class);

        // Call the method under test and expect IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.getCustomerByEmail(request, responseObserver);
        });

        // Verify no interactions with responseObserver.onNext()
        verify(responseObserver, never()).onNext(any());
        verify(responseObserver, never()).onCompleted();
    }

    @Test
    void Test_UpdateCustomerLocation_Success() {
        // Mock Request
        CustomerLocationRequest request = CustomerLocationRequest.newBuilder()
                .setCustomerId("1234")
                .setLatitude(37.7749)
                .setLongitude(-122.4194)
                .setRadius(10)
                .build();

        // Mock repository behavior
        when(customerRepository.findById("1234")).thenReturn(Optional.of(new Customer()));

        // Mock ResponseObserver
        StreamObserver<BusinessesInRadiusResponse> responseObserver = mock(StreamObserver.class);

        // Call the method under test
        customerService.updateCustomerLocation(request, responseObserver);

        // Verify that the customer's location was updated
        verify(customerRepository).updateLocationById("1234", 37.7749, -122.4194, 10);
        verify(responseObserver).onNext(any(BusinessesInRadiusResponse.class));
        verify(responseObserver).onCompleted();
    }

    @Test
    void Test_GetCustomerLocation_Success() {
        // Mock Request
        IdRequestResponse request = IdRequestResponse.newBuilder()
                .setId("1234")
                .build();

        // Mock Customer
        Customer customer = new Customer();
        customer.setId("1234");
        customer.setLatitude(37.7749);
        customer.setLongitude(-122.4194);
        customer.setSearchRadius(10);

        // Mock repository behavior
        when(customerRepository.findById("1234")).thenReturn(Optional.of(customer));

        // Mock ResponseObserver
        StreamObserver<CustomerLocationRequest> responseObserver = mock(StreamObserver.class);

        // Call the method under test
        customerService.getCustomerLocation(request, responseObserver);

        // Verify interactions
        verify(customerRepository).findById("1234");
        verify(responseObserver).onNext(argThat(response ->
                response.getLatitude() == 37.7749 &&
                        response.getLongitude() == -122.4194 &&
                        response.getRadius() == 10
        ));
        verify(responseObserver).onCompleted();
    }
}
