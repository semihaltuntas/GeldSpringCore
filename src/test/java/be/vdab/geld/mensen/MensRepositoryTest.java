package be.vdab.geld.mensen;

import be.vdab.geld.mensen.exceptions.MensNietGevondenException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

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

    @Test
    void findByIdMetBestaandeIdVindtEenMens() {
        assertThat(mensRepository.findByID(mensRepository.idVanTestMens1()))
                .hasValueSatisfying(
                        mens -> assertThat(mens.getNaam()).isEqualTo("test1"));
    }

    @Test
    void findByIdMetOnbestaandeIdVindtGeenMens() {
        assertThat(mensRepository.findByID(Long.MAX_VALUE)).isEmpty();
    }

    @Test
    void findAndLockByIdMetBestaandeIdVindtEenMens() {
        assertThat(mensRepository.findAndLockByID(mensRepository.idVanTestMens1()))
                .hasValueSatisfying(
                        mens -> assertThat(mens.getNaam()).isEqualTo("test1"));
    }

    @Test
    void findAndLockByIdMetOnbestaandeIdVindtGeenMens() {
        assertThat(mensRepository.findAndLockByID(Long.MAX_VALUE)).isEmpty();
    }

    @Test
    void updateWijzigtEenMens() {
        var id = mensRepository.idVanTestMens1();
        var mens = new Mens(id, "mens1", BigDecimal.TEN);
        mensRepository.update(mens);
        var aantalAangepasteRecords = JdbcTestUtils.countRowsInTableWhere(
                jdbcClient, MENSEN_TABLE, "geld = 10 and id =" + id);
        assertThat(aantalAangepasteRecords).isOne();
    }

    @Test
    void updateOnbestaandeMensMislukt() {
        assertThatExceptionOfType(MensNietGevondenException.class).isThrownBy(
                () -> mensRepository.update(
                        new Mens(Long.MAX_VALUE, "test3", BigDecimal.TEN)));
    }

    @Test
    void findByGeldBetweenVindtDeJuisteMensen() {
        var van = BigDecimal.ONE;
        var tot = BigDecimal.TEN;
        var aantalRecords = JdbcTestUtils.countRowsInTableWhere(
                jdbcClient, MENSEN_TABLE, "geld between 1 and 10");
        assertThat(mensRepository.findByGeldBetween(van, tot))
                .hasSize(aantalRecords)
                .extracting(Mens::getGeld)
                .allSatisfy(geld -> assertThat(geld).isBetween(van, tot))
                .isSorted();
    }
}
