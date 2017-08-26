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
 * A PurchaseOrderItem.
 */
@Entity
@Table(name = "purchase_order_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "purchaseorderitem")
public class PurchaseOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_cost")
    private Integer totalCost;

    @OneToMany(mappedBy = "purchaseOrderItem")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> items = new HashSet<>();

    @ManyToOne
    private PurchaseOrder purchaseOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public PurchaseOrderItem quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public PurchaseOrderItem totalCost(Integer totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    public Set<Product> getItems() {
        return items;
    }

    public PurchaseOrderItem items(Set<Product> products) {
        this.items = products;
        return this;
    }

    public PurchaseOrderItem addItem(Product product) {
        this.items.add(product);
        product.setPurchaseOrderItem(this);
        return this;
    }

    public PurchaseOrderItem removeItem(Product product) {
        this.items.remove(product);
        product.setPurchaseOrderItem(null);
        return this;
    }

    public void setItems(Set<Product> products) {
        this.items = products;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public PurchaseOrderItem purchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
        return this;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PurchaseOrderItem purchaseOrderItem = (PurchaseOrderItem) o;
        if (purchaseOrderItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchaseOrderItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PurchaseOrderItem{" +
            "id=" + getId() +
            ", quantity='" + getQuantity() + "'" +
            ", totalCost='" + getTotalCost() + "'" +
            "}";
    }
}
