package com.hoard.app.repository;

import com.hoard.app.domain.UserGroup;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the UserGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup,Long> {

    @Query("select user_group from UserGroup user_group where user_group.user.login = ?#{principal.username}")
    List<UserGroup> findByUserIsCurrentUser();

    List<UserGroup> findByGroupId(Long groupId);

}
