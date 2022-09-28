package com.lucistore.lucistorebe.controller.payload.request.product;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateProductRequest {
	@NotEmpty
	private String name;
	
	@NotEmpty
	private List<CreateProductProductVariationRequest> variation;
	
	@NotEmpty
	private String description;
	
	@NotNull
	private Long idCategory;
	
	@NotNull
	private Long idType;
	
	@NotNull
	private Boolean isNew;
	
	@NotEmpty
	private String codeSellPlace;
	
	private Long idShopCategory;
	
	@Schema(type = "string", pattern = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", example = "2023-01-01 00:00:00")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date discountStart;
	
	@Schema(type = "string", pattern = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", example = "2023-01-01 00:00:00")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date discountEnd;
}
