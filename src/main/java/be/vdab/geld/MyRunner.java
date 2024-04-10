package be.vdab.geld;

import be.vdab.geld.mensen.Mens;
import be.vdab.geld.mensen.MensService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MyRunner implements CommandLineRunner {
    private final MensService mensService;

    public MyRunner(MensService mensService) {
        this.mensService = mensService;
    }

    @Override
    public void run(String... args) throws Exception {
        var scanner = new Scanner(System.in);
        System.out.println("Naam: ");
        var naam = scanner.nextLine();
        System.out.println("Geld: ");
        var geld = scanner.nextBigDecimal();
        var mens = new Mens(0,naam,geld);
        var nieuwId = mensService.create(mens);
        System.out.println("Id van deze mens: "+ nieuwId);

//        mensService.findAll().forEach(
//                mens -> System.out.println(mens.getNaam() + ":"+ mens.getGeld()));
    }
}
