create table mopaia.products (
    id uuid primary key,
    name text not null,
    created_at timestamp with time zone not null
);
