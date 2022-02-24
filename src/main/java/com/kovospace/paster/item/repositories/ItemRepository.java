package com.kovospace.paster.item.repositories;

import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.user.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ItemRepository extends JpaRepository<Item, Long> {

  List<Item> findAllByUserOrderByCreatedAtDesc(User user);

  Optional<Item> findFirstByUserAndId(User user, long id);

  void deleteByUserAndId(User user, long id);

  // method to delete all user's items and let only first 20 of them
  // JPQL priamo nepodporuje limit a offset, paginacia sa robi cez objekty ktore JPA poskytuje
  @Transactional
  @Modifying
  @Query(value = "SELECT * FROM paster.item WHERE user_id=:userId ORDER BY created_at DESC LIMIT 99999 OFFSET 20",
          nativeQuery = true)
  List<Item> findUserItemsAbove20(long userId);
}
