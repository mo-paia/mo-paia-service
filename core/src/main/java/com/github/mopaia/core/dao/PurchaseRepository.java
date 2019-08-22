package com.github.mopaia.core.dao;

import com.github.mopaia.core.dao.model.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PurchaseRepository {

    private static final RowMapper<Purchase> purchaseRowMapper = (rs, i) -> new Purchase(
            UUID.fromString(rs.getString("id")),
            rs.getString("provider"),
            rs.getString("provider_url"),
            rs.getString("provider_order_id"),
            rs.getString("tracking_code"),
            Optional.ofNullable(rs.getTimestamp("arrived_at")).map(Timestamp::toInstant).orElse(null),
            rs.getTimestamp("created_at").toInstant());

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<Purchase> list() {
        return jdbcTemplate.query(
                "select id, provider, provider_url, provider_order_id, tracking_code, arrived_at, created_at from purchases order by created_at desc",
                purchaseRowMapper);
    }

    @Transactional(readOnly = true)
    public Optional<Purchase> getById(UUID id) {
        return jdbcTemplate.query(
                "select id, provider, provider_url, provider_order_id, tracking_code, arrived_at, created_at from purchases where id = :id",
                Collections.singletonMap("id", id),
                purchaseRowMapper).stream().findAny();
    }

    @Transactional
    public Optional<Purchase> create(String provider, String providerUrl, String providerOrderId, String trackingCode, Instant arrivedAt) {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();
        return jdbcTemplate.update("insert into purchases (id, provider, provider_url, provider_order_id, tracking_code, arrived_at, created_at) values (:id, :provider, :provider_url, :provider_order_id, :tracking_code, :arrived_at, :created_at)",
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("provider", provider)
                        .addValue("provider_url", sanitize(providerUrl))
                        .addValue("provider_order_id", sanitize(providerOrderId))
                        .addValue("tracking_code", sanitize(trackingCode))
                        .addValue("arrived_at", Optional.ofNullable(arrivedAt).map(Timestamp::from).orElse(null))
                        .addValue("created_at", Timestamp.from(createdAt))) > 0
                ? Optional.of(new Purchase(id, provider, providerUrl, providerOrderId, trackingCode, arrivedAt, createdAt))
                : Optional.empty();
    }

    @Transactional
    public Optional<Purchase> update(UUID id, String provider, String providerUrl, String providerOrderId, String trackingCode, Instant arrivedAt) {
        List<Instant> returning = jdbcTemplate.query("update purchases set provider = :provider, provider_url = :provider_url, provider_order_id = :provider_order_id, tracking_code = :tracking_code, arrived_at = :arrived_at where id = :id returning created_at",
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("provider", provider)
                        .addValue("provider_url", sanitize(providerUrl))
                        .addValue("provider_order_id", sanitize(providerOrderId))
                        .addValue("tracking_code", sanitize(trackingCode))
                        .addValue("arrived_at", Optional.ofNullable(arrivedAt).map(Timestamp::from).orElse(null)),
                (rs, i) -> rs.getTimestamp("created_at").toInstant());
        return returning.isEmpty()
                ? Optional.empty()
                : Optional.of(new Purchase(id, provider, providerUrl, providerOrderId, trackingCode, arrivedAt, returning.get(0)));
    }

    private String sanitize(String dirty) {
        return Optional.ofNullable(dirty)
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .orElse(null);
    }
}
