package com.example.server.contollers;


import com.example.server.dto.business.BusinessResponseDto;
import com.example.server.services.IBusinessService;
import com.example.server.services.Implementations.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/business") public class BusinessController {


    private final IBusinessService businessService;

    public BusinessController(IBusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("/{id}") public ResponseEntity<BusinessResponseDto> getBusinessById(
            @PathVariable String id)
    {
        try {
            BusinessResponseDto business = businessService.getBusinessById(id);
            return ResponseEntity.ok(business);
        }
        catch (Exception e) {
            //  TODO: Change this to proper exception response
            return ResponseEntity.notFound().build();
        }
    }
}
