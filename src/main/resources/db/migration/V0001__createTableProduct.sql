CREATE TABLE IF NOT EXISTS tb_product
(
    id                  SERIAL PRIMARY KEY,
    bar_code            BIGINT,
    category            VARCHAR(50),
    code                VARCHAR(8),
    color               VARCHAR(20),
    description         VARCHAR(255),
    expiration_date     TIMESTAMP,
    gross_amount        DECIMAL(19,2),
    manufacturing_date  TIMESTAMP,
    material            VARCHAR(20),
    name                VARCHAR(50),
    price               DECIMAL(19,2),
    quantity            INTEGER,
    series              VARCHAR(7),
    taxes               DECIMAL(19,2)
)