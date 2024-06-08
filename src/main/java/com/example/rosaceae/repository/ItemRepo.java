package com.example.rosaceae.repository;

import com.example.rosaceae.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepo extends JpaRepository<Item, Integer>, JpaSpecificationExecutor<Item> {
    Optional<Item> findByItemId(int id);
    List<Item> findByUserUsersID(int id);
    Page<Item> findByUserUsersID(int userId, Pageable pageable);
    @Query("SELECT i FROM Item i WHERE i.user.usersID = :userId " +
            "AND (:minPrice IS NULL OR i.itemPrice >= :minPrice) " +
            "AND (:maxPrice IS NULL OR i.itemPrice <= :maxPrice) " +
            "AND (:categoryName IS NULL OR i.category.categoryName = :categoryName) " +
            "AND (:itemTypeName IS NULL OR i.itemType.itemTypeName = :itemTypeName)")
    Page<Item> findByUserAndFilters(@Param("userId") int userId,
                                    @Param("minPrice") Double minPrice,
                                    @Param("maxPrice") Double maxPrice,
                                    @Param("categoryName") String categoryName,
                                    @Param("itemTypeName") String itemTypeName,
                                    Pageable pageable);

}
