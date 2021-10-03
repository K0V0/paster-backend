package com.kovospace.paster.board.repositories;

import com.kovospace.paster.board.models.Item;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

  public List<Item> findAllByUser(long userId);

  public Optional<Item> findFirstByUserAndId(long userId, long id);
}
