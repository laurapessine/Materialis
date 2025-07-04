package br.ufscar.dc.dsw;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
            try {
                if (materialService.buscarPorDono(lorena).stream().noneMatch(m -> m.getTitulo().equals("Kit de Papelaria Completo"))) {
                    Material m1 = new Material();
                    m1.setTitulo("Kit de Papelaria Completo");
                    m1.setDescricao("Estojo completo para aulas.");
                    m1.setCategoria(Categoria.PAPELARIA);
                    m1.setEstadoConservacao(EstadoConservacao.NOVO);
                    m1.setLocalRetirada("Biblioteca Central");
                    m1.setEstudante(lorena);                    
                    File file1 = new ClassPathResource("static/images/kit_papelaria.jpeg").getFile();
                    m1.setNomeImagem(file1.getName());
                    m1.setTipoImagem(Files.probeContentType(file1.toPath()));
                    m1.setImagem(Files.readAllBytes(file1.toPath()));
                    materialService.salvar(m1);
                }
                 if (materialService.buscarPorDono(luis).stream().noneMatch(m -> m.getTitulo().equals("Kit Arduino Uno"))) {
                    Material m4 = new Material();
                    m4.setTitulo("Kit Arduino Uno");
                    m4.setDescricao("Ideal para projetos de eletrônica.");
                    m4.setCategoria(Categoria.ELETRONICOS);
                    m4.setEstadoConservacao(EstadoConservacao.BOM);
                    m4.setLocalRetirada("Laboratório de Eletrônica");
                    m4.setEstudante(luis);
                    File file4 = new ClassPathResource("static/images/kit_arduino.jpg").getFile();
                    m4.setNomeImagem(file4.getName());
                    m4.setTipoImagem(Files.probeContentType(file4.toPath()));
                    m4.setImagem(Files.readAllBytes(file4.toPath()));
                    materialService.salvar(m4);
                }
            } catch (IOException e) {
                System.err.println("Erro ao carregar imagens de exemplo. Verifique se os arquivos estão em src/main/resources/static/images/");
                System.err.println(e.getMessage());
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