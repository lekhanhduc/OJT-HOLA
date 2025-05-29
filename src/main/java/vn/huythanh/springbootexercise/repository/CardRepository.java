package vn.huythanh.springbootexercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.huythanh.springbootexercise.entity.Card;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByAccountId(Long accountId);

    @Query("select c from Card c where c.account.id=:accountId and c.cardId=:cardId")
    Optional<Card> findByAccountIdAndCardId(Long accountId, Long cardId);
}
