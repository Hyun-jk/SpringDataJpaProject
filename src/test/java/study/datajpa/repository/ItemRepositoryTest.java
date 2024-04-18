package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test //save 메서드안에 Trascation이 들어있기 때문에 따로 어노테이션 처리 안해도 괜찮다.
    public void save() {
        Item item = new Item("A");
        itemRepository.save(item);

    }


}