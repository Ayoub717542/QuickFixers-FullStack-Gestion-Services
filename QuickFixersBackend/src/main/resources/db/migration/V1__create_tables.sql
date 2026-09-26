CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(50),
    service_type VARCHAR(50)
);

CREATE TABLE services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255),
    statut VARCHAR(50),
    type VARCHAR(50),
    prix DECIMAL(10, 2),
    created_by BIGINT
);

CREATE TABLE ticket (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255),
    description TEXT,
    date_creation DATETIME,
    statut VARCHAR(50),
    assigned_to BIGINT,
    created_by BIGINT,
    prix DECIMAL(10, 2),
    service_id BIGINT
);

CREATE TABLE paiement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    montant DECIMAL(10, 2),
    statut VARCHAR(50),
    date_creation DATETIME,
    ticket_id BIGINT,
    user_id BIGINT
);

CREATE TABLE message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contenu TEXT,
    date_envoi DATETIME,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT,
    ticket_id BIGINT
);