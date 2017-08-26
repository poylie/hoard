package com.hoard.app.repository;

import com.hoard.app.domain.PurchaseOrder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the PurchaseOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {

    @Query("select purchase_order from PurchaseOrder purchase_order where purchase_order.author.login = ?#{principal.username}")
    List<PurchaseOrder> findByAuthorIsCurrentUser();

    @Query("select purchase_order from PurchaseOrder purchase_order where purchase_order.lastEdit.login = ?#{principal.username}")
    List<PurchaseOrder> findByLastEditIsCurrentUser();
    
}
