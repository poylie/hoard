package com.hoard.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.hoard.app.domain.enumeration.InvitationStatus;

/**
 * A Invitation.
 */
@Entity
@Table(name = "invitation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "invitation")
public class Invitation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "invitation_status")
    private InvitationStatus invitationStatus;

    @ManyToOne
    private User inviter;

    @ManyToOne
    private User invitee;

    @ManyToOne
    private Group group;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }

    public Invitation invitationStatus(InvitationStatus invitationStatus) {
        this.invitationStatus = invitationStatus;
        return this;
    }

    public void setInvitationStatus(InvitationStatus invitationStatus) {
        this.invitationStatus = invitationStatus;
    }

    public User getInviter() {
        return inviter;
    }

    public Invitation inviter(User user) {
        this.inviter = user;
        return this;
    }

    public void setInviter(User user) {
        this.inviter = user;
    }

    public User getInvitee() {
        return invitee;
    }

    public Invitation invitee(User user) {
        this.invitee = user;
        return this;
    }

    public void setInvitee(User user) {
        this.invitee = user;
    }

    public Group getGroup() {
        return group;
    }

    public Invitation group(Group group) {
        this.group = group;
        return this;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Invitation invitation = (Invitation) o;
        if (invitation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invitation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Invitation{" +
            "id=" + getId() +
            ", invitationStatus='" + getInvitationStatus() + "'" +
            "}";
    }
}
