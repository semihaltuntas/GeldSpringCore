package be.vdab.geld.mensen;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Schenking {
    private final long vanMensId;
    private final long aanMensId;
    private final BigDecimal bedrag;
    private final long id;
    private final LocalDateTime wanneer;


    public Schenking(long vanMensId, long aanMensId, BigDecimal bedrag) {
        if (vanMensId <= 0) {
            throw new IllegalArgumentException("Id van mens moet positief zijn");
        }
        if (aanMensId <= 0) {
            throw new IllegalArgumentException("Id aan mens moet positief zijn");
        }
        if (aanMensId == vanMensId) {
            throw new IllegalArgumentException("Je kan niet aan jezelf schenken");
        }
        if (bedrag.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Het bedrag moet groter dan 0 zijn");
        }
        this.vanMensId = vanMensId;
        this.aanMensId = aanMensId;
        this.bedrag = bedrag;
        this.id = 0;
        this.wanneer=LocalDateTime.now();
    }




    public long getVanMensId() {
        return vanMensId;
    }

    public long getAanMensId() {
        return aanMensId;
    }

    public BigDecimal getBedrag() {
        return bedrag;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getWanneer() {
        return wanneer;
    }
}
