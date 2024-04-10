package be.vdab.geld.mensen;
import be.vdab.geld.mensen.MensRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class MensService {
    private final MensRepository mensRepository;

    public MensService(MensRepository mensRepository) {
        this.mensRepository = mensRepository;
    }
    public List<Mens> findAll() {
        return mensRepository.findAll();
    }
}