package com.banking.loan;

import com.banking.loan.model.Customer;
import com.banking.loan.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class LoanApi2Application {

    public static void main(String[] args) {
        SpringApplication.run(LoanApi2Application.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(CustomerRepository customerRepository) {
        return args -> {
            customerRepository.save(new Customer("John", "Doe", BigDecimal.valueOf(10000), BigDecimal.ZERO));
            customerRepository.save(new Customer("Jane", "Smith", BigDecimal.valueOf(15000), BigDecimal.ZERO));
            customerRepository.save(new Customer("Alice", "Johnson", BigDecimal.valueOf(20000), BigDecimal.ZERO));
            customerRepository.save(new Customer("Bob", "Williams", BigDecimal.valueOf(5000), BigDecimal.ZERO));
            customerRepository.save(new Customer("Charlie", "Brown", BigDecimal.valueOf(12000), BigDecimal.ZERO));
        };
    }
}
