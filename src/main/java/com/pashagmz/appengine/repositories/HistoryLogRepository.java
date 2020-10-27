package com.pashagmz.appengine.repositories;

import com.pashagmz.appengine.models.entity.HistoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryLogRepository extends JpaRepository<HistoryLog, Long> {
}
