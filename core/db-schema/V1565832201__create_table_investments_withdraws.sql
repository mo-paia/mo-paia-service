create table mopaia.investments_withdraws (
    id uuid primary key,
    investment_id uuid references mopaia.investments(id) not null,
    amount numeric(6, 2) not null,
    destination text not null,
    destination_id uuid not null,
    created_at timestamp with time zone not null
);
