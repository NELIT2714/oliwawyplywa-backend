-- tbl_categories
CREATE TABLE IF NOT EXISTS tbl_categories (
    id_category INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL UNIQUE
);

-- tbl_products
CREATE TABLE IF NOT EXISTS tbl_products (
    id_product INT PRIMARY KEY AUTO_INCREMENT,
    id_category INT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_description TEXT,
    CONSTRAINT fk_products_category
    FOREIGN KEY (id_category)
    REFERENCES tbl_categories(id_category)
    ON DELETE CASCADE
);

-- tbl_products_options
CREATE TABLE IF NOT EXISTS tbl_products_options (
    id_product_option INT PRIMARY KEY AUTO_INCREMENT,
    id_product INT NOT NULL,
    option_label VARCHAR(255) NOT NULL,
    option_price FLOAT,
    CONSTRAINT fk_options_product
    FOREIGN KEY (id_product)
    REFERENCES tbl_products(id_product)
    ON DELETE CASCADE
);
