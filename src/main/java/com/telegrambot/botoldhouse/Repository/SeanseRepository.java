package com.telegrambot.botoldhouse.Repository;

import com.telegrambot.botoldhouse.Entity.Seanse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SeanseRepository extends CrudRepository<Seanse, Long> {

    @Query(value = "SELECT * FROM SEANSE WHERE MONTH(date) = ?1", nativeQuery = true)
    public List<Seanse> findSeanseByMonth(int month);

    @Query(value = "SELECT * FROM SEANSE WHERE MONTH(date) = ?1", nativeQuery = true)
    public List<Seanse> findSeansesByMontPageble(int month, PageRequest pageable);

    @Query(value = "SELECT * FROM SEANSE WHERE MONTH(date) = ?1", nativeQuery = true)
    public Seanse findSeanseByMontPageble(int month, PageRequest pageable);

}

