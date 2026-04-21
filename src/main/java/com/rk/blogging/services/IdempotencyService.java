package com.rk.blogging.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rk.blogging.model.IdempotencyKey;
import com.rk.blogging.repository.IdempotencyRepository;
import com.rk.blogging.utils.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    @Autowired
    private IdempotencyRepository repository;

    @Autowired
    private HashUtil hashUtil;

    private final ObjectMapper mapper = new ObjectMapper();

    // 🔹 Find existing key
    public Optional<IdempotencyKey> find(String key) {
        return repository.findById(key);
    }

    // 🔹 Generate hash from request
    public String generateHash(Object request) throws JsonProcessingException {
        String json = mapper.writeValueAsString(request);
        return hashUtil.hash(json);
    }

    // 🔹 Validate existing record (IMPORTANT)
    public IdempotencyKey validateAndReturn(String key, String requestHash) {
        IdempotencyKey record = repository.findById(key)
                .orElseThrow(() -> new RuntimeException("Idempotency key not found"));

        // 🔸 Expiry check
        if (record.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Idempotency key expired");
        }

        // 🔸 Hash mismatch → same key used with different request
        if (!record.getRequestHash().equals(requestHash)) {
            throw new RuntimeException("Idempotency key reused with different request");
        }

        return record;
    }

    // 🔹 Save record safely
    public void save(String key, String requestHash,
                     String response, int status) {

        IdempotencyKey record = new IdempotencyKey();
        record.setIdempotencyKey(key);
        record.setRequestHash(requestHash);
        record.setResponse(response);
        record.setStatusCode(status);
        record.setCreatedAt(LocalDateTime.now());
        record.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        try {
            repository.save(record);
        } catch (Exception e) {
            // 🔥 Handles race condition
            throw new RuntimeException("Duplicate idempotency key detected");
        }
    }

}
