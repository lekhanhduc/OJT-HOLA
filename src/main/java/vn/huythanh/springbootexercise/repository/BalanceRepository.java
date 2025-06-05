package vn.huythanh.springbootexercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.huythanh.springbootexercise.entity.Account;
import vn.huythanh.springbootexercise.entity.Balance;

import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

    Optional<Balance> findByAccount(Account account);
}
