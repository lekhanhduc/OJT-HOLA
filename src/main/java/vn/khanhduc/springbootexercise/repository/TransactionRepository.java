package vn.khanhduc.springbootexercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.khanhduc.springbootexercise.common.TransactionStatus;
import vn.khanhduc.springbootexercise.entity.Card;
import vn.khanhduc.springbootexercise.entity.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCardAndStatus(Card card, TransactionStatus status);
}
