package com.kirby.lookthis.user.repository;

import com.kirby.lookthis.user.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {
}
