package com.example.server.services;

import com.example.sep3.grpc.BusinessIdRequest;
import com.example.sep3.grpc.BusinessResponse;
import com.example.server.DataServerStub;
import com.example.server.converters.BusinessDtoGrpcConverter;
import com.example.server.dto.business.BusinessResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service public class BusinessService {

    private final DataServerStub dataServerStub;

    @Autowired public BusinessService(DataServerStub dataServerStub, ImageStorageService imageStorageService) {
        this.dataServerStub = dataServerStub;
        System.out.println("BusinessService created");
    }

    public BusinessResponseDto getBusinessById(String id){
        System.out.println("getBusinessById method called with id " + id);
        BusinessIdRequest request = BusinessIdRequest.newBuilder().setId(id).build();
        BusinessResponse response = dataServerStub.getBusinessById(request);
        System.out.println("Received response from dataServerStub " + response);

        return BusinessDtoGrpcConverter.BusinessResponseGrpc_To_BusinessResponseDto(response);
    }
}
