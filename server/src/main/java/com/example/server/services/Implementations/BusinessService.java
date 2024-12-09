package com.example.server.services.Implementations;

import com.example.sep3.grpc.BusinessIdRequest;
import com.example.sep3.grpc.BusinessResponse;
import com.example.server.DataServerStub;
import com.example.server.converters.BusinessDtoGrpcConverter;
import com.example.server.dto.business.BusinessResponseDto;
import com.example.server.services.IBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service public class BusinessService implements IBusinessService {

    private final DataServerStub dataServerStub;

    @Autowired public BusinessService(DataServerStub dataServerStub) {
        this.dataServerStub = dataServerStub;
        System.out.println("BusinessService created");
    }
    @Override
    public BusinessResponseDto getBusinessById(String id){
        System.out.println("getBusinessById method called with id " + id);
        BusinessIdRequest request = BusinessIdRequest.newBuilder().setId(id).build();
        BusinessResponse response = dataServerStub.getBusinessById(request);
        System.out.println("Received response from dataServerStub " + response);

        return BusinessDtoGrpcConverter.BusinessResponseGrpc_To_BusinessResponseDto(response);
    }
}
