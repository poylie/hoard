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
 * A SalesOrderItem.
 */
@Entity
@Table(name = "sales_order_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "salesorderitem")
public class SalesOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_price")
    private Integer totalPrice;

    @OneToMany(mappedBy = "salesOrderItem")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> items = new HashSet<>();

    @ManyToOne
    private SalesOrder salesOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public SalesOrderItem quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public SalesOrderItem totalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Set<Product> getItems() {
        return items;
    }

    public SalesOrderItem items(Set<Product> products) {
        this.items = products;
        return this;
    }

    public SalesOrderItem addItem(Product product) {
        this.items.add(product);
        product.setSalesOrderItem(this);
        return this;
    }

    public SalesOrderItem removeItem(Product product) {
        this.items.remove(product);
        product.setSalesOrderItem(null);
        return this;
    }

    public void setItems(Set<Product> products) {
        this.items = products;
    }

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public SalesOrderItem salesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
        return this;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SalesOrderItem salesOrderItem = (SalesOrderItem) o;
        if (salesOrderItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), salesOrderItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SalesOrderItem{" +
            "id=" + getId() +
            ", quantity='" + getQuantity() + "'" +
            ", totalPrice='" + getTotalPrice() + "'" +
            "}";
    }
}
