package com.github.mopaia.core.dao;

import com.github.mopaia.core.dao.dto.ProductReviewDTO;
import com.github.mopaia.core.dao.model.ProductReview;
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
public class ProductReviewRepository {

    private static final RowMapper<ProductReviewDTO> productReviewDtoRowMapper = (rs, i) -> new ProductReviewDTO(
            UUID.fromString(rs.getString("id")),
            UUID.fromString(rs.getString("product_id")),
            rs.getString("product_name"),
            rs.getInt("score"),
            rs.getString("name"),
            rs.getString("comment"),
            rs.getTimestamp("created_at").toInstant());

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<ProductReviewDTO> list() {
        return jdbcTemplate.query(
                "select pr.id, pr.product_id, p.name as product_name, pr.score, pr.name, pr.comment, pr.created_at from product_reviews pr inner join products p on p.id = pr.product_id order by pr.created_at desc",
                productReviewDtoRowMapper);
    }

    @Transactional(readOnly = true)
    public List<ProductReviewDTO> filter(UUID productId) {
        return jdbcTemplate.query(
                "select pr.id, pr.product_id, p.name as product_name, pr.score, pr.name, pr.comment, pr.created_at from product_reviews pr inner join products p on p.id = pr.product_id where pr.product_id = :product_id order by pr.created_at desc",
                Collections.singletonMap("product_id", productId),
                productReviewDtoRowMapper);
    }

    @Transactional
    public Optional<ProductReview> create(UUID productId, int score, String name, String comment) {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();
        return jdbcTemplate.update("insert into product_reviews (id, product_id, score, name, comment, created_at) values (:id, :product_id, :score, :name, :comment, :created_at)",
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("product_id", productId)
                        .addValue("score", score)
                        .addValue("name", name)
                        .addValue("comment", comment)
                        .addValue("created_at", Timestamp.from(createdAt))) > 0
                ? Optional.of(new ProductReview(id, productId, score, name, comment, createdAt))
                : Optional.empty();
    }
}
