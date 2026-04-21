package com.rk.blogging.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "idempotency_keys")
public class IdempotencyKey {

    @Id
    private String idempotencyKey;

    private String requestHash;

    @Lob
    private String response;

    private Integer statusCode;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}
