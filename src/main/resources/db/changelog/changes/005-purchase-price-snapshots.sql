ALTER TABLE items ALTER COLUMN price TYPE numeric(19, 2) USING round(price::numeric, 2);
ALTER TABLE items ALTER COLUMN price SET NOT NULL;
ALTER TABLE items ADD CONSTRAINT items_price_check CHECK (price > 0);
ALTER TABLE projections ALTER COLUMN price TYPE numeric(19, 2) USING round(price::numeric, 2);
ALTER TABLE projections ALTER COLUMN price SET NOT NULL;
ALTER TABLE projections ADD CONSTRAINT projections_price_check CHECK (price > 0);
ALTER TABLE orders ALTER COLUMN total_price TYPE numeric(19, 2) USING round(total_price::numeric, 2);
ALTER TABLE orders ALTER COLUMN total_price SET NOT NULL;
ALTER TABLE orders ADD CONSTRAINT orders_total_price_check CHECK (total_price >= 0);
ALTER TABLE orders ADD COLUMN discount_percentage integer;
ALTER TABLE orders ADD CONSTRAINT orders_discount_percentage_check CHECK (discount_percentage BETWEEN 0 AND 50);

ALTER TABLE tickets ADD COLUMN unit_price numeric(19, 2);
ALTER TABLE tickets ADD COLUMN paid_price numeric(19, 2);
UPDATE tickets ticket SET unit_price = projection.price FROM projections projection WHERE projection.id = ticket.projection_id;
ALTER TABLE tickets ALTER COLUMN unit_price SET NOT NULL;
ALTER TABLE tickets ADD CONSTRAINT tickets_unit_price_check CHECK (unit_price > 0);
ALTER TABLE tickets ADD CONSTRAINT tickets_paid_price_check CHECK (paid_price >= 0);

CREATE TABLE orders_items_snapshot (
    order_id integer NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    item_id integer NOT NULL REFERENCES items (id),
    name varchar(255) NOT NULL,
    quantity integer NOT NULL CHECK (quantity > 0),
    unit_price numeric(19, 2) NOT NULL CHECK (unit_price > 0),
    paid_total numeric(19, 2) NOT NULL CHECK (paid_total >= 0),
    CONSTRAINT orders_items_pkey PRIMARY KEY (order_id, item_id)
);

INSERT INTO orders_items_snapshot (order_id, item_id, name, quantity, unit_price, paid_total)
SELECT line.order_id, line.item_id, item.name, count(*)::integer, item.price, 0
FROM orders_items line
JOIN items item ON item.id = line.item_id
GROUP BY line.order_id, line.item_id, item.name, item.price;

DROP TABLE orders_items;
ALTER TABLE orders_items_snapshot RENAME TO orders_items;

CREATE TEMPORARY TABLE purchase_line_allocations ON COMMIT DROP AS
WITH lines AS (
    SELECT link.order_id, 0 AS line_type, ticket.id AS line_id, ticket.unit_price AS gross
    FROM orders_tickets link
    JOIN tickets ticket ON ticket.id = link.tickets_id
    UNION ALL
    SELECT order_id, 1, item_id, unit_price * quantity FROM orders_items
), weights AS (
    SELECT lines.*, purchase.total_price,
        sum(gross) OVER (PARTITION BY order_id) AS gross_total,
        sum(gross) OVER (PARTITION BY order_id ORDER BY line_type, line_id ROWS UNBOUNDED PRECEDING) AS cumulative_gross
    FROM lines
    JOIN orders purchase ON purchase.id = lines.order_id
), targets AS (
    SELECT weights.*, round(total_price * cumulative_gross / nullif(gross_total, 0), 2) AS cumulative_paid
    FROM weights
)
SELECT order_id, line_type, line_id,
    cumulative_paid - lag(cumulative_paid, 1, 0::numeric) OVER (PARTITION BY order_id ORDER BY line_type, line_id) AS paid
FROM targets;

UPDATE tickets ticket SET paid_price = line.paid
FROM purchase_line_allocations line WHERE line.line_type = 0 AND line.line_id = ticket.id;
UPDATE orders_items item SET paid_total = line.paid
FROM purchase_line_allocations line
WHERE line.line_type = 1 AND line.order_id = item.order_id AND line.line_id = item.item_id;

CREATE INDEX orders_items_item_id_idx ON orders_items (item_id);
CREATE INDEX orders_tickets_order_id_idx ON orders_tickets (order_id);
CREATE INDEX orders_date_of_purchase_idx ON orders (date_of_purchase);
