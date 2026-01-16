USE orders_db;

-- =========================
-- 1) ROLES
-- =========================
INSERT INTO roles (name, description) VALUES
('ADMIN', 'System administrator'),
('STAFF', 'Sales staff'),
('WAREHOUSE', 'Warehouse staff');

-- =========================
-- 2) USERS
-- =========================
INSERT INTO users (username, email, password, full_name, phone, role_id, status) VALUES
('admin', 'admin@fer.com', '123456', 'Admin FER', '0900000001', 1, 'ACTIVE'),
('staff01', 'staff01@fer.com', '123456', 'Staff One', '0900000002', 2, 'ACTIVE'),
('warehouse01', 'warehouse@fer.com', '123456', 'Warehouse One', '0900000003', 3, 'ACTIVE');

-- =========================
-- 3) CATEGORIES
-- =========================
INSERT INTO categories (name, description, status) VALUES
('T-Shirts', 'FER T-Shirts', 'ACTIVE'),
('Hoodies', 'FER Hoodies', 'ACTIVE'),
('Accessories', 'FER Accessories', 'ACTIVE');

-- =========================
-- 4) PRODUCTS (10 items)
-- =========================
INSERT INTO products (sku, name, category_id, price, description, status) VALUES
('FER-TS-001', 'FER Classic T-Shirt', 1, 199000, 'Classic cotton T-shirt', 'ACTIVE'),
('FER-TS-002', 'FER Oversize T-Shirt', 1, 229000, 'Oversize fit, soft cotton', 'ACTIVE'),
('FER-TS-003', 'FER Graphic T-Shirt', 1, 249000, 'Graphic print, regular fit', 'ACTIVE'),
('FER-TS-004', 'FER Premium T-Shirt', 1, 279000, 'Premium fabric, minimal logo', 'ACTIVE'),

('FER-HD-001', 'FER Black Hoodie', 2, 399000, 'Black hoodie with FER logo', 'ACTIVE'),
('FER-HD-002', 'FER Grey Hoodie', 2, 419000, 'Grey hoodie, warm fleece', 'ACTIVE'),
('FER-HD-003', 'FER Zip Hoodie', 2, 459000, 'Zip hoodie, easy layering', 'ACTIVE'),
('FER-HD-004', 'FER Oversize Hoodie', 2, 499000, 'Oversize hoodie, heavy cotton', 'ACTIVE'),

('FER-AC-001', 'FER Cap', 3, 149000, 'FER branded cap', 'ACTIVE'),
('FER-AC-002', 'FER Tote Bag', 3, 179000, 'Canvas tote bag', 'ACTIVE');

-- =========================
-- 5) INVENTORY (auto create for all products)
-- =========================
INSERT INTO inventory (product_id, quantity, reserved_quantity)
SELECT p.id,
       CASE
         WHEN p.category_id = 1 THEN 120
         WHEN p.category_id = 2 THEN 60
         ELSE 200
       END AS quantity,
       0
FROM products p
LEFT JOIN inventory i ON i.product_id = p.id
WHERE i.product_id IS NULL;

-- =========================
-- 6) CUSTOMERS
-- =========================
INSERT INTO customers (full_name, phone, email, address) VALUES
('Nguyen Van A', '0911111111', 'a@gmail.com', 'Ho Chi Minh City'),
('Tran Thi B', '0922222222', 'b@gmail.com', 'Ha Noi');

-- =========================
-- 7) ORDERS
-- (Order 1: 1 áo 199k + 1 hoodie 399k = 598k)
-- =========================
INSERT INTO orders (order_code, customer_id, total_amount, status) VALUES
('ORD-202601-001', 1, 598000, 'CONFIRMED'),
('ORD-202601-002', 2, 249000, 'CREATED');

-- =========================
-- 8) ORDER ITEMS (by SKU => no wrong IDs)
-- =========================
-- Order 1
INSERT INTO order_items (order_id, product_id, price, quantity, subtotal)
SELECT 1, p.id, p.price, 1, p.price
FROM products p
WHERE p.sku IN ('FER-TS-001','FER-HD-001');

-- Order 2: 1 áo graphic 249k
INSERT INTO order_items (order_id, product_id, price, quantity, subtotal)
SELECT 2, p.id, p.price, 1, p.price
FROM products p
WHERE p.sku = 'FER-TS-003';

-- =========================
-- 9) PAYMENTS
-- =========================
INSERT INTO payments (order_id, method, status, transaction_code, paid_at) VALUES
(1, 'COD', 'SUCCESS', 'TXN-COD-001', NOW());

-- =========================
-- 10) AUDIT LOGS
-- =========================
INSERT INTO audit_logs (user_id, action, entity, entity_id) VALUES
(1, 'CREATE_PRODUCT', 'products', 1),
(2, 'CREATE_ORDER', 'orders', 1),
(2, 'CONFIRM_ORDER', 'orders', 1),
(1, 'RECEIVE_PAYMENT', 'payments', 1);