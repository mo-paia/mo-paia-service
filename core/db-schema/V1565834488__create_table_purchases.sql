create table mopaia.purchases (
    id uuid primary key,
    provider text not null,
    provider_url text,
    provider_order_id text,
    tracking_code text,
    arrived_at timestamp with time zone,
    created_at timestamp with time zone not null
);
