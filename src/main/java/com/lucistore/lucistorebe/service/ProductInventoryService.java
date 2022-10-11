package com.lucistore.lucistorebe.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.ProductInventoryDTO;
import com.lucistore.lucistorebe.controller.payload.request.CreateProductInventoryRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListWithPagingResponse;
import com.lucistore.lucistorebe.entity.product.ProductInventory;
import com.lucistore.lucistorebe.entity.product.ProductVariation;
import com.lucistore.lucistorebe.repo.ProductInventoryRepo;
import com.lucistore.lucistorebe.repo.ProductVariationRepo;
import com.lucistore.lucistorebe.repo.UserRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.PageWithJpaSort;

@Service
public class ProductInventoryService {
    
    @Autowired
    ProductInventoryRepo productInventoryRepo;

    @Autowired
    ProductVariationRepo productVariationRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    private ServiceUtils serviceUtils;

    public DataResponse<ProductInventoryDTO> getById(Long id) {
        return serviceUtils.convertToDataResponse(
            productInventoryRepo.findById(id).orElseThrow(() -> new InvalidInputDataException("No product found with given id")), 
            ProductInventoryDTO.class
        );
    }

    public ListResponse<ProductInventoryDTO> getAll() {
        return serviceUtils.convertToListResponse(
            productInventoryRepo.findAll(), 
            ProductInventoryDTO.class
        );
    }

	public ListWithPagingResponse<ProductInventoryDTO> search(Long idProduct, Long idProductVariation, Long idImporter, Date importDate, Integer currentPage, Integer size, Sort sort) {
		
		// int count = productRepo.searchCount(idsCategory, searchName, searchDescription, status, minPrice, maxPrice).intValue();
		// PageWithJpaSort page = new PageWithJpaSort(currentPage, size, count, sort);
		
		// return serviceUtils.convertToListResponse(
		// 		productRepo.search(idsCategory, searchName, searchDescription, status, minPrice, maxPrice, page),
		// 		ProductInventoryDTO.class, 
		// 		page
		// 	);

        return null;
	}

    public ListResponse<ProductInventoryDTO> getAllByIdImporter(Long id) {
        // return serviceUtils.convertToListResponse(
        //     productInventoryRepo.findAllByIdImporter(id), 
        //     ProductInventoryDTO.class
        // );
        return null;
    }

    public ListResponse<ProductInventoryDTO> getAllByIdProduct(Long id) {
        // return serviceUtils.convertToListResponse(
        //     productInventoryRepo.findAllByIdProduct(id), 
        //     ProductInventoryDTO.class
        // );

        return null;
    }

    public DataResponse<ProductInventoryDTO> create(CreateProductInventoryRequest data, Long idImporter) {
        if(!productVariationRepo.existsById(data.getIdProductVariation())){
            throw new InvalidInputDataException("No product variation found with given id: " + data.getIdProductVariation());
        }

        ProductVariation productVariation = productVariationRepo.getReferenceById(data.getIdProductVariation());

        productVariation.setAvailableQuantity(productVariation.getAvailableQuantity() + data.getImportQuantity());
        
        return serviceUtils.convertToDataResponse(
            productInventoryRepo.save(
                new ProductInventory(
                    productVariation, 
                    userRepo.getReferenceById(idImporter), 
                    data.getImportQuantity()
                )),
                ProductInventoryDTO.class
            );
    }
}
