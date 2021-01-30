package com.spring.messenger.repository;

import com.spring.messenger.model.UserProfile;
import com.spring.messenger.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUser(User user);
}
