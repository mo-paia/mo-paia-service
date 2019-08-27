create table mopaia.purchase_expenses (
    id uuid primary key,
    purchase_id uuid references mopaia.purchases(id) not null,
    type text not null,
    quantity integer not null,
    unit_price numeric(6, 2) not null,
    created_at timestamp with time zone not null
);
