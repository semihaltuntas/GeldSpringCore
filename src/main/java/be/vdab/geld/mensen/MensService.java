package be.vdab.geld.mensen;

import be.vdab.geld.exceptions.MensNietGevondenException;
import be.vdab.geld.schenking.Schenking;
import be.vdab.geld.schenking.SchenkingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MensService {
    private final MensRepository mensRepository;
    private final SchenkingRepository schenkingRepository;

    public MensService(MensRepository mensRepository, SchenkingRepository schenkingRepository) {
        this.mensRepository = mensRepository;
        this.schenkingRepository = schenkingRepository;
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
       // Die voegt een record toe aan de table schenkingen:
        schenkingRepository.create(schenking);
    }
}