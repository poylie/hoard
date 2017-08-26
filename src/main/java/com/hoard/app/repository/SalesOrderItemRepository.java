package com.hoard.app.repository;

import com.hoard.app.domain.SalesOrderItem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SalesOrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalesOrderItemRepository extends JpaRepository<SalesOrderItem,Long> {
    
}
