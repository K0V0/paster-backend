package com.kovospace.paster.item.repositories;

import com.kovospace.paster.item.models.File;
import com.kovospace.paster.item.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<Item> findFirstByItemAndId(Item item, long id);
}
