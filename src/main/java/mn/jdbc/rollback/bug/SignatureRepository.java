package mn.jdbc.rollback.bug;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.io.ByteArrayInputStream;

@JdbcRepository(dialect = Dialect.H2)
public interface SignatureRepository extends CrudRepository<Signature, Long> {
    void update(@Id Long id, String state);
}
