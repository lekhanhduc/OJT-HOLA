package vn.huythanh.springbootexercise.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "Balance")
@Table(name = "balances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Balance extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "available_balance", nullable = false)
    @Builder.Default
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @Column(name = "hold_balance", nullable = false)
    @Builder.Default
    private BigDecimal holdBalance = BigDecimal.ZERO;

    @Column(name = "total_balance", insertable = false, updatable = false)
    private BigDecimal totalBalance;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    // One-to-One relationship với Account
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @Transient
    public BigDecimal getTotalBalance() {
        return availableBalance.add(holdBalance);
    }

}