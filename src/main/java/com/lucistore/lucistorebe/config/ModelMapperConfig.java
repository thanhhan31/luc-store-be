package com.lucistore.lucistorebe.config;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lucistore.lucistorebe.controller.payload.dto.BuyerDTO;
import com.lucistore.lucistorebe.controller.payload.dto.ProductCategoryDTO;
import com.lucistore.lucistorebe.controller.payload.dto.ProductChildCategoryDTO;
import com.lucistore.lucistorebe.entity.MediaResource;
import com.lucistore.lucistorebe.entity.product.ProductCategory;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;

@Configuration
public class ModelMapperConfig {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		
		Converter<MediaResource, String> mediaResourceCvt = c -> {
			if (c.getSource() == null)
				return "";
			else
				return c.getSource().getUrl();
		};
		
		Converter<List<ProductCategory>, List<ProductChildCategoryDTO>> lstChildCategoryCvt = c -> {
			if (c.getSource() == null)
				return null;
			else
				return c.getSource().stream().map(m -> mapper.map(m, ProductChildCategoryDTO.class))
						.collect(Collectors.toList());
		};
		
		
		
		mapper.createTypeMap(Buyer.class, BuyerDTO.class).addMappings(m -> {
			m.using(mediaResourceCvt).map(Buyer::getAvatar, BuyerDTO::setAvatar);
			m.map(src -> src.getUser().getUsername(), BuyerDTO::setUsername);
			m.map(src -> src.getUser().getPhone(), BuyerDTO::setPhone);
			m.map(src -> src.getUser().getEmail(), BuyerDTO::setEmail);
		});
		
		mapper.createTypeMap(ProductCategory.class, ProductCategoryDTO.class).addMappings(m -> {
			m.using(lstChildCategoryCvt).map(src -> src.getChild(), ProductCategoryDTO::setChild);
			m.map(src -> src.getParent().getId(), ProductCategoryDTO::setIdParent);
		});
		
		return mapper;
	}
}
