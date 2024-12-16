package com.banking.loan.service;

import com.banking.loan.model.Customer;
import com.banking.loan.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }
}
