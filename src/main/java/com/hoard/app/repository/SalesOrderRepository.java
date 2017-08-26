package com.hoard.app.repository;

import com.hoard.app.domain.SalesOrder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SalesOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder,Long> {
    
}
