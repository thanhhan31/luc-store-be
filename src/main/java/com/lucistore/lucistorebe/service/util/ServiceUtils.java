package com.lucistore.lucistorebe.service.util;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lucistore.lucistorebe.controller.advice.exception.CommonRestException;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListWithPagingResponse;
import com.lucistore.lucistorebe.entity.UpdatableAvatar;
import com.lucistore.lucistorebe.entity.product.ProductCategory;
import com.lucistore.lucistorebe.service.MediaResourceService;
import com.lucistore.lucistorebe.utility.EProductCategoryStatus;
import com.lucistore.lucistorebe.utility.Page;
import com.lucistore.lucistorebe.utility.PageWithJpaSort;
import com.lucistore.lucistorebe.utility.PlatformPolicyParameter;

@Service
public class ServiceUtils {
	@Autowired
	ModelMapper mapper;
	
	@Autowired
	MediaResourceService mediaResourceService;
	
	public void updateAvatar(UpdatableAvatar entity, MultipartFile newAvatar) {
		byte[] byteNewAvatar;
		try {
			byteNewAvatar = newAvatar.getBytes();
		} catch (IOException e) {
			throw new CommonRestException("Can not processing new avatar");
		}
		
		if (entity.getAvatar() != null) {
			var oldAvatar = entity.getAvatar();
			entity.setAvatar(null);
			mediaResourceService.delete(oldAvatar.getId());
		}
		entity.setAvatar(mediaResourceService.save(byteNewAvatar));
	}
	
	public <T, V> DataResponse<V> convertToDataResponse(T src, Class<V> cls) {
		return new DataResponse<>(mapper.map(src, cls));
	}
	
	public <T, V> ListResponse<V> convertToListResponse(List<T> src, Class<V> cls) {
		return new ListResponse<>(src.stream().map(p -> mapper.map(p, cls)).toList());
	}
	
	public <T, V> ListWithPagingResponse<V> convertToListResponse(List<T> src, Class<V> cls, PageWithJpaSort page) {
		return new ListWithPagingResponse<>(page.getPageNumber() + 1, page.getTotalPage(),
				src.stream().map(p -> mapper.map(p, cls)).toList());
	}
	
	public <T, V> ListWithPagingResponse<V> convertToListResponse(List<T> src, Class<V> cls, Page page) {
		return new ListWithPagingResponse<>(page.getPageNumber() + 1, page.getTotalPage(),
				src.stream().map(p -> mapper.map(p, cls)).toList());
	}
	
	public <T, V> ListWithPagingResponse<V> convertToListResponse(org.springframework.data.domain.Page<T> src, Class<V> cls) {
		return new ListWithPagingResponse<>(src.getPageable().getPageNumber() + 1, src.getTotalPages(),
				src.stream().map(p -> mapper.map(p, cls)).toList());
	}
	
	public PageRequest getPageRequest(Integer page, Integer size) {
		if (page == null || page < 1)
			page = 0;
		else 
			page = page - 1;
		
		if (size == null) 
			size = PlatformPolicyParameter.DEFAULT_PAGE_SIZE;
		
		return PageRequest.of(page, size);
	}
	
	public boolean checkStatusProductCategory(ProductCategory pc, EProductCategoryStatus statusToCheck) { //check it and its parent
		while (pc != null) {
			if (pc.getStatus() == statusToCheck)
				return true;
			pc = pc.getParent();
		}
		
		return false;
	}
}
