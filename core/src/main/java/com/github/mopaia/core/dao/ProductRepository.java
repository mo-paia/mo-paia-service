package com.github.mopaia.core.dao;

import com.github.mopaia.core.dao.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private static final RowMapper<Product> productRowMapper = (rs, i) -> new Product(
            UUID.fromString(rs.getString("id")),
            rs.getString("name"),
            rs.getTimestamp("created_at").toInstant());

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<Product> list() {
        return jdbcTemplate.query(
                "select id, name, created_at from products order by created_at desc",
                productRowMapper);
    }

}
