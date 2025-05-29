package vn.huythanh.springbootexercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.huythanh.springbootexercise.common.TransactionStatus;
import vn.huythanh.springbootexercise.entity.Card;
import vn.huythanh.springbootexercise.entity.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCardAndStatus(Card card, TransactionStatus status);
}
