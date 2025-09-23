-- tbl_categories
CREATE TABLE IF NOT EXISTS tbl_categories
(
    id_category   INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE
) ENGINE = InnoDB;

-- tbl_products
CREATE TABLE IF NOT EXISTS tbl_products
(
    id_product          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_category         INT UNSIGNED NOT NULL,
    product_name        VARCHAR(200) NOT NULL,
    product_description TEXT,
    product_image       VARCHAR(60) NOT NULL,
    INDEX idx_products_category (id_category),
    INDEX idx_products_name (product_name),
    CONSTRAINT fk_products_category
        FOREIGN KEY (id_category)
            REFERENCES tbl_categories (id_category)
            ON DELETE CASCADE
) ENGINE = InnoDB;

-- tbl_products_options
CREATE TABLE IF NOT EXISTS tbl_products_options
(
    id_product_option INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_product        INT UNSIGNED   NOT NULL,
    option_label      VARCHAR(150)   NOT NULL,
    option_price      DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    INDEX idx_options_product (id_product),
    INDEX idx_option_label (option_label),
    CONSTRAINT fk_options_product
        FOREIGN KEY (id_product)
            REFERENCES tbl_products (id_product)
            ON DELETE CASCADE
) ENGINE = InnoDB;

-- tbl_admins
CREATE TABLE IF NOT EXISTS tbl_admins
(
    id_admin      INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(100) NOT NULL UNIQUE,
    password_hash CHAR(60)     NOT NULL,
    INDEX idx_username (username)
) ENGINE = InnoDB;

-- tbl_orders
CREATE TABLE IF NOT EXISTS tbl_orders
(
    id_order   INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    full_name  VARCHAR(150)                                 NOT NULL,
    email      VARCHAR(150)                                 NOT NULL,
    address    VARCHAR(255)                                 NOT NULL,
    phone_number VARCHAR(20)                                NOT NULL,
    status     ENUM ("CREATED", "PAID") NOT NULL DEFAULT 'CREATED',
    created_at TIMESTAMP                                    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_orders_email (email),
    INDEX idx_orders_status (status),
    INDEX idx_orders_created (created_at)
) ENGINE = InnoDB;

-- tbl_order_items
CREATE TABLE IF NOT EXISTS tbl_order_items
(
    id_item           INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_order          INT UNSIGNED      NOT NULL,
    id_product_option INT UNSIGNED      NOT NULL,
    quantity          SMALLINT UNSIGNED NOT NULL,
    price             DECIMAL(10, 2)    NOT NULL,
    INDEX idx_order_items_order (id_order),
    INDEX idx_order_items_option (id_product_option),
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (id_order)
            REFERENCES tbl_orders (id_order)
            ON DELETE CASCADE,
    CONSTRAINT fk_order_items_option
        FOREIGN KEY (id_product_option)
            REFERENCES tbl_products_options (id_product_option)
            ON DELETE CASCADE
) ENGINE = InnoDB;
