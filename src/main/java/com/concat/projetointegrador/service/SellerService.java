package com.concat.projetointegrador.service;

import com.concat.projetointegrador.exception.EntityNotFound;
import com.concat.projetointegrador.model.Seller;
import com.concat.projetointegrador.repository.SellerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SellerService {

    private SellerRepository sellerRepository;

    public Seller create(Seller seller) {
        return sellerRepository.save(seller);
    }

    public List<Seller> findAll() {
        List<Seller> sellers = sellerRepository.findAll();
        if (sellers.isEmpty()) {
            throw new EntityNotFound("Vendedor não existe.");
        }
        return sellers;
    }

    public Seller findByID(Long id) {
        Optional<Seller> seller = sellerRepository.findById(id);
        if (seller.isPresent()) {
        return seller.get();
        }
            throw new EntityNotFound("Vendedor não existe.");
    }

    public Seller update(Seller seller, Long id) {
        Optional<Seller> doesTheSellerExist = sellerRepository.findById(id);
        if (doesTheSellerExist.isPresent()) {
            seller.setId(id);
            return sellerRepository.save(seller);
        }
        throw new EntityNotFound("Vendedor não existe.");
    }

    public void delete(Long id) {
        sellerRepository.deleteById(id);
    }
}
