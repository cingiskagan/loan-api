package com.banking.loan.controller;

import com.banking.loan.dto.response.CustomerDto;
import com.banking.loan.model.Customer;
import com.banking.loan.model.Loan;
import com.banking.loan.service.CustomerService;
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

    @GetMapping()
    public ResponseEntity<List<CustomerDto>> listCustomers() {
        List<CustomerDto> customers = customerService.listCustomers()
                .stream()
                .map(CustomerDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customers);
    }
}
