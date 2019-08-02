package com.github.mopaia.core.dao;

import com.github.mopaia.core.dao.model.Investment;
import com.github.mopaia.core.dao.model.InvestmentDeposit;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InvestmentRepository {

    private static final RowMapper<Investment> investmentRowMapper = (rs, i) -> new Investment(
            UUID.fromString(rs.getString("id")),
            rs.getString("name"),
            rs.getTimestamp("created_at").toInstant());

    private static final RowMapper<InvestmentDeposit> investmentDepositRowMapper = (rs, i) -> new InvestmentDeposit(
            UUID.fromString(rs.getString("id")),
            UUID.fromString(rs.getString("investment_id")),
            rs.getString("investor_name"),
            rs.getBigDecimal("amount"),
            rs.getTimestamp("created_at").toInstant());

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<Investment> list() {
        return jdbcTemplate.query(
                "select id, name, created_at from investments",
                investmentRowMapper);
    }

    @Transactional(readOnly = true)
    public Optional<Investment> getById(UUID id) {
        return jdbcTemplate.query(
                "select id, name, created_at from investments where id = :id",
                Collections.singletonMap("id", id),
                investmentRowMapper).stream().findAny();
    }

    @Transactional
    public Optional<UUID> deposit(UUID investmentId, String investorName, BigDecimal amount) {
        UUID id = UUID.randomUUID();
        return jdbcTemplate.update("insert into investments_deposits (id, investment_id, investor_name, amount, created_at) values (:id, :investment_id, :investor_name, :amount, :created_at)",
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("investment_id", investmentId)
                        .addValue("investor_name", investorName)
                        .addValue("amount", amount)
                        .addValue("created_at", Timestamp.from(Instant.now()))) > 0
                ? Optional.of(id)
                : Optional.empty();
    }

    public List<InvestmentDeposit> listDeposits(UUID investmentId) {
        return jdbcTemplate.query("select id, investment_id, investor_name, amount, created_at from investments_deposits  where investment_id = :investment_id",
                Collections.singletonMap("investment_id", investmentId),
                investmentDepositRowMapper);
    }
}
