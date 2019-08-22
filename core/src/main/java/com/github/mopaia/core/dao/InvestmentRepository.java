package com.github.mopaia.core.dao;

import com.github.mopaia.core.dao.model.Investment;
import com.github.mopaia.core.dao.model.InvestmentDeposit;
import com.github.mopaia.core.dao.model.InvestmentWithdraw;
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

    private static final RowMapper<InvestmentWithdraw> investmentWithdrawRowMapper = (rs, i) -> new InvestmentWithdraw(
            UUID.fromString(rs.getString("id")),
            UUID.fromString(rs.getString("investment_id")),
            rs.getBigDecimal("amount"),
            rs.getString("destination"),
            UUID.fromString(rs.getString("destination_id")),
            rs.getTimestamp("created_at").toInstant());

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<Investment> list() {
        return jdbcTemplate.query(
                "select id, name, created_at from investments order by created_at desc",
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
    public Optional<InvestmentDeposit> deposit(UUID investmentId, String investorName, BigDecimal amount) {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();
        return jdbcTemplate.update("insert into investments_deposits (id, investment_id, investor_name, amount, created_at) values (:id, :investment_id, :investor_name, :amount, :created_at)",
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("investment_id", investmentId)
                        .addValue("investor_name", investorName)
                        .addValue("amount", amount)
                        .addValue("created_at", Timestamp.from(createdAt))) > 0
                ? Optional.of(new InvestmentDeposit(id, investmentId, investorName, amount, createdAt))
                : Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<InvestmentDeposit> listDeposits(UUID investmentId) {
        return jdbcTemplate.query("select id, investment_id, investor_name, amount, created_at from investments_deposits where investment_id = :investment_id order by created_at desc",
                Collections.singletonMap("investment_id", investmentId),
                investmentDepositRowMapper);
    }

    @Transactional
    public Optional<InvestmentWithdraw> withdraw(UUID investmentId, BigDecimal amount, String destination, UUID destinationId) {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();
        return jdbcTemplate.update("insert into investments_withdraws (id, investment_id, amount, destination, destination_id, created_at) values (:id, :investment_id, :amount, :destination, :destination_id, :created_at)",
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("investment_id", investmentId)
                        .addValue("amount", amount)
                        .addValue("destination", destination)
                        .addValue("destination_id", destinationId)
                        .addValue("created_at", Timestamp.from(createdAt))) > 0
                ? Optional.of(new InvestmentWithdraw(id, investmentId, amount, destination, destinationId, createdAt))
                : Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<InvestmentWithdraw> listWithdraws(UUID investmentId) {
        return jdbcTemplate.query("select id, investment_id, amount, destination, destination_id, created_at from investments_withdraws where investment_id = :investment_id order by created_at desc",
                Collections.singletonMap("investment_id", investmentId),
                investmentWithdrawRowMapper);
    }

}
