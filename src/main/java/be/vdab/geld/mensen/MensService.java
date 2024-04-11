package be.vdab.geld.mensen;

import be.vdab.geld.mensen.MensRepository;
import be.vdab.geld.mensen.exceptions.MensNietGevondenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MensService {
    private final MensRepository mensRepository;

    public MensService(MensRepository mensRepository) {
        this.mensRepository = mensRepository;
    }

    public List<Mens> findAll() {
        return mensRepository.findAll();
    }

    @Transactional
    public long create(Mens mens) {
        return mensRepository.create(mens);
    }

    @Transactional
    public void schenk(Schenking schenking) {
        var vanMensId = schenking.getVanMensId();
        var vanMens = mensRepository.findAndLockByID(vanMensId)
                .orElseThrow(() -> new MensNietGevondenException(vanMensId));
        var aanMensId = schenking.getAanMensId();
        var aanMens = mensRepository.findAndLockByID(aanMensId)
                .orElseThrow(() -> new MensNietGevondenException(aanMensId));
        vanMens.schenk(aanMens, schenking.getBedrag());
        mensRepository.update(vanMens);
        mensRepository.update(aanMens);
    }
}