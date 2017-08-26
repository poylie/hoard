package com.hoard.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.hoard.app.domain.enumeration.Status;

/**
 * PURCHASE ORDER
 */
@ApiModel(description = "PURCHASE ORDER")
@Entity
@Table(name = "purchase_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "purchaseorder")
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "reference_number", nullable = false)
    private String referenceNumber;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @NotNull
    @Column(name = "last_updated", nullable = false)
    private ZonedDateTime lastUpdated;

    @NotNull
    @Column(name = "supplier", nullable = false)
    private String supplier;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "purchaseOrder")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PurchaseOrderItem> purchaseOrderItems = new HashSet<>();

    @ManyToOne
    private User author;

    @ManyToOne
    private User lastEdit;

    @ManyToOne
    private Group group;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public PurchaseOrder referenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
        return this;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public PurchaseOrder transactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public PurchaseOrder lastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getSupplier() {
        return supplier;
    }

    public PurchaseOrder supplier(String supplier) {
        this.supplier = supplier;
        return this;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Status getStatus() {
        return status;
    }

    public PurchaseOrder status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<PurchaseOrderItem> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }

    public PurchaseOrder purchaseOrderItems(Set<PurchaseOrderItem> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
        return this;
    }

    public PurchaseOrder addPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        this.purchaseOrderItems.add(purchaseOrderItem);
        purchaseOrderItem.setPurchaseOrder(this);
        return this;
    }

    public PurchaseOrder removePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        this.purchaseOrderItems.remove(purchaseOrderItem);
        purchaseOrderItem.setPurchaseOrder(null);
        return this;
    }

    public void setPurchaseOrderItems(Set<PurchaseOrderItem> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
    }

    public User getAuthor() {
        return author;
    }

    public PurchaseOrder author(User user) {
        this.author = user;
        return this;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public User getLastEdit() {
        return lastEdit;
    }

    public PurchaseOrder lastEdit(User user) {
        this.lastEdit = user;
        return this;
    }

    public void setLastEdit(User user) {
        this.lastEdit = user;
    }

    public Group getGroup() {
        return group;
    }

    public PurchaseOrder group(Group group) {
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
        PurchaseOrder purchaseOrder = (PurchaseOrder) o;
        if (purchaseOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchaseOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
            "id=" + getId() +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", supplier='" + getSupplier() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
