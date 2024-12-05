package com.example.data_server.service;

import com.example.data_server.repository.CustomerRepository;
import com.example.shared.converters.AddressConverter;
import com.example.sep3.grpc.*;
import com.example.shared.dao.usersDao.CustomerDao;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
public class CustomerServiceImpl extends CustomerServiceGrpc.CustomerServiceImplBase {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        System.out.println("CustomerServiceImpl created");
    }

    @Override
    public void registerCustomer(RegisterCustomerRequest request,
                                 StreamObserver<EmptyMessage> responseObserver) {
        System.out.println("Request for register customer.");

        // Prepare to save the customer details in database
        CustomerDao customer = generateCustomerDaoFromRegisterCustomerRequest(request);

        try {
            customerRepository.save(customer);
            EmptyMessage response = EmptyMessage.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (org.springframework.dao.DuplicateKeyException e) {
            responseObserver.onError(new IllegalArgumentException("Customer with email already exists: " + customer.getEmail()));
        }
    }

    @Override
    public void getCustomerByEmail(CustomerByEmailRequest request,
                                   StreamObserver<CustomerResponse> responseObserver) {
        Optional<CustomerDao> customer = customerRepository.findByEmail(request.getEmail());
        if (customer.isPresent()) {
            CustomerResponse response = buildCustomerResponse(customer.get());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        throw new IllegalArgumentException("Customer not found");
    }

    private CustomerResponse buildCustomerResponse(CustomerDao customer) {
        return CustomerResponse.newBuilder()
                .setFirstName(customer.getFirstName())
                .setLastName(customer.getLastName())
                .setEmail(customer.getEmail())
                .setPhoneNumber(customer.getPhoneNumber())
                .setHashedPassword(customer.getPassword())
                .setId(customer.getId())
                .setAddress(AddressConverter.convertAddressDaoToGrpcAddress(customer.getAddress()))
                .setRole(customer.getRole())
                .build();
    }

    private CustomerDao generateCustomerDaoFromRegisterCustomerRequest(RegisterCustomerRequest request) {
        CustomerDao customer = new CustomerDao();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPassword(request.getHashedPassword());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setRole(request.getRole());
        customer.setAddress(
                AddressConverter.convertGrpcAddressToAddressDao(request.getAddress()));
        return customer;
    }
}
