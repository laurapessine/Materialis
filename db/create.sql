CREATE DATABASE Materialis;

DROP TABLE IF EXISTS Material;
CREATE TABLE Material (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    categoria ENUM('papelaria', 'livros', 'eletrônicos') NOT NULL,
    estado_conservacao ENUM('novo', 'excelente', 'bom', 'regular', 'ruim') NOT NULL,
    fotos JSON NULL,
    local_retirada VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);