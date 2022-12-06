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
);

INSERT INTO tb_product (code, category, series, description, color, material, name, gross_amount, taxes, price, quantity, bar_code, manufacturing_date, expiration_date)
VALUES ('21g437s','FOOD','1/2022','Fruta','Vermelha','n/a','Maçã',10,10,11,150,123456789012, DATE '2022-11-20', DATE '2022-10-03');

INSERT INTO tb_product (code, category, series, description, color, material, name, gross_amount, taxes, price, quantity, bar_code, manufacturing_date, expiration_date)
VALUES ('21g438s','FOOD','1/2022','Fruta','Amarela','n/a','Banana',10,10,11,150,123456789013, DATE '2022-11-20', DATE '2022-10-03');

INSERT INTO tb_product (code, category, series, description, color, material, name, gross_amount, taxes, price, quantity, bar_code, manufacturing_date, expiration_date)
VALUES ('21g439s','FOOD','1/2022','Grão','Preto','n/a','Feijão',10,10,11,150,123456789014, DATE '2022-11-20', DATE '2022-10-03');