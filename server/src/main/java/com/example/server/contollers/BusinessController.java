package com.example.server.contollers;

import com.example.server.dto.business.BusinessResponseDto;
import com.example.server.dto.business.BusinessUpdateRequestDto;
import com.example.server.services.IBusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/businesses") public class BusinessController {


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
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<BusinessResponseDto> updateBusinessProfile(@RequestBody BusinessUpdateRequestDto updatedProfile) {

        // Business logic
        BusinessResponseDto responseDto = businessService.updateBusinessProfile(updatedProfile);
        try{
            return ResponseEntity.ok(responseDto);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().build();
        }
    }
}
