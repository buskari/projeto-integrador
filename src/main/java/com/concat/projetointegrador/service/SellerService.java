package com.concat.projetointegrador.service;

import com.concat.projetointegrador.dto.AllSalesDTO;
import com.concat.projetointegrador.dto.SaleDTO;
import com.concat.projetointegrador.exception.EntityNotFound;
import com.concat.projetointegrador.model.Cart;
import com.concat.projetointegrador.model.PurchasedOrder;
import com.concat.projetointegrador.model.Seller;
import com.concat.projetointegrador.repository.PurchasedOrderRepository;
import com.concat.projetointegrador.repository.SellerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SellerService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private SellerRepository sellerRepository;
    private PurchasedOrderRepository purchasedOrderRepository;

    /**
     * Find a Seller by ID or throw an EntityNotFound Exception
     * @param id A Long type ID from a seller
     * @return Seller
     */
    public Seller findByID(Long id) {
        Optional<Seller> seller = sellerRepository.findById(id);
        if (seller.isPresent()) {
        return seller.get();
        }
            throw new EntityNotFound("Vendedor não existe.");
    }

    /**
     * Save a Seller in database
     * @param seller is an object Seller to save
     * @return the object Seller with your id
     */
    public Seller create(Seller seller) {
            seller.setPassword(passwordEncoder.encode(seller.getPassword()));
            return sellerRepository.save(seller);
    }

    /**
     * Search punchased orders and build a hashMap
     * @return a hashmap with sellersID in keys and quantity of sale in values
     */
    public HashMap<Long, Integer> findBestSellers() {

        List<PurchasedOrder> purchasedOrders = purchasedOrderRepository.findAll();

        List<PurchasedOrder> finishedOrders = purchasedOrders.stream()
                .filter(order -> order.getStatus().equals("finalizado")).collect(Collectors.toList());

        if(finishedOrders.isEmpty()) {
            throw new RuntimeException("Não existem vendas finalizadas");
        }

            List<Cart> carts = new ArrayList();
            finishedOrders.stream().forEach(order -> {
                carts.addAll(order.getCart());
            });

        AllSalesDTO sales = AllSalesDTO.builder().sales(carts.stream()
                .map(sale -> SaleDTO.builder().sellerID(sale.getProduct().getSeller().getId())
                .username(sale.getProduct().getSeller().getUsername())
                .salesQuantity(sale.getQuantity())
                .build()).collect(Collectors.toList()))
                .build();

        Set<Long> sellers = new HashSet();
        sales.getSales().stream().forEach(sale -> sellers.add(sale.getSellerID()));

        HashMap<Long, Integer> sellersSales = new HashMap();
        sellers.stream().forEach(seller -> sellersSales.put(seller, sales
                .getSales().stream()
                .filter(element -> element.getSellerID().equals(seller))
                .reduce(0, (acc, curr) -> acc + curr.getSalesQuantity(), Integer::sum)));

        return sellersSales;

}

}
