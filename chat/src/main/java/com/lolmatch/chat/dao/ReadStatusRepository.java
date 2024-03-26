package com.lolmatch.chat.dao;

import com.lolmatch.chat.entity.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Integer> {

}
