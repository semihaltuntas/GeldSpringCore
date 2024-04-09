package be.vdab.geld.mensen;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class MensRepository {
    private final JdbcClient jdbcClient;

    public MensRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public long findAantal() {
        String sql = """
                select count(*) as aantalMensen
                from mensen
                """;
        return jdbcClient.sql(sql)
                .query(Long.class)
                .single();
    }

    public void delete(long id) {
        String sql = """
                delete from mensen
                where id = ?
                """;
        jdbcClient.sql(sql)
                .param(id)
                .update();
    }

    public void update(Mens mens) {
        var sql = """
                update mensen
                set naam = ?, geld = ?
                where id = ?
                """;
        if (jdbcClient.sql(sql)
                .params(mens.getNaam(), mens.getGeld(), mens.getId())
                .update() == 0) {
            throw new MensNietGevondenException(mens.getId());
        }
    }

    public long create(Mens mens) {

//        var sql = """
//                insert into mensen (naam,geld)
//                values (?, ?)
//                """;
//        jdbcClient.sql(sql)
//                .params(mens.getNaam(), mens.getGeld())
//                .update();

        var sql = """
                insert into mensen(naam, geld)
                values (?, ?)
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .params(mens.getNaam(), mens.getGeld())
                .update(keyHolder);
        return keyHolder.getKey().longValue();
    }

    public List<Mens> findAll() {
        var sql = """
                select id,naam,geld
                from mensen
                order by id
                """;
        return jdbcClient.sql(sql)
                .query(Mens.class)
                .list();
    }

    public List<Mens> findByGeldBetween(BigDecimal van, BigDecimal tot) {
        var sql = """
                select id,naam,geld
                from mensen
                where geld between ? and ?
                order by geld
                """;
        return jdbcClient.sql(sql)
                .params(van, tot)
                .query(Mens.class)
                .list();
    }

    public Optional<Mens> findByID(long id) {
        var sql = """
                select id,naam,geld
                from mensen
                where id = ?
                """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(Mens.class)
                .optional();
    }

    public Optional<Mens> findAndLockByID(long id) {
        var sql = """
                select id,naam,geld
                from mensen
                where id = ?
                for update
                """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(Mens.class)
                .optional();
    }
    public long idVanTestMens1() {
        return jdbcClient.sql(
                "select id from mensen where naam = 'test1'")
                .query(Long.class)
                .single();
    }
}
