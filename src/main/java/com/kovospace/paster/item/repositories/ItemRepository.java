package com.kovospace.paster.item.repositories;

import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.user.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

  List<Item> findAllByUser(User user);

  Optional<Item> findFirstByUserAndId(User user, long id);

  void deleteByUserAndId(User user, long id);
}
