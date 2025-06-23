package br.ufscar.dc.dsw;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import br.ufscar.dc.dsw.dao.IEstudanteDAO;
import br.ufscar.dc.dsw.domain.Estudante;

@SpringBootApplication
public class MaterialisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaterialisApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(IEstudanteDAO estudanteDAO, PasswordEncoder passwordEncoder) {
        return (args) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
        };
    }
}
