package br.ufscar.dc.dsw;

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
            Estudante e1 = new Estudante();
            e1.setCpf("123.141.928-23");
            e1.setNome("Lorena");
            e1.setEmail("lorena@gmail.com");
            e1.setNascimento("01/02/2000");
            e1.setRa("821239");
            e1.setSenha(passwordEncoder.encode("123abc")); 
            e1.setSexo("Feminino");
            e1.setTelefone("(16)1792-3824");
            estudanteDAO.save(e1);

            Estudante e2 = new Estudante();
            e2.setCpf("182.283.192-01");
            e2.setNome("Luis");
            e2.setEmail("luis@gmail.com");
            e2.setNascimento("05/10/1999");
            e2.setRa("123456");
            e2.setSenha(passwordEncoder.encode("password"));
            e2.setSexo("Masculino");
            e2.setTelefone("(11)98765-4321");
            estudanteDAO.save(e2);
        };
    }

}
