package com.lucistore.lucistorebe.utility;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListWithPagingResponse;

@Service
public class ServiceDataReturnConverter {
	@Autowired
	ModelMapper mapper;
	
	public <T, V> DataResponse<V> convertToDataResponse(T src, Class<V> cls) {
		return new DataResponse<>(mapper.map(src, cls));
	}
	
	public <T, V> ListWithPagingResponse<V> convertToListResponse(List<T> src, Class<V> cls, Page page) {
		return new ListWithPagingResponse<>(page.getPageNumber() + 1, page.getTotalPage() + 1,
				src.stream().map(p -> mapper.map(p, cls)).collect(Collectors.toList()));
	}
}
