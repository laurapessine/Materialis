package br.ufscar.dc.dsw;

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
import br.ufscar.dc.dsw.domain.Emprestimo.Status;
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
            if (!estudanteDAO.existsByCpf("12314192823")) {
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
            if (!estudanteDAO.existsByCpf("18228319201")) {
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
            Estudante lorena = estudanteDAO.findByCpf("12314192823");
            Estudante luis = estudanteDAO.findByCpf("18228319201");
            if (lorena != null) {
                List<Material> materiaisLorena = materialService.buscarPorDono(lorena);
                if (materiaisLorena.stream().noneMatch(m -> m.getTitulo().equals("Kit de Papelaria Completo"))) {
                    Material m1 = new Material();
                    m1.setTitulo("Kit de Papelaria Completo");
                    m1.setDescricao(
                            "Estojo contendo canetas coloridas, lápis, borracha, régua e apontador. Ideal para anotações em aula.");
                    m1.setCategoria(Categoria.PAPELARIA);
                    m1.setEstadoConservacao(EstadoConservacao.NOVO);
                    m1.setFotos(null);
                    m1.setLocalRetirada("Biblioteca Central, Balcão 3");
                    m1.setEstudante(lorena);
                    materialService.salvar(m1);
                }
                if (materiaisLorena.stream()
                        .noneMatch(m -> m.getTitulo().equals("Livro: Estruturas de Dados em Java"))) {
                    Material m2 = new Material();
                    m2.setTitulo("Livro: Estruturas de Dados em Java");
                    m2.setDescricao(
                            "Livro texto usado, 2ª edição. Cobre listas, pilhas, filas, árvores e grafos com exemplos em Java.");
                    m2.setCategoria(Categoria.LIVROS);
                    m2.setEstadoConservacao(EstadoConservacao.REGULAR);
                    m2.setFotos(null);
                    m2.setLocalRetirada("Sala dos Projetos de IC, Prédio de Computação");
                    m2.setEstudante(lorena);
                    materialService.salvar(m2);
                }
                if (materiaisLorena.stream().noneMatch(m -> m.getTitulo().equals("Calculadora Científica HP 50g"))) {
                    Material m3 = new Material();
                    m3.setTitulo("Calculadora Científica HP 50g");
                    m3.setDescricao(
                            "Calculadora científica avançada, com funções de álgebra computacional. Bateria em bom estado.");
                    m3.setCategoria(Categoria.ELETRONICOS);
                    m3.setEstadoConservacao(EstadoConservacao.EXCELENTE);
                    m3.setFotos(null);
                    m3.setLocalRetirada("Laboratório de Matemática, Prédio 5");
                    m3.setEstudante(lorena);
                    materialService.salvar(m3);
                }
            }
            if (luis != null) {
                List<Material> materiaisLuis = materialService.buscarPorDono(luis);
                if (materiaisLuis.stream().noneMatch(m -> m.getTitulo().equals("Kit Arduino Uno com Protoboard"))) {
                    Material m4 = new Material();
                    m4.setTitulo("Kit Arduino Uno com Protoboard");
                    m4.setDescricao(
                            "Inclui placa Arduino Uno, cabos, sensores básicos e protoboard. Perfeito para projetos de eletrônica.");
                    m4.setCategoria(Categoria.ELETRONICOS);
                    m4.setEstadoConservacao(EstadoConservacao.RUIM);
                    m4.setFotos(null);
                    m4.setLocalRetirada("Laboratório de Eletrônica, Sala 12");
                    m4.setEstudante(luis);
                    materialService.salvar(m4);
                }
                if (materiaisLuis.stream().noneMatch(m -> m.getTitulo().equals("Livro: Banco de Dados Relacionais"))) {
                    Material m5 = new Material();
                    m5.setTitulo("Livro: Banco de Dados Relacionais");
                    m5.setDescricao("Livro em bom estado, aborda modelo relacional, SQL avançado e exemplos práticos.");
                    m5.setCategoria(Categoria.LIVROS);
                    m5.setEstadoConservacao(EstadoConservacao.BOM);
                    m5.setFotos(null);
                    m5.setLocalRetirada("Sala de Aula de Banco de Dados, Bloco de TI");
                    m5.setEstudante(luis);
                    materialService.salvar(m5);
                }
            }
            Material material = materialService.buscarPorId((long) 5);
            if (lorena != null && material != null) {
                boolean existeSolicitacao = emprestimoService
                        .buscarPorEstudante(lorena)
                        .stream()
                        .anyMatch(e -> e.getMaterial().getId().equals(material.getId()));
                if (!existeSolicitacao) {
                    Emprestimo emprestimo = new Emprestimo();
                    emprestimo.setEstudante(lorena);
                    emprestimo.setMaterial(material);
                    emprestimo.setDataSolicitacao(LocalDate.now());
                    emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));
                    emprestimo.setJustificativa("Uso acadêmico para anotações.");
                    emprestimo.setStatus(Status.ABERTO);
                    emprestimoService.salvar(emprestimo);
                }
            }
        };
    }
}