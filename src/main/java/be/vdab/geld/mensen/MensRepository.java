package be.vdab.geld.mensen;

import org.springframework.jdbc.core.simple.JdbcClient;

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
}
