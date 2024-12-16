package com.banking.loan.service;

import com.banking.loan.dto.request.LoanRequestDto;
import com.banking.loan.dto.response.PaymentResultDto;
import com.banking.loan.model.Customer;
import com.banking.loan.model.Loan;
import com.banking.loan.model.LoanInstallment;
import com.banking.loan.repository.CustomerRepository;
import com.banking.loan.repository.LoanInstallmentRepository;
import com.banking.loan.repository.LoanRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanInstallmentRepository installmentRepository;

    @Transactional
    public Loan createLoan(LoanRequestDto request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        BigDecimal totalLoanAmount = request.getAmount().multiply(BigDecimal.valueOf(1 + request.getInterestRate()));

        if (customer.getCreditLimit().subtract(customer.getUsedCreditLimit()).compareTo(totalLoanAmount) < 0) {
            throw new IllegalArgumentException("Insufficient credit limit");
        }

        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setLoanAmount(totalLoanAmount);
        loan.setNumberOfInstallment(request.getNumberOfInstallments());
        loan.setCreateDate(LocalDate.now());
        loan.setIsPaid(false);
        loanRepository.save(loan);

        LocalDate dueDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        BigDecimal installmentAmount = totalLoanAmount.divide(BigDecimal.valueOf(request.getNumberOfInstallments()), BigDecimal.ROUND_HALF_UP);

        for (int i = 0; i < request.getNumberOfInstallments(); i++) {
            LoanInstallment installment = new LoanInstallment();
            installment.setLoan(loan);
            installment.setAmount(installmentAmount);
            installment.setPaidAmount(BigDecimal.ZERO);
            installment.setDueDate(dueDate);
            installment.setIsPaid(false);
            installmentRepository.save(installment);
            dueDate = dueDate.plusMonths(1);
        }

        customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(totalLoanAmount));
        customerRepository.save(customer);

        return loan;
    }

    public List<Loan> listLoans(Long customerId, Boolean isPaid) {
        if (isPaid == null) {
            return loanRepository.findByCustomerId(customerId);
        }
        return loanRepository.findByCustomerIdAndIsPaid(customerId, isPaid);
    }

    public List<LoanInstallment> listInstallments(Long loanId) {
        return installmentRepository.findByLoanId(loanId);
    }

    @Transactional
    public PaymentResultDto payLoan(Long loanId, BigDecimal amount) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        List<LoanInstallment> installments = installmentRepository.findByLoanIdAndIsPaidFalseOrderByDueDateAsc(loanId);
        BigDecimal remainingAmount = amount;
        int installmentsPaid = 0;
        BigDecimal totalPaid = BigDecimal.ZERO;

        for (LoanInstallment installment : installments) {
            if (YearMonth.from(installment.getDueDate()).isAfter(YearMonth.from(LocalDate.now().plusMonths(3)))) {
                continue;
            }

            // Calculate reward or penalty
            LocalDate dueDate = installment.getDueDate();
            LocalDate paymentDate = LocalDate.now();
            BigDecimal adjustedAmount = installment.getAmount();

            if (paymentDate.isBefore(dueDate)) {
                // Reward for early payment
                long daysBeforeDue = java.time.temporal.ChronoUnit.DAYS.between(paymentDate, dueDate);
                BigDecimal discount = BigDecimal.valueOf(0.001).multiply(BigDecimal.valueOf(daysBeforeDue));
                adjustedAmount = adjustedAmount.subtract(adjustedAmount.multiply(discount)); // Apply discount
            } else if (paymentDate.isAfter(dueDate)) {
                // Penalty for late payment
                long daysAfterDue = java.time.temporal.ChronoUnit.DAYS.between(dueDate, paymentDate);
                BigDecimal penalty = BigDecimal.valueOf(0.001).multiply(BigDecimal.valueOf(daysAfterDue));
                adjustedAmount = adjustedAmount.add(adjustedAmount.multiply(penalty)); // Apply penalty
            }

            if (remainingAmount.compareTo(adjustedAmount) >= 0) {
                installment.setPaidAmount(adjustedAmount); // Set the adjusted paid amount
                installment.setIsPaid(true);
                installment.setPaymentDate(paymentDate);

                remainingAmount = remainingAmount.subtract(adjustedAmount);
                totalPaid = totalPaid.add(adjustedAmount);
                installmentsPaid++;
                installmentRepository.save(installment);
            } else {
                break; // Stop if remaining amount is insufficient
            }
        }

        boolean isLoanFullyPaid = installmentRepository.countByLoanIdAndIsPaidFalse(loanId) == 0;

        if (isLoanFullyPaid) {
            loan.setIsPaid(true);
            loanRepository.save(loan);
        }

        Customer customer = loan.getCustomer();
        customer.setUsedCreditLimit(customer.getUsedCreditLimit().subtract(totalPaid));
        customerRepository.save(customer);

        return new PaymentResultDto(installmentsPaid, totalPaid, isLoanFullyPaid);
    }
}

