package de.berlin.htw;

import java.util.function.Supplier;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.junit.jupiter.api.BeforeEach;

import de.berlin.htw.entity.dto.ProjectEntity;
import de.berlin.htw.entity.dto.UserEntity;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
public abstract class AbstractTest {

    @Inject
    EntityManager entityManager;
    
    @Inject
    protected UserTransaction userTransaction;

    @BeforeEach
    void cleanDB() {
        final String userQuery = String.format("DELETE FROM %s", UserEntity.class.getSimpleName());
        runInTransaction(() -> entityManager.createQuery(userQuery).executeUpdate());

        final String projectQuery = String.format("DELETE FROM %s", ProjectEntity.class.getSimpleName());
        runInTransaction(() -> entityManager.createQuery(projectQuery).executeUpdate());
    }
    
    /**
     * Wrap a call in a database transaction.
     * @param task function that should be executed inside transaction
     */
    protected void runInTransaction(final Runnable task) {
        try {
            userTransaction.begin();
            entityManager.joinTransaction();
            task.run();
            userTransaction.commit();
        } catch (final NotSupportedException | SystemException | HeuristicMixedException e) {
            throw new RuntimeException(e);
        } catch (final RollbackException | HeuristicRollbackException e) {
            try {
                userTransaction.rollback();
            } catch (SystemException ex) {
                // no action
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Wrap a call in a database transaction
     *
     * @param supplier function that returns a value
     * @param <T>      return data type
     * @return supplier return value
     */
    protected <T> T runInTransaction(final Supplier<T> supplier) {

        try {
            userTransaction.begin();
            entityManager.joinTransaction();
            T result = supplier.get();
            userTransaction.commit();
            return result;
        } catch (final Exception e) {
            try {
                if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION) {
                    userTransaction.rollback();
                }
            } catch (final Exception ex) {
                // ignore
            }
            throw new RuntimeException(e);
        }
    }

}
