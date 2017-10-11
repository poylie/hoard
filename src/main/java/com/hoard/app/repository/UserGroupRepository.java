package com.hoard.app.repository;

import com.hoard.app.domain.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the UserGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup,Long> {

    @Query("select user_group from UserGroup user_group where user_group.user.login = ?#{principal.username}")
    List<UserGroup> findByUserIsCurrentUser();

    Page<UserGroup> findByGroupId(Long groupId, Pageable pageable);

}
