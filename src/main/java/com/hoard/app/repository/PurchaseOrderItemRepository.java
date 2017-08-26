package com.hoard.app.repository;

import com.hoard.app.domain.PurchaseOrderItem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PurchaseOrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem,Long> {
    
}
