package com.MyWebApp.Java_Web_App;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppService {
     @Autowired
    private JPArepo repo;

    public List<Products> getAllProducts() {
        return repo.findAll();
    }

   public Products addProduct(Products product) {

        return repo.save(product);
    }
    public void delProduct(int id) {
            repo.deleteById(id);
    }
      public List<String> get_product_name() { 
       List<String> namesList = repo.findAll()
            .stream()
            .map(Products::getProduct_name)
            .collect(Collectors.toList());
            return namesList;
   }
         public List<Integer> getIds() { 
       List<Integer> idList = repo.findAll()
            .stream()
            .map(Products::getId)
            .collect(Collectors.toList());
            return idList;
   }
      public List<Double> getPrice() { 
       List<Double> priceList = repo.findAll()
           .stream()
           .map(Products::getPrice)
           .collect(Collectors.toList());
           return priceList;

   }
    public List<Products> getCheaperProducts() {
      return repo.findAll()
      .stream()
      .filter(f -> f.getPrice() <= 200)
      .collect(Collectors.toList());
    
    }
    public List<Products> getExpensiveProducts() {
      return repo.findAll()
      .stream()
      .filter(f -> f.getPrice() >= 500)
      .collect(Collectors.toList());
    
    }
}
