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
 * SALES ORDER
 */
@ApiModel(description = "SALES ORDER")
@Entity
@Table(name = "sales_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "salesorder")
public class SalesOrder implements Serializable {

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
    @Column(name = "customer", nullable = false)
    private String customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToOne
    @JoinColumn(unique = true)
    private User author;

    @OneToOne
    @JoinColumn(unique = true)
    private User lastEdit;

    @OneToMany(mappedBy = "salesOrder")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SalesOrderItem> salesOrderItems = new HashSet<>();

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

    public SalesOrder referenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
        return this;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public SalesOrder transactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public SalesOrder lastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCustomer() {
        return customer;
    }

    public SalesOrder customer(String customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Status getStatus() {
        return status;
    }

    public SalesOrder status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getAuthor() {
        return author;
    }

    public SalesOrder author(User user) {
        this.author = user;
        return this;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public User getLastEdit() {
        return lastEdit;
    }

    public SalesOrder lastEdit(User user) {
        this.lastEdit = user;
        return this;
    }

    public void setLastEdit(User user) {
        this.lastEdit = user;
    }

    public Set<SalesOrderItem> getSalesOrderItems() {
        return salesOrderItems;
    }

    public SalesOrder salesOrderItems(Set<SalesOrderItem> salesOrderItems) {
        this.salesOrderItems = salesOrderItems;
        return this;
    }

    public SalesOrder addSalesOrderItem(SalesOrderItem salesOrderItem) {
        this.salesOrderItems.add(salesOrderItem);
        salesOrderItem.setSalesOrder(this);
        return this;
    }

    public SalesOrder removeSalesOrderItem(SalesOrderItem salesOrderItem) {
        this.salesOrderItems.remove(salesOrderItem);
        salesOrderItem.setSalesOrder(null);
        return this;
    }

    public void setSalesOrderItems(Set<SalesOrderItem> salesOrderItems) {
        this.salesOrderItems = salesOrderItems;
    }

    public Group getGroup() {
        return group;
    }

    public SalesOrder group(Group group) {
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
        SalesOrder salesOrder = (SalesOrder) o;
        if (salesOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), salesOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SalesOrder{" +
            "id=" + getId() +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", customer='" + getCustomer() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
