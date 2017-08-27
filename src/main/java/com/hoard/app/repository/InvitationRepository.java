package com.hoard.app.repository;

import com.hoard.app.domain.Invitation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Invitation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvitationRepository extends JpaRepository<Invitation,Long> {

    @Query("select invitation from Invitation invitation where invitation.inviter.login = ?#{principal.username}")
    List<Invitation> findByInviterIsCurrentUser();

    @Query("select invitation from Invitation invitation where invitation.invitee.login = ?#{principal.username}")
    List<Invitation> findByInviteeIsCurrentUser();
    
}
