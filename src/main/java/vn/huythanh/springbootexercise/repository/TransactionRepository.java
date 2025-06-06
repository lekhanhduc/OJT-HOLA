package vn.huythanh.springbootexercise.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.huythanh.springbootexercise.common.TransactionStatus;
import vn.huythanh.springbootexercise.common.TransactionType;
import vn.huythanh.springbootexercise.entity.Account;
import vn.huythanh.springbootexercise.entity.Card;
import vn.huythanh.springbootexercise.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCardAndStatus(Card card, TransactionStatus status);

    Page<Transaction> findByAccount(Account account, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.account = :account AND t.status = :status")
    List<Transaction> findByAccountAndStatus(@Param("account") Account account,
                                             @Param("status") TransactionStatus status);

    @Query("SELECT t FROM Transaction t WHERE t.account = :account AND t.transactionType = :type")
    List<Transaction> findByAccountAndTransactionType(@Param("account") Account account,
                                                      @Param("type") TransactionType type);

    @Query("SELECT t FROM Transaction t WHERE t.account = :account AND t.createdAt BETWEEN :startDate AND :endDate")
    List<Transaction> findByAccountAndDateRange(@Param("account") Account account,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);
}
