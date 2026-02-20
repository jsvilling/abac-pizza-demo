CREATE TABLE orders
(
    id         UUID PRIMARY KEY,
    user_id    VARCHAR(255) NOT NULL,
    pizza_name VARCHAR(255) NOT NULL,
    pizza_id   UUID NULL,
    CONSTRAINT fk_pizza
        FOREIGN KEY (pizza_id)
            REFERENCES pizzas (id)
            ON DELETE RESTRICT
);