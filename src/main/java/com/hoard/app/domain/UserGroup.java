package com.hoard.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.hoard.app.domain.enumeration.Permission;

import com.hoard.app.domain.enumeration.Feature;

/**
 * A UserGroup.
 */
@Entity
@Table(name = "user_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "usergroup")
public class UserGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private Permission permission;

    @Enumerated(EnumType.STRING)
    @Column(name = "feature")
    private Feature feature;

    @ManyToOne
    private Group group;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Permission getPermission() {
        return permission;
    }

    public UserGroup permission(Permission permission) {
        this.permission = permission;
        return this;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Feature getFeature() {
        return feature;
    }

    public UserGroup feature(Feature feature) {
        this.feature = feature;
        return this;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public Group getGroup() {
        return group;
    }

    public UserGroup group(Group group) {
        this.group = group;
        return this;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public UserGroup user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserGroup userGroup = (UserGroup) o;
        if (userGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserGroup{" +
            "id=" + getId() +
            ", permission='" + getPermission() + "'" +
            ", feature='" + getFeature() + "'" +
            "}";
    }
}
