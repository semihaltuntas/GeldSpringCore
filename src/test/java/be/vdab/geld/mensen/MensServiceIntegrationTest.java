package be.vdab.geld.mensen;

import be.vdab.geld.exceptions.MensNietGevondenException;
import be.vdab.geld.exceptions.OnvoldoendeGeldException;
import be.vdab.geld.schenking.Schenking;
import be.vdab.geld.schenking.SchenkingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

@JdbcTest
@Import({MensService.class, MensRepository.class, SchenkingRepository.class})
@Sql("/mensen.sql")
public class MensServiceIntegrationTest {
    private static final String MENSEN_TABLE = "mensen";
    private static final String SCHENKINGEN_TABLE = "schenkingen";
    private final MensService mensService;
    private final JdbcClient jdbcClient;

    public MensServiceIntegrationTest(MensService mensService, JdbcClient jdbcClient) {
        this.mensService = mensService;
        this.jdbcClient = jdbcClient;
    }

    private long idVanTestMens1() {
        var sql = """
                select id from mensen where naam = 'test1'
                """;
        return jdbcClient.sql(sql)
                .query(Long.class)
                .single();
    }

    private long idVanTestMens2() {
        var sql = """
                select id from mensen where naam = 'test2'
                """;
        return jdbcClient.sql(sql)
                .query(Long.class)
                .single();
    }

    @Test
    void createVoegtEenSchenkingToeEnPastHetGeldVanDeMensenAan() {
        var vanMensenId = idVanTestMens1();
        var aanMensenId = idVanTestMens2();
        mensService.schenk(new Schenking(vanMensenId, aanMensenId, BigDecimal.ONE));
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, MENSEN_TABLE,
                "geld = 999 and id =" + vanMensenId)).isOne();
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, MENSEN_TABLE,
                "geld =2001 and id=" + aanMensenId)).isOne();
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, SCHENKINGEN_TABLE,
                "bedrag = 1 and vanMensId =" + vanMensenId +
                        " and aanMensId = " + aanMensenId)).isOne();
    }

    @Test
    void schenkingMetOnbestaandeVanMensMislukt() {
        assertThatExceptionOfType(MensNietGevondenException.class).isThrownBy(
                () -> mensService.schenk(
                        new Schenking(Long.MAX_VALUE, idVanTestMens2(), BigDecimal.ONE))
        );
    }

    @Test
    void schenkingMetOnbestaandAanMensMislukt() {
        assertThatExceptionOfType(MensNietGevondenException.class).isThrownBy(
                () -> mensService.schenk(
                        new Schenking(idVanTestMens1(), Long.MAX_VALUE, BigDecimal.ONE))
        );
    }

    @Test
    void schenkingMetOnvoldoendGeldMislukt() {
        var vanMensId = idVanTestMens1();
        var aanMensId = idVanTestMens2();
        assertThatExceptionOfType(OnvoldoendeGeldException.class).isThrownBy(
                () -> mensService.schenk(
                        new Schenking(vanMensId, aanMensId, BigDecimal.valueOf(1_001)))
        );
    }
}
