package be.vdab.geld.mensen;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class SchenkingTest {

    @Test
    void eenSchenkingAanvaardtCorrecteParameters() {
        new Schenking(1, 2, BigDecimal.TEN);
    }

    @Test
    void vanMensIdMoetPositiefZijn() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Schenking(0, 3, BigDecimal.ONE));
    }

    @Test
    void aanMensIdMoetPositiefZijn() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Schenking(1, 0, BigDecimal.ONE));
    }

    @Test
    void eenSchenkingAanJezelfMislukt() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Schenking(2, 2, BigDecimal.ONE));
    }

    @Test
    void hetBedragMoetPositiefZijn() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Schenking(1, 2, BigDecimal.valueOf(-10)));
    }

    @Test
    void eenSchenkingVan0€Mislukt() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Schenking(1, 2, BigDecimal.ZERO));
    }
}
