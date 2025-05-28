package vn.khanhduc.springbootexercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.khanhduc.springbootexercise.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
