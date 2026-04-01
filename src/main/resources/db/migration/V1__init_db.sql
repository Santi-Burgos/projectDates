CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS roles (
  rol_id SERIAL PRIMARY KEY,
  rol_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS permission (
  permission_id SERIAL PRIMARY KEY,
  permission_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS role_permission (
  rol_id INTEGER,
  permission_id INTEGER,
  CONSTRAINT pk_role_permission PRIMARY KEY (rol_id, permission_id),
  CONSTRAINT fk_role_permission_roles 
    FOREIGN KEY (rol_id) REFERENCES roles(rol_id) 
    ON DELETE CASCADE,
  CONSTRAINT fk_role_permission_permission 
    FOREIGN KEY (permission_id) REFERENCES permission(permission_id) 
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
  user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  username VARCHAR(100),
  lastname VARCHAR(100),
  birthday DATE,
  rol_id INTEGER,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_users_roles 
    FOREIGN KEY (rol_id) REFERENCES roles(rol_id) 
    ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS concept (
  concept_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name VARCHAR(100) NOT NULL,
  details TEXT,
  is_active BOOLEAN DEFAULT true,
  capacity INTEGER DEFAULT 1,
  is_24h BOOLEAN DEFAULT false
);

CREATE TABLE IF NOT EXISTS slot (
  slot_id SERIAL PRIMARY KEY,
  start_at TIME NOT NULL,
  end_at TIME NOT NULL,
  concept_id UUID,
  CONSTRAINT fk_slot_concept 
    FOREIGN KEY (concept_id) REFERENCES concept(concept_id) 
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS appointments (
  appointments_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID,
  concept_id UUID,
  start_at TIMESTAMP WITH TIME ZONE NOT NULL,
  end_at TIMESTAMP WITH TIME ZONE NOT NULL,
  status VARCHAR(20) DEFAULT 'scheduled',
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_appointments_user 
    FOREIGN KEY (user_id) REFERENCES users(user_id) 
    ON DELETE CASCADE,
  CONSTRAINT fk_appointments_concept 
    FOREIGN KEY (concept_id) REFERENCES concept(concept_id) 
    ON DELETE RESTRICT
);