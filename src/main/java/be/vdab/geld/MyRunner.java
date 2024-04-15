package be.vdab.geld;

import be.vdab.geld.mensen.MensService;
import be.vdab.geld.schenking.Schenking;
import be.vdab.geld.exceptions.MensNietGevondenException;
import be.vdab.geld.exceptions.OnvoldoendeGeldException;
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

        Scanner scanner = new Scanner(System.in);
        System.out.println("Id van mens:");
        var vanMensId = scanner.nextInt();
        System.out.println("Id aan mens:");
        var aanMensId = scanner.nextInt();
        System.out.println("Het bedrag:");
        var bedrag = scanner.nextBigDecimal();
        try {
            var schenking = new Schenking(vanMensId, aanMensId, bedrag);
            mensService.schenk(schenking);
            System.out.println("Schenking gelukt");
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
        } catch (MensNietGevondenException ex) {
            System.err.println("Schenking mislukt. Mens ontbreekt. Id:" + ex.getId());
        } catch (OnvoldoendeGeldException ex) {
            System.err.println("Schenking mislukt. Onvoldoende geld.");
        }

        // 2.Code
//        var scanner = new Scanner(System.in);
//        System.out.println("Naam: ");
//        var naam = scanner.nextLine();
//        System.out.println("Geld: ");
//        var geld = scanner.nextBigDecimal();
//        var mens = new Mens(0,naam,geld);
//        var nieuwId = mensService.create(mens);
//        System.out.println("Id van deze mens: "+ nieuwId);

        // 1.Code
//        mensService.findAll().forEach(
//        mens -> System.out.println(mens.getNaam() + ":"+ mens.getGeld()));


    }
}
