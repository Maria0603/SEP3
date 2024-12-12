package com.example.server.services.Implementations;

import com.example.sep3.grpc.BusinessIdRequest;
import com.example.sep3.grpc.BusinessResponse;
import com.example.sep3.grpc.BusinessUpdateRequest;
import com.example.server.DataServerStub;
import com.example.server.converters.BusinessDtoGrpcConverter;
import com.example.server.dto.business.BusinessResponseDto;
import com.example.server.dto.business.BusinessUpdateRequestDto;
import com.example.server.services.IBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessService implements IBusinessService {

    private final DataServerStub dataServerStub;

    @Autowired
    public BusinessService(DataServerStub dataServerStub) {
        this.dataServerStub = dataServerStub;
        System.out.println("BusinessService created");
    }

    @Override
    public BusinessResponseDto getBusinessById(String id) {
        System.out.println("getBusinessById method called with id " + id);
        BusinessIdRequest request = BusinessIdRequest.newBuilder().setId(id).build();
        BusinessResponse response = dataServerStub.getBusinessById(request);
        System.out.println("Received response from dataServerStub: " + response);

        return BusinessDtoGrpcConverter.BusinessResponseGrpc_To_BusinessResponseDto(response);
    }

    @Override
    public BusinessResponseDto updateAndValidateBusinessProfile(
        BusinessUpdateRequestDto updatedProfile) {

        if (updatedProfile.getBusinessName().contains("invalid")) {
            throw new IllegalArgumentException("Business name contains invalid content.");
        }

        System.out.println("updateBusinessProfile method called for ID: " + updatedProfile.getId());

        System.out.println("Updated Profile Details:");
        System.out.println("Business Name: " + updatedProfile.getBusinessName());
        System.out.println("Email: " + updatedProfile.getEmail());
        System.out.println("Phone Number: " + updatedProfile.getPhoneNumber());
        //System.out.println("Logo Path: " + updatedProfile.getLogoPath());


        BusinessUpdateRequest request = BusinessDtoGrpcConverter.BusinessUpdateRequestDto_To_BusinessUpdateRequest(updatedProfile);

        try {

            BusinessResponse grpcResponse = dataServerStub.updateBusinessProfile(request);

            return BusinessDtoGrpcConverter.BusinessResponseGrpc_To_BusinessResponseDto(grpcResponse);
        } catch (Exception e) {
            System.out.println("Error updating business profile: " + e.getMessage());
            return null; // Return null or handle error response appropriately
        }
    }

}
