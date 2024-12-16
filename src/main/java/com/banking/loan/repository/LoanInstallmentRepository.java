package com.banking.loan.repository;

import com.banking.loan.model.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {
    List<LoanInstallment> findByLoanId(Long loanId);
    List<LoanInstallment> findByLoanIdAndIsPaidFalseOrderByDueDateAsc(Long loanId);
    long countByLoanIdAndIsPaidFalse(Long loanId);
}