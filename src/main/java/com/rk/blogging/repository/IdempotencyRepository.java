package com.rk.blogging.repository;

import com.rk.blogging.model.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyRepository extends JpaRepository<IdempotencyKey, String> {

}
