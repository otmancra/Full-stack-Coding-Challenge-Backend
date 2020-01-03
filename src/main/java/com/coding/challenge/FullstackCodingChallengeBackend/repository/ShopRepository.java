package com.coding.challenge.FullstackCodingChallengeBackend.repository;

import com.coding.challenge.FullstackCodingChallengeBackend.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopRepository  extends JpaRepository<Shop, Long> {
    @Query(value = "SELECT shop_id, name, image, latitude, longitude, ( 3959 * acos ( cos ( radians(?1) ) * cos( radians( latitude ) ) " +
            "* cos( radians( longitude ) - radians(?2) ) + sin ( radians(?1) ) " +
            "* sin( radians( latitude ) ) )) AS distance " +
            "FROM shops HAVING distance < 30 " +
            "ORDER BY distance LIMIT 0 , 30", nativeQuery = true)
    List<Shop> getShopsNearBy(double latitude, double longitude);

    @Query("SELECT NEW com.coding.challenge.FullstackCodingChallengeBackend.model.Shop(s.id, s.name, s.image, s.latitude, s.longitude) FROM Shop s inner join s.likes l WHERE l.id = ?1")
    List<Shop> getAllLikedShops(long userId);
}
