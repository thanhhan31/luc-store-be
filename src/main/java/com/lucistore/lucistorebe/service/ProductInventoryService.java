package com.lucistore.lucistorebe.service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.lucistore.lucistorebe.utility.filter.PagingInfo;
import com.lucistore.lucistorebe.utility.filter.ProductInventoryFilter;

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

	public ListWithPagingResponse<ProductInventoryDTO> search(ProductInventoryFilter filter, PagingInfo pagingInfo) {
		return serviceUtils.convertToListResponse(
            productInventoryRepo.search(filter, pagingInfo),
			ProductInventoryDTO.class
		);
	}

    @Transactional
    public ListResponse<ProductInventoryDTO> create(Long idImporter, Set<CreateProductInventoryRequest> data) {
        List<ProductInventory> pil = data.stream().map(item -> {
            if(!productVariationRepo.existsById(item.getIdProductVariation())){
                throw new InvalidInputDataException("No product variation found with given id");
            }

            ProductVariation productVariation = productVariationRepo.getReferenceById(item.getIdProductVariation());
            productVariation.setAvailableQuantity(productVariation.getAvailableQuantity() + item.getQuantity());

            return new ProductInventory(
                productVariation, 
                userRepo.getReferenceById(idImporter), 
                item.getQuantity()
            );
        }).toList();

        
        return serviceUtils.convertToListResponse(
            productInventoryRepo.saveAll(pil),
            ProductInventoryDTO.class
        );
    }
}
