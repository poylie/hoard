package com.hoard.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Group.
 */
@Entity
@Table(name = "jhi_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "group")
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "group_name", nullable = false)
    private String groupName;

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserGroup> users = new HashSet<>();

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SalesOrder> salesOrders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public Group groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<UserGroup> getUsers() {
        return users;
    }

    public Group users(Set<UserGroup> userGroups) {
        this.users = userGroups;
        return this;
    }

    public Group addUser(UserGroup userGroup) {
        this.users.add(userGroup);
        userGroup.setGroup(this);
        return this;
    }

    public Group removeUser(UserGroup userGroup) {
        this.users.remove(userGroup);
        userGroup.setGroup(null);
        return this;
    }

    public void setUsers(Set<UserGroup> userGroups) {
        this.users = userGroups;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Group products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Group addProduct(Product product) {
        this.products.add(product);
        product.setGroup(this);
        return this;
    }

    public Group removeProduct(Product product) {
        this.products.remove(product);
        product.setGroup(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public Group purchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
        return this;
    }

    public Group addPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.add(purchaseOrder);
        purchaseOrder.setGroup(this);
        return this;
    }

    public Group removePurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.remove(purchaseOrder);
        purchaseOrder.setGroup(null);
        return this;
    }

    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public Set<SalesOrder> getSalesOrders() {
        return salesOrders;
    }

    public Group salesOrders(Set<SalesOrder> salesOrders) {
        this.salesOrders = salesOrders;
        return this;
    }

    public Group addSalesOrder(SalesOrder salesOrder) {
        this.salesOrders.add(salesOrder);
        salesOrder.setGroup(this);
        return this;
    }

    public Group removeSalesOrder(SalesOrder salesOrder) {
        this.salesOrders.remove(salesOrder);
        salesOrder.setGroup(null);
        return this;
    }

    public void setSalesOrders(Set<SalesOrder> salesOrders) {
        this.salesOrders = salesOrders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group group = (Group) o;
        if (group.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), group.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Group{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            "}";
    }
}
