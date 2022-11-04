package com.lucistore.lucistorebe.config;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lucistore.lucistorebe.controller.payload.dto.BuyerCartDetailDTO;
import com.lucistore.lucistorebe.controller.payload.dto.BuyerDTO;
import com.lucistore.lucistorebe.controller.payload.dto.LogDTO;
import com.lucistore.lucistorebe.controller.payload.dto.ProductCategoryDTO;
import com.lucistore.lucistorebe.controller.payload.dto.ProductCategoryGeneralDTO;
import com.lucistore.lucistorebe.controller.payload.dto.ProductGeneralDetailDTO;
import com.lucistore.lucistorebe.controller.payload.dto.ProductImageDTO;
import com.lucistore.lucistorebe.controller.payload.dto.ProductVariationDTO;
import com.lucistore.lucistorebe.controller.payload.dto.productdetail.ProductCategoryDetailDTO;
import com.lucistore.lucistorebe.controller.payload.dto.productdetail.ProductDetailDTO;
import com.lucistore.lucistorebe.entity.Log;
import com.lucistore.lucistorebe.entity.MediaResource;
import com.lucistore.lucistorebe.entity.product.Product;
import com.lucistore.lucistorebe.entity.product.ProductCategory;
import com.lucistore.lucistorebe.entity.product.ProductImage;
import com.lucistore.lucistorebe.entity.product.ProductVariation;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerCartDetail;

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
		
		Converter<String, Boolean> emptyPasswordCvt = c -> {
			return StringUtils.isBlank(c.getSource());
		};
		
		var lstChildProductCategoryCvt = generateListConverter(ProductCategory.class, ProductCategoryDTO.class, mapper);			
		var lstProductImageCvt = generateListConverter(ProductImage.class, ProductImageDTO.class, mapper);
		var lstProductVariationCvt = generateListConverter(ProductVariation.class, ProductVariationDTO.class, mapper);
		
		mapper.createTypeMap(Buyer.class, BuyerDTO.class).addMappings(m -> {
			m.using(mediaResourceCvt).map(Buyer::getAvatar, BuyerDTO::setAvatar);
			m.using(emptyPasswordCvt).map(src -> src.getUser().getPassword(), BuyerDTO::setEmptyPassword);
			m.map(src -> src.getUser().getUsername(), BuyerDTO::setUsername);
			m.map(src -> src.getUser().getPhone(), BuyerDTO::setPhone);
			m.map(src -> src.getUser().getEmail(), BuyerDTO::setEmail);
		});
		
		mapper.createTypeMap(ProductCategory.class, ProductCategoryDTO.class).addMappings(m -> {
			m.using(lstChildProductCategoryCvt).map(ProductCategory::getChild, ProductCategoryDTO::setChild);
			m.map(src -> src.getParent().getId(), ProductCategoryDTO::setIdParent);
		});
		
		mapper.createTypeMap(ProductCategory.class, ProductCategoryDetailDTO.class).addMappings(m -> {
			m.map(src -> src.getParent().getId(), ProductCategoryDetailDTO::setIdParent);
		});
		
		mapper.createTypeMap(Product.class, ProductDetailDTO.class).addMappings(m -> {
			m.using(lstProductImageCvt).map(Product::getImages, ProductDetailDTO::setImages);
			m.using(lstProductVariationCvt).map(Product::getVariations, ProductDetailDTO::setVariations);
			m.using(mediaResourceCvt).map(Product::getAvatar, ProductDetailDTO::setAvatar);
			
			m.<List<ProductCategoryGeneralDTO>>map(Product::getParents, (dst, value) -> dst.getCategory().setParents(value));
		});
		
		mapper.createTypeMap(Product.class, ProductGeneralDetailDTO.class).addMappings(m -> {
			m.using(mediaResourceCvt).map(Product::getAvatar, ProductGeneralDetailDTO::setAvatar);
		});
		
		mapper.createTypeMap(ProductImage.class, ProductImageDTO.class).addMappings(m -> {
			m.map(src -> src.getProduct().getId(), ProductImageDTO::setIdProduct);
			m.map(src -> src.getMedia().getUrl(), ProductImageDTO::setUrl);
			m.map(src -> src.getMedia().getResourceType(), ProductImageDTO::setResourceType);
		});
		
		mapper.createTypeMap(ProductVariation.class, ProductVariationDTO.class).addMappings(m -> {
			m.map(src -> src.getProduct().getId(), ProductVariationDTO::setIdProduct);
		});

		mapper.createTypeMap(BuyerCartDetail.class, BuyerCartDetailDTO.class).addMappings(m -> {
			m.map(src -> src.getBuyer().getId(), BuyerCartDetailDTO::setIdBuyer);
			m.map(src -> src.getProductVariation().getProduct(), BuyerCartDetailDTO::setProductDetail);
		});
		
		mapper.createTypeMap(Log.class, LogDTO.class).addMappings(m -> {
			m.map(src -> src.getUser().getUsername(), LogDTO::setUsername);
		});
		
		return mapper;
	}
	
	private <T,V> Converter<List<T>, List<V>> generateListConverter(Class<T> src, Class<V> dst, ModelMapper mapper) {
		return c -> {
			if (c.getSource() == null)
				return null;
			else
				return c.getSource().stream().map(m -> mapper.map(m, dst)).toList();
		};
	}
}
