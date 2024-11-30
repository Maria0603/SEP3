package com.example.server.services;

import com.example.sep3.grpc.AuthServiceGrpc;
import com.example.sep3.grpc.LoginBusinessRequest;
import com.example.sep3.grpc.LoginBusinessResponse;
import com.example.server.DataServerStub;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {

    private final DataServerStub dataServerStub;

    public AuthService(DataServerStub dataServerStub) {
        this.dataServerStub = dataServerStub;
    }

    @Override
    public void loginBusiness(LoginBusinessRequest request, StreamObserver<LoginBusinessResponse> responseObserver) {
        try {
            // Validate the request (basic example)
            if (request.getEmail().isEmpty() || request.getPassword().isEmpty()) {
                responseObserver.onError(new IllegalArgumentException("Email and password must not be empty."));
                return;
            }

            // Example of interacting with the stub to retrieve data (replace with your actual logic)
            String businessId = dataServerStub.findBusinessIdByEmailAndPassword(request.getEmail(), request.getPassword());
            if (businessId == null) {
                responseObserver.onError(new IllegalArgumentException("Invalid email or password."));
                return;
            }

            // Create a response
            LoginBusinessResponse response = LoginBusinessResponse.newBuilder()
                    .setBusinessId(businessId)
                    .setHashedPassword("hashedPasswordPlaceholder") // Replace with actual hashed password logic
                    .build();

            // Send the response
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            // Handle any exceptions and notify the client
            responseObserver.onError(e);
        }
    }

}
