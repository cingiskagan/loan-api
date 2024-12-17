package com.banking.loan.controller;

import com.banking.loan.dto.response.CustomerDto;
import com.banking.loan.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "List all customers",
            description = "Retrieves a list of all customers, including their credit limit and usage details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<CustomerDto>> listCustomers() {
        List<CustomerDto> customers = customerService.listCustomers()
                .stream()
                .map(CustomerDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customers);
    }
}
