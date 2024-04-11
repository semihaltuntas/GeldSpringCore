package be.vdab.geld.mensen;

import be.vdab.geld.mensen.exceptions.OnvoldoendeGeldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class MensTast {
    private Mens jan, mie;

    @BeforeEach
    void beforeEach() {
        jan = new Mens(1, "jan", BigDecimal.TEN);
        mie = new Mens(1, "mie", BigDecimal.ONE);
    }

    @Test
    void schenkWijzigtHetGeldVanDeBetrokkenMensen() {
        jan.schenk(mie, BigDecimal.ONE);
        assertThat(jan.getGeld()).isEqualByComparingTo("9");
        assertThat(mie.getGeld()).isEqualByComparingTo("2");
    }

    @Test
    void schenkMisluktBigOnvoldoendeGeld() {
        assertThatExceptionOfType(OnvoldoendeGeldException.class).isThrownBy(
                () -> jan.schenk(mie, BigDecimal.valueOf(12)));
    }

    @Test
    void schenkMisluktAlsAanMensLeegIs() {
        assertThatNullPointerException().isThrownBy(
                () -> jan.schenk(null, BigDecimal.ONE));
    }

    @Test
    void schenkMisluktAlsBedragLeegIs() {
        assertThatNullPointerException().isThrownBy(
                () -> jan.schenk(mie, null));
    }
}
