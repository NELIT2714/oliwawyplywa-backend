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
    option_price DECIMAL(10,2),
    CONSTRAINT fk_options_product
        FOREIGN KEY (id_product)
        REFERENCES tbl_products(id_product)
        ON DELETE CASCADE
);

-- tbl_admins
CREATE TABLE IF NOT EXISTS tbl_admins (
    id_admin INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

-- tbl_orders
CREATE TABLE IF NOT EXISTS tbl_orders (
    id_order INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- tbl_order_items
CREATE TABLE IF NOT EXISTS tbl_order_items (
    id_item INT PRIMARY KEY AUTO_INCREMENT,
    id_order INT NOT NULL,
    id_product_option INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (id_order)
        REFERENCES tbl_orders(id_order)
        ON DELETE CASCADE,
    CONSTRAINT fk_order_items_option
        FOREIGN KEY (id_product_option)
        REFERENCES tbl_products_options(id_product_option)
        ON DELETE CASCADE
);