create table mopaia.investments_deposits (
    id uuid primary key,
    investment_id uuid references mopaia.investments(id) not null,
    investor_name text not null,
    amount numeric(6, 2) not null,
    created_at timestamp with time zone not null
);
