package vn.huythanh.springbootexercise.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "invalid_tokens")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InvalidToken {
    @Id
    private String id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String token;

    private Date expirationTime;
}
