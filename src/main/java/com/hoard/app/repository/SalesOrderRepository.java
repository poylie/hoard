package com.hoard.app.repository;

import com.hoard.app.domain.SalesOrder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the SalesOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder,Long> {

    @Query("select sales_order from SalesOrder sales_order where sales_order.author.login = ?#{principal.username}")
    List<SalesOrder> findByAuthorIsCurrentUser();

    @Query("select sales_order from SalesOrder sales_order where sales_order.lastEdit.login = ?#{principal.username}")
    List<SalesOrder> findByLastEditIsCurrentUser();
    
}
