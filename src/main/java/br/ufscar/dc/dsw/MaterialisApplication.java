package br.ufscar.dc.dsw;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import br.ufscar.dc.dsw.dao.IEstudanteDAO;
import br.ufscar.dc.dsw.domain.Emprestimo;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.domain.Material.Categoria;
import br.ufscar.dc.dsw.domain.Material.EstadoConservacao;
import br.ufscar.dc.dsw.service.spec.IEmprestimoService;
import br.ufscar.dc.dsw.service.spec.IMaterialService;

@SpringBootApplication
public class MaterialisApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaterialisApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(IEstudanteDAO estudanteDAO, IMaterialService materialService, IEmprestimoService emprestimoService, PasswordEncoder passwordEncoder) {
        return (args) -> {
            
            // 0. CRIAÇÃO DO ADMIN
            if (estudanteDAO.findByEmail("admin@materialis.com") == null) {
            Estudante admin = new Estudante();
            admin.setEmail("admin@materialis.com");
            admin.setSenha(passwordEncoder.encode("admin")); 
            admin.setNome("Administrador");
            admin.setCpf("00000000000"); 
            admin.setTelefone("00000000000");
            admin.setSexo(Estudante.Sexo.Outro);
            admin.setNascimento(LocalDate.of(1990, 1, 1)); 
            admin.setRa("000000");
            admin.setRole("ROLE_ADMIN");
            estudanteDAO.save(admin);
            System.out.println("Usuário 'admin' criado com sucesso!");
        }

            // 1. CRIAÇÃO DOS ESTUDANTES
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if (estudanteDAO.findByEmail("lorena@gmail.com") == null) {
                Estudante e1 = new Estudante();
                e1.setCpf("12314192823");
                e1.setNome("Lorena");
                e1.setEmail("lorena@gmail.com");
                e1.setNascimento(LocalDate.parse("01/02/2000", formatter));
                e1.setRa("821239");
                e1.setSenha(passwordEncoder.encode("123abc"));
                e1.setSexo(Estudante.Sexo.Feminino);
                e1.setTelefone("16179238224");
                e1.setRole("ROLE_USER");
                estudanteDAO.save(e1);
            }
            if (estudanteDAO.findByEmail("luis@gmail.com") == null) {
                Estudante e2 = new Estudante();
                e2.setCpf("18228319201");
                e2.setNome("Luis");
                e2.setEmail("luis@gmail.com");
                e2.setNascimento(LocalDate.parse("05/10/1999", formatter));
                e2.setRa("123456");
                e2.setSenha(passwordEncoder.encode("password"));
                e2.setSexo(Estudante.Sexo.Masculino);
                e2.setTelefone("11987654321");
                e2.setRole("ROLE_USER");
                estudanteDAO.save(e2);
            }

            Estudante lorena = estudanteDAO.findByEmail("lorena@gmail.com");
            Estudante luis = estudanteDAO.findByEmail("luis@gmail.com");

            // 2. CRIAÇÃO DOS MATERIAIS
            try {
                // Materiais da Lorena
                if (lorena != null) {
                    // Kit de Papelaria
                    if (materialService.buscarPorDono(lorena).stream().noneMatch(m -> m.getTitulo().equals("Kit de Papelaria Completo"))) {
                        Material m = new Material();
                        m.setTitulo("Kit de Papelaria Completo");
                        m.setDescricao("Estojo completo para aulas.");
                        m.setCategoria(Categoria.PAPELARIA);
                        m.setEstadoConservacao(EstadoConservacao.NOVO);
                        m.setLocalRetirada("Biblioteca Central");
                        m.setEstudante(lorena);
                        File file = new ClassPathResource("static/images/kit_papelaria.jpeg").getFile();
                        m.setNomeImagem(file.getName());
                        m.setTipoImagem(Files.probeContentType(file.toPath()));
                        m.setImagem(Files.readAllBytes(file.toPath()));
                        materialService.salvar(m);
                    }
                    // Livro de Java
                    if (materialService.buscarPorDono(lorena).stream().noneMatch(m -> m.getTitulo().equals("Livro: Estruturas de Dados em Java"))) {
                        Material m = new Material();
                        m.setTitulo("Livro: Estruturas de Dados em Java");
                        m.setDescricao("Livro texto usado, 2ª edição. Cobre listas, pilhas, filas, árvores e grafos.");
                        m.setCategoria(Categoria.LIVROS);
                        m.setEstadoConservacao(EstadoConservacao.REGULAR);
                        m.setLocalRetirada("Sala dos Projetos de IC, Prédio de Computação");
                        m.setEstudante(lorena);
                        File file = new ClassPathResource("static/images/livro_java.jpg").getFile();
                        m.setNomeImagem(file.getName());
                        m.setTipoImagem(Files.probeContentType(file.toPath()));
                        m.setImagem(Files.readAllBytes(file.toPath()));
                        materialService.salvar(m);
                    }
                    // Calculadora
                     if (materialService.buscarPorDono(lorena).stream().noneMatch(m -> m.getTitulo().equals("Calculadora Científica HP 50g"))) {
                        Material m = new Material();
                        m.setTitulo("Calculadora Científica HP 50g");
                        m.setDescricao("Calculadora científica avançada, com funções de álgebra computacional.");
                        m.setCategoria(Categoria.ELETRONICOS);
                        m.setEstadoConservacao(EstadoConservacao.EXCELENTE);
                        m.setLocalRetirada("Laboratório de Matemática, Prédio 5");
                        m.setEstudante(lorena);
                        File file = new ClassPathResource("static/images/calculadora.jpg").getFile();
                        m.setNomeImagem(file.getName());
                        m.setTipoImagem(Files.probeContentType(file.toPath()));
                        m.setImagem(Files.readAllBytes(file.toPath()));
                        materialService.salvar(m);
                    }
                }

                // Materiais do Luis
                if (luis != null) {
                    // Kit Arduino
                    if (materialService.buscarPorDono(luis).stream().noneMatch(m -> m.getTitulo().equals("Kit Arduino Uno"))) {
                        Material m = new Material();
                        m.setTitulo("Kit Arduino Uno");
                        m.setDescricao("Ideal para projetos de eletrônica.");
                        m.setCategoria(Categoria.ELETRONICOS);
                        m.setEstadoConservacao(EstadoConservacao.BOM);
                        m.setLocalRetirada("Laboratório de Eletrônica");
                        m.setEstudante(luis);
                        File file = new ClassPathResource("static/images/kit_arduino.jpg").getFile();
                        m.setNomeImagem(file.getName());
                        m.setTipoImagem(Files.probeContentType(file.toPath()));
                        m.setImagem(Files.readAllBytes(file.toPath()));
                        materialService.salvar(m);
                    }
                    // Livro de Banco de Dados
                    if (materialService.buscarPorDono(luis).stream().noneMatch(m -> m.getTitulo().equals("Livro: Banco de Dados Relacionais"))) {
                        Material m = new Material();
                        m.setTitulo("Livro: Banco de Dados Relacionais");
                        m.setDescricao("Livro em bom estado, aborda modelo relacional e SQL avançado.");
                        m.setCategoria(Categoria.LIVROS);
                        m.setEstadoConservacao(EstadoConservacao.BOM);
                        m.setLocalRetirada("Sala de Aula de Banco de Dados, Bloco de TI");
                        m.setEstudante(luis);
                        File file = new ClassPathResource("static/images/livro_bd.jpg").getFile();
                        m.setNomeImagem(file.getName());
                        m.setTipoImagem(Files.probeContentType(file.toPath()));
                        m.setImagem(Files.readAllBytes(file.toPath()));
                        materialService.salvar(m);
                    }
                }

            } catch (IOException e) {
                System.err.println("Erro ao carregar imagens de exemplo: " + e.getMessage());
            }

            // 3. CRIAÇÃO DE EMPRÉSTIMO DE TESTE
            Material materialDeLuis = materialService.buscarPorDono(luis).stream()
                .filter(m -> m.getTitulo().equals("Kit Arduino Uno"))
                .findFirst().orElse(null);

            if (lorena != null && materialDeLuis != null) {
                boolean existeSolicitacao = emprestimoService
                        .buscarPorEstudante(lorena)
                        .stream()
                        .anyMatch(e -> e.getMaterial().getId().equals(materialDeLuis.getId()));

                if (!existeSolicitacao) {
                    Emprestimo emprestimo = new Emprestimo();
                    emprestimo.setEstudante(lorena);
                    emprestimo.setMaterial(materialDeLuis);
                    emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));
                    emprestimo.setJustificativa("Uso acadêmico para projeto de Robótica.");
                    emprestimoService.salvar(emprestimo);
                }
            }
        };
    }
}