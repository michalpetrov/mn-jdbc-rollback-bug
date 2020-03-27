package mn.jdbc.rollback.bug;

import io.micronaut.context.annotation.Context;
import io.micronaut.runtime.Micronaut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.Objects;

@Singleton
@Context
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    public static String NEW = "NEW";
    public static String FAIL = "FAIL";

    @Inject
    private SignatureRepository repository;

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }

    @PostConstruct
    public void start() {
        Long id = insert();

        try {
            updateToFail(id);
        } catch (NoRollbackException e) {
            log.error("Exception thrown");
        }

        Signature signature = find(id);
        if (Objects.equals(signature.getState(), FAIL)) {
            log.info("Working correctly");
        } else {
            log.error("BUG");
        }
    }

    @Transactional
    public Long insert() {
        return repository.save(new Signature(NEW)).getId();
    }

    @Transactional
    public Signature find(long id) {
        return repository.findById(id).orElseThrow();
    }

    @Transactional(dontRollbackOn = NoRollbackException.class)
    public void updateToFail(Long id) throws NoRollbackException {
        repository.update(id, FAIL);
        throw new NoRollbackException();
    }
}