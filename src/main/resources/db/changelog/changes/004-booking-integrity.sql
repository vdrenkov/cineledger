ALTER TABLE projections ADD COLUMN runtime numeric(21, 0);
UPDATE projections projection SET runtime = movie.runtime FROM movies movie WHERE movie.id = projection.movie_id;
ALTER TABLE projections ALTER COLUMN runtime SET NOT NULL;
ALTER TABLE projections ADD CONSTRAINT projections_runtime_check CHECK (runtime > 0);
ALTER TABLE items ADD CONSTRAINT items_quantity_check CHECK (quantity >= 0);
CREATE INDEX projections_hall_id_idx ON projections (hall_id);
CREATE INDEX tickets_projection_id_idx ON tickets (projection_id);
CREATE INDEX orders_user_id_idx ON orders (user_id);
