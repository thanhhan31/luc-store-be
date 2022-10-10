package com.lucistore.lucistorebe.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.request.CreateProductInventory;
import com.lucistore.lucistorebe.controller.payload.request.CreateProductInventoryRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.entity.product.ProductInventory;
import com.lucistore.lucistorebe.repo.ProductInventoryRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;

@Service
public class ProductInventoryService {
    
    @Autowired
    private ProductInventoryRepo productInventoryRepo;

    @Autowired
    private ServiceUtils serviceUtils;

    public DataResponse<?> getById(Long id) {
        return serviceUtils.convertToDataResponse(
            productInventoryRepo.findById(id).orElseThrow(() -> new InvalidInputDataException("No product found with given id")), 
            ProductInventory.class
        );
    }

    public ListResponse<?> getAll() {
        return serviceUtils.convertToListResponse(
            productInventoryRepo.findAll(), 
            ProductInventory.class
        );
    }

    public ListResponse<?> getAllByIdImporter(Long id) {
        return serviceUtils.convertToListResponse(
            productInventoryRepo.findAllByIdImporter(id), 
            ProductInventory.class
        );
    }

    public ListResponse<?> getAllByIdProduct(Long id) {
        return serviceUtils.convertToListResponse(
            productInventoryRepo.findAllByIdProduct(id), 
            ProductInventory.class
        );
    }

    public DataResponse<?> create(@Valid CreateProductInventoryRequest data) {
        return serviceUtils.convertToDataResponse(
            productInventoryRepo.save(
                new CreateProductInventory(data).getProductInventory()
            ), 
            ProductInventory.class
        );
    }
}
