package tests.db;

import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Test;


import static junit.framework.Assert.assertNotNull;

public class ContextTest implements SimplePersistenceTest {

    @Test
    public void ContextUp() { assertNotNull(entityManager()); }

    @Test
    public void contextUpWithTransaction() throws Exception {

        withTransaction(()->{});
    }
}
