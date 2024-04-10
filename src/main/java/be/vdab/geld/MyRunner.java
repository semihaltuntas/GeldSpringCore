package be.vdab.geld;

import be.vdab.geld.mensen.MensService;
import org.springframework.boot.CommandLineRunner;

public class MyRunner implements CommandLineRunner {
    private final MensService mensService;

    public MyRunner(MensService mensService) {
        this.mensService = mensService;
    }

    @Override
    public void run(String... args) throws Exception {
        mensService.findAll().forEach(
                mens -> System.out.println(mens.getNaam()));
    }
}
