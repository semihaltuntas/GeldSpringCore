package be.vdab.geld.schenking;

import be.vdab.geld.schenking.Schenking;
import be.vdab.geld.schenking.SchenkingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(SchenkingRepository.class)
@Sql("/mensen.sql")
public class SchenkingRepositoryTest {
    private final SchenkingRepository schenkingRepository;
    private final JdbcClient jdbcClient;
    private static final String SCHENKINGEN_TABLE = "schenkingen";

    public SchenkingRepositoryTest(SchenkingRepository schenkingRepository, JdbcClient jdbcClient) {
        this.schenkingRepository = schenkingRepository;
        this.jdbcClient = jdbcClient;
    }

    private long idVanTestMens1() {
        return jdbcClient.sql("select id from mensen where naam = 'test1'")
                .query(Long.class)
                .single();
    }

    private long idVanTestMens2() {
        return jdbcClient.sql("select id from mensen where naam = 'test2'")
                .query(Long.class)
                .single();
    }

    @Test
    void createVoegtEenSchenkToe() {
        var vanMensId = idVanTestMens1();
        var aanMensId = idVanTestMens2();
        schenkingRepository.create(new Schenking(vanMensId, aanMensId, BigDecimal.ONE));
        var aantalRecords = JdbcTestUtils.countRowsInTableWhere(jdbcClient, SCHENKINGEN_TABLE,
                "bedrag = 1 and vanMensId = " + vanMensId + " and aanMensId = " + aanMensId);
        assertThat(aantalRecords).isOne();
    }
}
