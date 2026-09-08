CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       nom VARCHAR(255),
                       prenom VARCHAR(255),
                       email VARCHAR(255),
                       password VARCHAR(255),
                       role VARCHAR(50),
                       field VARCHAR(50)
);

CREATE TABLE serviceEntity (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nom VARCHAR(255),
                         disponible BOOLEAN,
                         type VARCHAR(50)
);

CREATE TABLE ticket (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        titre VARCHAR(255),
                        description TEXT,
                        statut VARCHAR(50),
                        date_creation DATETIME,
                        assigned_to BIGINT,
                        prix DOUBLE
);

CREATE TABLE payment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         montant DOUBLE,
                         statut VARCHAR(50),
                         date_creation DATETIME
);

CREATE TABLE message (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         contenu TEXT,
                         date_envoi DATETIME
);