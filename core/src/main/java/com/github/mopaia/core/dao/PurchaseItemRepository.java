package com.github.mopaia.core.dao;

import com.github.mopaia.core.dao.dto.PurchaseItemDTO;
import com.github.mopaia.core.dao.model.PurchaseItem;
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
public class PurchaseItemRepository {

    private static final RowMapper<PurchaseItemDTO> purchaseItemDtoRowMapper = (rs, i) -> new PurchaseItemDTO(
            UUID.fromString(rs.getString("id")),
            UUID.fromString(rs.getString("purchase_id")),
            UUID.fromString(rs.getString("product_id")),
            rs.getString("product_name"),
            rs.getInt("quantity"),
            rs.getBigDecimal("unit_price"),
            rs.getTimestamp("created_at").toInstant());

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<PurchaseItemDTO> listByPurchaseId(UUID purchaseId) {
        return jdbcTemplate.query("select pi.id, pi.purchase_id, pi.product_id, p.name as product_name, pi.quantity, pi.unit_price, pi.created_at from purchase_items pi inner join products p on p.id = pi.product_id where pi.purchase_id = :purchase_id order by pi.created_at desc",
                Collections.singletonMap("purchase_id", purchaseId),
                purchaseItemDtoRowMapper);
    }

    @Transactional
    public Optional<PurchaseItem> createOnPurchase(UUID purchaseId, UUID productId, Integer quantity, BigDecimal unitPrice) {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();
        return jdbcTemplate.update("insert into purchase_items (id, purchase_id, product_id, quantity, unit_price, created_at) values (:id, :purchase_id, :product_id, :quantity, :unit_price, :created_at)",
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("purchase_id", purchaseId)
                        .addValue("product_id", productId)
                        .addValue("quantity", quantity)
                        .addValue("unit_price", unitPrice)
                        .addValue("created_at", Timestamp.from(createdAt))) > 0
                ? Optional.of(new PurchaseItem(id, purchaseId, productId, quantity, unitPrice, createdAt))
                : Optional.empty();
    }

    @Transactional
    public Optional<PurchaseItem> update(UUID purchaseId, UUID productItemId, Integer quantity, BigDecimal unitPrice) {
        List<ProductItemProductIdAndCreatedAtVO> returning = jdbcTemplate.query("update purchase_items set quantity = :quantity, unit_price = :unit_price where id = :id and purchase_id = :purchase_id returning product_id, created_at",
                new MapSqlParameterSource()
                        .addValue("id", productItemId)
                        .addValue("purchase_id", purchaseId)
                        .addValue("quantity", quantity)
                        .addValue("unit_price", unitPrice),
                (rs, i) -> new ProductItemProductIdAndCreatedAtVO(UUID.fromString(rs.getString("product_id")), rs.getTimestamp("created_at").toInstant()));
        return returning.isEmpty()
                ? Optional.empty()
                : Optional.of(new PurchaseItem(productItemId, purchaseId, returning.get(0).getProductId(), quantity, unitPrice, returning.get(0).getCreatedAt()));
    }

    public Optional<UUID> delete(UUID purchaseId, UUID productItemId) {
        return jdbcTemplate.update("delete from purchase_items where id = :id and purchase_id = :purchase_id",
                new MapSqlParameterSource()
                        .addValue("id", productItemId)
                        .addValue("purchase_id", purchaseId)) > 0
                ? Optional.of(productItemId)
                : Optional.empty();
    }

    @Getter
    @RequiredArgsConstructor
    private static class ProductItemProductIdAndCreatedAtVO {
        private final UUID productId;
        private final Instant createdAt;
    }
}
