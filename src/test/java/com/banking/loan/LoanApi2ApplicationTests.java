package com.banking.loan;

import com.banking.loan.dto.request.LoanRequestDto;
import com.banking.loan.model.Customer;
import com.banking.loan.model.Loan;
import com.banking.loan.repository.CustomerRepository;
import com.banking.loan.repository.LoanInstallmentRepository;
import com.banking.loan.repository.LoanRepository;
import com.banking.loan.service.LoanService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class LoanApi2ApplicationTests {

    private Validator validator;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LoanInstallmentRepository installmentRepository;

    @InjectMocks
    private LoanService loanService;

    public LoanApi2ApplicationTests() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void setUp() {
        // Initialize the validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testCreateLoan_Success() {
        // Arrange
        LoanRequestDto request = new LoanRequestDto();
        request.setCustomerId(1L);
        request.setAmount(BigDecimal.valueOf(1000));
        request.setInterestRate(0.2);
        request.setNumberOfInstallments(12);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCreditLimit(BigDecimal.valueOf(20000));
        customer.setUsedCreditLimit(BigDecimal.ZERO);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Loan createdLoan = loanService.createLoan(request);

        // Assert
        assertEquals(BigDecimal.valueOf(1200.0), createdLoan.getLoanAmount()); // 1000 * 1.2
        assertEquals(12, createdLoan.getNumberOfInstallment());
        verify(customerRepository, times(1)).save(customer);
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    public void testCreateLoan_InsufficientCreditLimit() {
        // Arrange
        LoanRequestDto request = new LoanRequestDto();
        request.setCustomerId(1L);
        request.setAmount(BigDecimal.valueOf(15000));
        request.setInterestRate(0.2);
        request.setNumberOfInstallments(12);

        Set<ConstraintViolation<LoanRequestDto>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "No validation errors should occur for valid input");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCreditLimit(BigDecimal.valueOf(10000));
        customer.setUsedCreditLimit(BigDecimal.ZERO);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Act & Assert
        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> loanService.createLoan(request)
        );

        assertEquals("Insufficient credit limit", exception.getMessage());
    }

    @Test
    public void testCreateLoan_InvalidInstallmentCount() {
        // Arrange
        LoanRequestDto request = new LoanRequestDto();
        request.setCustomerId(1L);
        request.setAmount(BigDecimal.valueOf(1005));
        request.setInterestRate(0.2);
        request.setNumberOfInstallments(10);

        Set<ConstraintViolation<LoanRequestDto>> violations = validator.validate(request);
        assertEquals("Invalid number of installments, must be 6, 9, 12, or 24",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCreateLoan_InvalidInterestRate() {
        // Arrange
        LoanRequestDto request = new LoanRequestDto();
        request.setCustomerId(1L);
        request.setAmount(BigDecimal.valueOf(3000));
        request.setInterestRate(0.6);
        request.setNumberOfInstallments(6);

        Set<ConstraintViolation<LoanRequestDto>> violations = validator.validate(request);
        assertEquals("Interest rate must be between 0.1 and 0.5",
                violations.iterator().next().getMessage());
    }
}
