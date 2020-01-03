package com.coding.challenge.FullstackCodingChallengeBackend.controller;

import com.coding.challenge.FullstackCodingChallengeBackend.exception.AppException;
import com.coding.challenge.FullstackCodingChallengeBackend.exception.ResourceNotFoundException;
import com.coding.challenge.FullstackCodingChallengeBackend.model.Shop;
import com.coding.challenge.FullstackCodingChallengeBackend.model.User;
import com.coding.challenge.FullstackCodingChallengeBackend.payload.ApiResponse;
import com.coding.challenge.FullstackCodingChallengeBackend.repository.ShopRepository;
import com.coding.challenge.FullstackCodingChallengeBackend.repository.UserRepository;
import com.coding.challenge.FullstackCodingChallengeBackend.security.CurrentUser;
import com.coding.challenge.FullstackCodingChallengeBackend.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ShopController {
    @Autowired
    ShopRepository shopRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/insert/shop")
    public Shop createNewShop( @Valid @RequestBody Shop shop){

        return shopRepository.save(shop);
    }

    @GetMapping("/shops")
    public ResponseEntity<?> getAllShops() {

        List<Shop> shops = shopRepository.findAll();

        return ResponseEntity.ok(shops);
    }

    @GetMapping("/shops/near_by")
    public ResponseEntity<?> getShopsNearBy(@RequestParam Double latitude, @RequestParam Double longitude) {

        List<Shop> shops = shopRepository.getShopsNearBy(latitude, longitude);

        return ResponseEntity.ok(shops);
    }

    @GetMapping("/liked/shops")
    public ResponseEntity<?> getLikedShops(@CurrentUser UserPrincipal currentUser) {
        long userId = currentUser.getId();
        List<Shop> shops = shopRepository.getAllLikedShops(userId);

        return ResponseEntity.ok(shops);
    }

    @GetMapping("/like/shop/{shopId}")
    public ResponseEntity<?> likeShop(@CurrentUser UserPrincipal currentUser, @PathVariable (value = "shopId") Long shopId) {
        long userId = currentUser.getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not found."));
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new AppException("Shop not found."));

        shop.getLikes().add(user);
        shopRepository.save(shop);

        return new ResponseEntity(new ApiResponse(true, "User with id : "+userId+" liked Shop with id : "+shopId), HttpStatus.OK);
    }

    @DeleteMapping("/likes/remove/shop/{shopId}")
    public ResponseEntity<?> removeShopFromPreferred(@CurrentUser UserPrincipal currentUser, @PathVariable (value = "shopId") Long shopId){
        long userId = currentUser.getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not found."));
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new AppException("Shop not found."));

        shop.getLikes().remove(user);
        shopRepository.save(shop);

        return new ResponseEntity(new ApiResponse(true, "User with id : "+userId+" remove Shop with id : "+shopId+" from his preferred list"),
                HttpStatus.OK);
    }
}
