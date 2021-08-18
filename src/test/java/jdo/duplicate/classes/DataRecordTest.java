package jdo.duplicate.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DataRecordTest
{
    @TempDir
    Path _temporaryDirectory;
    private EntityManagerFactory _entityManagerFactory;
    private EntityManager _entityManager;

    @BeforeEach
    void setUp()
    {
        _entityManagerFactory = Persistence.createEntityManagerFactory("objectdb:" + _temporaryDirectory.resolve("test.odb").toAbsolutePath());
        _entityManager = _entityManagerFactory.createEntityManager();
    }

    @Test
    void createAndQuerySomeDataRecords()
    {
        var recordA = new DataRecord(1L, Set.of());
        var recordB = new DataRecord(2L, Set.of(recordA.getId()));
        var recordC = new DataRecord(3L, Set.of(recordA.getId(), recordB.getId()));
        var records = List.of(recordA, recordB, recordC);

        _entityManager.getTransaction().begin();
        records.forEach(_entityManager::persist);
        _entityManager.getTransaction().commit();

        assertEquals(List.of(recordB, recordC), getParentsOf(recordA));
    }

    @AfterEach
    void tearDown()
    {
        if (_entityManager.getTransaction().isActive())
            _entityManager.getTransaction().rollback();

        _entityManager.close();
        _entityManagerFactory.close();
    }

    private List<DataRecord> getParentsOf(DataRecord record)
    {
        var query = _entityManager.createQuery("SELECT dr from DataRecord dr WHERE :childId in dr._children", DataRecord.class);
        query.setParameter("childId", record.getId());
        return query.getResultList();
    }
}
