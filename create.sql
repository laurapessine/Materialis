drop database if exists Materialis;

create database Materialis;

use Materialis;

create table Estudante(id bigint not null auto_increment, 
                        nome varchar(256) not null,
                        email varchar(200) not null,
                        senha varchar(15) not null,
                        CPF int(11) not null,
                        RA int(6) not null,
                        telefone int(11) not null,
                        sexo ENUM('Feminino', 'Masculino', 'Outro'),
                        nascimento int(8) not null);

create table Editora(id bigint not null auto_increment, cnpj varchar(18) not null, nome varchar(256) not null, primary key (id));

create table Livro(id bigint not null auto_increment, titulo varchar(256) not null, autor varchar(256) not null, ano integer not null, preco float not null, editora_id bigint not null, primary key (id), foreign key (editora_id) references Editora(id));

insert into Editora(cnpj, nome) values  ('55.789.390/0008-99', 'Companhia das Letras');

insert into Livro(titulo, autor, ano, preco, editora_id) values ('Ensaio sobre a Cegueira', 'José Saramago', 1995, 54.9, 1);