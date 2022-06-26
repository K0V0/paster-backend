package com.kovospace.paster.item.repositories;

import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

  List<Item> findAllByUserOrderByCreatedAtDesc(User user);

  Optional<Item> findFirstByUserAndId(User user, long id);

  void deleteByUserAndId(User user, long id);

}
