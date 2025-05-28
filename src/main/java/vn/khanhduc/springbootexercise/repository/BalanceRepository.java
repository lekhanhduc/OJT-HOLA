package vn.khanhduc.springbootexercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.khanhduc.springbootexercise.entity.Balance;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
}
