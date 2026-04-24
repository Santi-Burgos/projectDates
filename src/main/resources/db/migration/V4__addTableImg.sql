CREATE TABLE IF NOT EXISTS concept_img(
  concept_img_id SERIAL PRIMARY KEY,
  concept_img_name VARCHAR(75)  UNIQUE,
  concept_img_url VARCHAR(255)  UNIQUE,
  concept_id UUID,
  CONSTRAINT fk_img_concept
    FOREIGN KEY (concept_id) REFERENCES concept(concept_id)
    ON DELETE CASCADE
);