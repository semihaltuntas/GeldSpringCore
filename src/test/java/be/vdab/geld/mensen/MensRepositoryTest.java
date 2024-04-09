package be.vdab.geld.mensen;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(MensRepository.class)
@Sql("/mensen.sql")
public class MensRepositoryTest {
    private final MensRepository mensRepository;
    private final JdbcClient jdbcClient;
    private static final String MENSEN_TABLE = "mensen";

    public MensRepositoryTest(MensRepository mensRepository, JdbcClient jdbcClient) {
        this.mensRepository = mensRepository;
        this.jdbcClient = jdbcClient;
    }

    @Test
    void findAantalGeeftHetJuisteAantalMensen() {
        var aantalRecords = JdbcTestUtils.countRowsInTable(jdbcClient, MENSEN_TABLE);
        assertThat(mensRepository.findAantal()).isEqualTo(aantalRecords);
    }

    @Test
    void findAllGeeftAlleMensenGesorteerdOpId() {
        var aantalRecords = JdbcTestUtils.countRowsInTable(jdbcClient, MENSEN_TABLE);
        assertThat(mensRepository.findAll())
                .hasSize(aantalRecords)
                .extracting(Mens::getId) // Bu method insanların id lerini ayıklar listeden.
                .isSorted();
    }

    @Test
    void createVoegtEenMensToe() {
        var id = mensRepository.create(new Mens(0, "tekst3", BigDecimal.TEN));
        assertThat(id).isPositive();
        var aantalRecordsMetDeIdVanDeToegevoegdeMens =
                JdbcTestUtils.countRowsInTableWhere(jdbcClient, MENSEN_TABLE, "id=" + id);
        assertThat(aantalRecordsMetDeIdVanDeToegevoegdeMens).isOne();
    }

    @Test
    void deleteVervijdertEenMens() {
        var id = mensRepository.idVanTestMens1();
        mensRepository.delete(id);
        var aantalRecordsMetDeIdVanDeVerwijderedeMens =
                JdbcTestUtils.countRowsInTableWhere(jdbcClient, MENSEN_TABLE, "id=" + id);
        assertThat(aantalRecordsMetDeIdVanDeVerwijderedeMens).isZero();
    }
}
