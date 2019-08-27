package com.github.mopaia.core.dao;

import com.github.mopaia.core.dao.model.PurchaseExpense;
import lombok.Getter;
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
public class PurchaseExpensesRepository {

    private static final RowMapper<PurchaseExpense> purchaseExpenseRowMapper = (rs, i) -> new PurchaseExpense(
            UUID.fromString(rs.getString("id")),
            UUID.fromString(rs.getString("purchase_id")),
            PurchaseExpense.Type.valueOf(rs.getString("type")),
            rs.getInt("quantity"),
            rs.getBigDecimal("unit_price"),
            rs.getTimestamp("created_at").toInstant());

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<PurchaseExpense> listByPurchaseId(UUID purchaseId) {
        return jdbcTemplate.query("select id, purchase_id, type, quantity, unit_price, created_at from purchase_expenses where purchase_id = :purchase_id order by created_at desc",
                Collections.singletonMap("purchase_id", purchaseId),
                purchaseExpenseRowMapper);
    }

    @Transactional
    public Optional<PurchaseExpense> createOnPurchase(UUID purchaseId, PurchaseExpense.Type type, Integer quantity, BigDecimal unitPrice) {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();
        return jdbcTemplate.update("insert into purchase_expenses (id, purchase_id, type, quantity, unit_price, created_at) values (:id, :purchase_id, :type, :quantity, :unit_price, :created_at)",
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("purchase_id", purchaseId)
                        .addValue("type", type.toString())
                        .addValue("quantity", quantity)
                        .addValue("unit_price", unitPrice)
                        .addValue("created_at", Timestamp.from(createdAt))) > 0
                ? Optional.of(new PurchaseExpense(id, purchaseId, type, quantity, unitPrice, createdAt))
                : Optional.empty();
    }

    @Transactional
    public Optional<PurchaseExpense> update(UUID purchaseId, UUID expenseId, Integer quantity, BigDecimal unitPrice) {
        List<ExpenseTypeAndCreatedAtVO> returning = jdbcTemplate.query("update purchase_expenses set quantity = :quantity, unit_price = :unit_price where id = :id and purchase_id = :purchase_id returning type, created_at",
                new MapSqlParameterSource()
                        .addValue("id", expenseId)
                        .addValue("purchase_id", purchaseId)
                        .addValue("quantity", quantity)
                        .addValue("unit_price", unitPrice),
                (rs, i) -> new ExpenseTypeAndCreatedAtVO(PurchaseExpense.Type.valueOf(rs.getString("type")), rs.getTimestamp("created_at").toInstant()));
        return returning.isEmpty()
                ? Optional.empty()
                : Optional.of(new PurchaseExpense(expenseId, purchaseId, returning.get(0).getType(), quantity, unitPrice, returning.get(0).getCreatedAt()));
    }

    public Optional<UUID> delete(UUID purchaseId, UUID expenseId) {
        return jdbcTemplate.update("delete from purchase_expenses where id = :id and purchase_id = :purchase_id",
                new MapSqlParameterSource()
                        .addValue("id", expenseId)
                        .addValue("purchase_id", purchaseId)) > 0
                ? Optional.of(expenseId)
                : Optional.empty();
    }

    @Getter
    @RequiredArgsConstructor
    private static class ExpenseTypeAndCreatedAtVO {
        private final PurchaseExpense.Type type;
        private final Instant createdAt;
    }
}
