package ma.ouassekssou;

import ma.ouassekssou.entities.Product;
import ma.ouassekssou.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Tp3WebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tp3WebappApplication.class, args);
    }

    // Étape 4 : tester la couche DAO en insérant des données de démonstration
    @Bean
    CommandLineRunner initData(ProductRepository repo) {
        return args -> {
            repo.save(new Product(null, "Ordinateur Dell", 12500.0, 10));
            repo.save(new Product(null, "Imprimante HP", 3200.0, 5));
            repo.save(new Product(null, "Clavier Mécanique", 450.0, 30));
            repo.save(new Product(null, "Souris Logitech", 180.0, 50));
            repo.save(new Product(null, "Ecran Samsung 24\"", 4200.0, 15));
            repo.save(new Product(null, "Webcam HD", 650.0, 20));
            repo.save(new Product(null, "Casque Audio", 890.0, 12));
            repo.save(new Product(null, "Disque SSD 1To", 1200.0, 25));

            System.out.println("\n========== TEST COUCHE DAO ==========");
            System.out.println("Nombre de produits : " + repo.count());
            repo.findAll().forEach(System.out::println);
        };
    }
}
