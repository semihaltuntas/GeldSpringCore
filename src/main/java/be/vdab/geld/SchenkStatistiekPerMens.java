package be.vdab.geld;

import java.math.BigDecimal;

public record SchenkStatistiekPerMens(long id, String naam, int aantal, BigDecimal totaal) {
}
