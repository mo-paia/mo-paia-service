create table mopaia.product_reviews (
    id uuid primary key,
    product_id uuid references mopaia.products(id) not null,
    score integer not null,
    name text,
    comment text,
    created_at timestamp with time zone not null
);
