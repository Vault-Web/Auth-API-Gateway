package vaultweb.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;


import java.time.Instant;


@Table(name = "refresh_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken {
    @Id
    private Long id;

    @Column("user_id")
    private Integer userId; // Store foreign key as primitive type, no @OneToOne

    @Column("token")
    private String token;

    @Column("expiry_date")
    private Instant expiryDate;

    @Column("created_at")
    private Instant createdAt;
}
