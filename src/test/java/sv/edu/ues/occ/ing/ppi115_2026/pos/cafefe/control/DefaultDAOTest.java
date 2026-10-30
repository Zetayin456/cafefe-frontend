package sv.edu.ues.occ.ing.ppi115_2026.pos.cafefe.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultDAOTest {
    @Mock EntityManager em;

    static class StringDAO extends DefaultDAO<String> {
        EntityManager manager;
        StringDAO() { super(String.class); }
        @Override protected EntityManager getEntityManager() { return manager; }
    }
    private StringDAO dao() { StringDAO dao = new StringDAO(); dao.manager = em; return dao; }

    @Test void performsCreateEditRemoveAndFind() {
        StringDAO dao = dao(); when(em.merge("old")).thenReturn("managed"); when(em.find(String.class, "id")).thenReturn("found");
        dao.create("new"); assertEquals("managed", dao.edit("old")); dao.remove("old"); assertEquals("found", dao.find("id"));
        verify(em).persist("new"); verify(em).remove("managed"); verify(em).find(String.class, "id");
    }

    @Test void validatesNullManagerEntitiesIdsAndPageBounds() {
        StringDAO dao = new StringDAO();
        assertThrows(IllegalStateException.class, () -> dao.create("x"));
        assertThrows(IllegalStateException.class, () -> dao.edit("x"));
        assertThrows(IllegalStateException.class, () -> dao.remove("x"));
        assertThrows(IllegalStateException.class, () -> dao.find("x"));
        assertThrows(IllegalStateException.class, () -> dao.findRange(0, 1));
        assertThrows(IllegalStateException.class, dao::count);
        dao.manager = em;
        assertThrows(IllegalArgumentException.class, () -> dao.create(null));
        assertThrows(IllegalArgumentException.class, () -> dao.edit(null));
        assertThrows(IllegalArgumentException.class, () -> dao.remove(null));
        assertThrows(IllegalArgumentException.class, () -> dao.find(null));
        assertThrows(IllegalArgumentException.class, () -> dao.findRange(-1, 2));
        assertThrows(IllegalArgumentException.class, () -> dao.findRange(0, 0));
    }

    @Test void pagesAndCountsUsingCriteriaQueries() throws Exception {
        StringDAO dao = dao();
        CriteriaBuilder cb = mock(CriteriaBuilder.class); CriteriaQuery<String> select = mock(CriteriaQuery.class);
        Root<String> root = mock(Root.class); TypedQuery<String> page = mock(TypedQuery.class);
        when(em.getCriteriaBuilder()).thenReturn(cb); when(cb.createQuery(String.class)).thenReturn(select);
        when(select.from(String.class)).thenReturn(root); when(select.select(root)).thenReturn(select);
        when(em.createQuery(select)).thenReturn(page); when(page.getResultList()).thenReturn(List.of("a", "b"));
        assertEquals(List.of("a", "b"), dao.findRange(2, 5)); verify(page).setFirstResult(2); verify(page).setMaxResults(5);

        @SuppressWarnings("unchecked") CriteriaQuery<Long> countQuery = mock(CriteriaQuery.class);
        @SuppressWarnings("unchecked") Root<String> countRoot = mock(Root.class);
        @SuppressWarnings("unchecked") TypedQuery<Long> count = mock(TypedQuery.class);
        when(cb.createQuery(Long.class)).thenReturn(countQuery); when(countQuery.from(String.class)).thenReturn(countRoot);
        when(cb.count(countRoot)).thenReturn(mock(jakarta.persistence.criteria.Expression.class));
        when(countQuery.select(any())).thenReturn(countQuery); when(em.createQuery(countQuery)).thenReturn(count);
        when(count.getSingleResult()).thenReturn(7L);
        assertEquals(7, dao.count());
    }
}
