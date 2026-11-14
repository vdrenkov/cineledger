ALTER TABLE tickets DROP CONSTRAINT fk_tickets_projection_id;
ALTER TABLE tickets ADD CONSTRAINT fk_tickets_projection_id
    FOREIGN KEY (projection_id) REFERENCES projections (id) ON DELETE RESTRICT;
ALTER TABLE orders DROP CONSTRAINT fk_orders_user_id;
ALTER TABLE orders ADD CONSTRAINT fk_orders_user_id
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT;
ALTER TABLE orders_tickets DROP CONSTRAINT fk_orders_tickets_tickets_id;
ALTER TABLE orders_tickets ADD CONSTRAINT fk_orders_tickets_tickets_id
    FOREIGN KEY (tickets_id) REFERENCES tickets (id) ON DELETE RESTRICT;
