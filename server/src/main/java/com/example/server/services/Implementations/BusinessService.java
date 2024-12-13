package com.example.server.services.Implementations;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.converters.BusinessDtoGrpcConverter;
import com.example.server.dto.business.BusinessResponseDto;
import com.example.server.dto.business.BusinessUpdateRequestDto;
import com.example.server.services.IBusinessService;
import com.example.server.services.auxServices.IImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessService implements IBusinessService {

    private final DataServerStub dataServerStub;
    private final IImageStorageService imageStorageService;

    @Autowired
    public BusinessService(DataServerStub dataServerStub, IImageStorageService imageStorageService) {
        this.dataServerStub = dataServerStub;
        this.imageStorageService = imageStorageService;
        System.out.println("BusinessService created");
    }

    @Override
    public List<BusinessResponseDto> getBusinesses() {
//        System.out.println("getBusinesses method called");

        // Create an EmptyMessage request (since there are no parameters)
        EmptyMessage request = EmptyMessage.newBuilder().build();

        // Call the gRPC method from the dataServerStub
        BusinessListResponse response = dataServerStub.getBusinesses(request);
//        System.out.println("Received response from dataServerStub: " + response);

        // Map the response to a list of BusinessResponseDto objects
        return response.getBusinessesList().stream()
                .map(BusinessDtoGrpcConverter::BusinessResponseGrpc_To_BusinessResponseDto)
                .collect(Collectors.toList());
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
        BusinessUpdateRequestDto updatedProfile) throws IOException {

        if (updatedProfile.getBusinessName().contains("invalid")) {
            throw new IllegalArgumentException("Business name contains invalid content.");
        }

        System.out.println("updateBusinessProfile method called for ID: " + updatedProfile.getId());

        System.out.println("Updated Profile Details:");
        System.out.println("Business Name: " + updatedProfile.getBusinessName());
        System.out.println("Email: " + updatedProfile.getEmail());
        System.out.println("Phone Number: " + updatedProfile.getPhoneNumber());

        imageStorageService.updateImage(updatedProfile.getImageName(), updatedProfile.getImage());
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
