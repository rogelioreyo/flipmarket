package mx.com.agurno.flipmarket.repository;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import mx.com.agurno.flipmarket.entity.Item;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/test.properties")
public class ItemRepositoryIntegrationTest {
	
    @Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private ItemRepository itemRepository;
 
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    
    @Test
    public void whenFindByName_thenReturnItem() {
        // given
        Item item = new Item();
        item.setItemName("name");
        item.setItemCategory(itemCategoryRepository.findById(1L).get());
        item.setItemId("itemIdTest");
        entityManager.persist(item);
        entityManager.flush();
     
        // when
        Item found = itemRepository.findByItemName(item.getItemName());
     
        // then
        assertEquals(found.getItemName(), item.getItemName());
        
        entityManager.remove(found);
        entityManager.flush();
    }
}
