package br.ufscar.dc.dsw;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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
    public CommandLineRunner demo(IEstudanteDAO estudanteDAO, IMaterialService materialService,
            IEmprestimoService emprestimoService, PasswordEncoder passwordEncoder) {
        return (args) -> {
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
                estudanteDAO.save(e2);
            }
            Estudante lorena = estudanteDAO.findByEmail("lorena@gmail.com");
            Estudante luis = estudanteDAO.findByEmail("luis@gmail.com");
            if (lorena != null) {
                List<Material> materiaisLorena = materialService.buscarPorDono(lorena);
                if (materiaisLorena.stream().noneMatch(m -> m.getTitulo().equals("Kit de Papelaria Completo"))) {
                    Material m1 = new Material();
                    m1.setTitulo("Kit de Papelaria Completo");
                    m1.setDescricao("Estojo contendo canetas, lápis, borracha, régua e apontador.");
                    m1.setCategoria(Categoria.PAPELARIA);
                    m1.setEstadoConservacao(EstadoConservacao.NOVO);
                    File file1 = new File("kit_papelaria.jpg");
                    if(file1.exists()){
                        m1.setNomeImagem(file1.getName());
                        m1.setTipoImagem(Files.probeContentType(file1.toPath()));
                        m1.setImagem(Files.readAllBytes(file1.toPath()));
                    }
                    m1.setLocalRetirada("Biblioteca Central, Balcão 3");
                    m1.setEstudante(lorena);
                    materialService.salvar(m1);
                }
                if (materiaisLorena.stream().noneMatch(m -> m.getTitulo().equals("Livro: Estruturas de Dados em Java"))) {
                    Material m2 = new Material();
                    m2.setTitulo("Livro: Estruturas de Dados em Java");
                    m2.setDescricao("Livro texto usado, 2ª edição. Cobre listas, pilhas, filas, etc.");
                    m2.setCategoria(Categoria.LIVROS);
                    m2.setEstadoConservacao(EstadoConservacao.REGULAR);
                    File file2 = new File("livro_java.jpg");
                     if(file2.exists()){
                        m2.setNomeImagem(file2.getName());
                        m2.setTipoImagem(Files.probeContentType(file2.toPath()));
                        m2.setImagem(Files.readAllBytes(file2.toPath()));
                    }
                    m2.setLocalRetirada("Prédio de Computação");
                    m2.setEstudante(lorena);
                    materialService.salvar(m2);
                }
            }
            if (luis != null) {
                List<Material> materiaisLuis = materialService.buscarPorDono(luis);
                if (materiaisLuis.stream().noneMatch(m -> m.getTitulo().equals("Kit Arduino Uno com Protoboard"))) {
                    Material m4 = new Material();
                    m4.setTitulo("Kit Arduino Uno com Protoboard");
                    m4.setDescricao("Inclui placa Arduino Uno, cabos, sensores básicos e protoboard.");
                    m4.setCategoria(Categoria.ELETRONICOS);
                    m4.setEstadoConservacao(EstadoConservacao.BOM);
                    File file4 = new File("kit_arduino.jpg");
                     if(file4.exists()){
                        m4.setNomeImagem(file4.getName());
                        m4.setTipoImagem(Files.probeContentType(file4.toPath()));
                        m4.setImagem(Files.readAllBytes(file4.toPath()));
                    }
                    m4.setLocalRetirada("Laboratório de Eletrônica, Sala 12");
                    m4.setEstudante(luis);
                    materialService.salvar(m4);
                }
            }
            Material materialDeLuis = materialService.buscarPorDono(luis).stream()
                .filter(m -> m.getTitulo().equals("Kit Arduino Uno com Protoboard"))
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