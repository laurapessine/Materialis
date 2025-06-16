DROP DATABASE IF EXISTS Materialis;

CREATE DATABASE Materialis;

USE Materialis;

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

DROP TABLE IF EXISTS Estudante;
CREATE TABLE Estudante(
    id INT NOT NULL auto_increment PRIMARY KEY, 
    nome VARCHAR(256) NOT NULL,
    email VARCHAR(200) NOT NULL,
    senha VARCHAR(15) NOT NULL,
    CPF INT(11) NOT NULL,
    RA INT(6) NOT NULL,
    telefone INT(11) NOT NULL,
    sexo ENUM('Feminino', 'Masculino', 'Outro') NOT NULL,
    nascimento INT(8) NOT NULL);

INSERT INTO Estudante(nome, email, senha, CPF, RA, telefone, sexo, nascimento) VALUES ('admin','admin@gmail.com','admin', 12345678910, 82000, 16912345678, 'Feminino', 11032000);