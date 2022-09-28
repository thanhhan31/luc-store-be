package com.lucistore.lucistorebe.controller.payload.request.product;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shopee.backend.utility.datatype.EProductStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProductRequest {
	@NotNull
	private Long id;

	private String name;

	private String description;

	private Long idCategory;

	private Long idType;

	private Boolean isNew;

	private String codeSellPlace;

	private EProductStatus status;

	private Long idShopCategory;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date discountStart;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date discountEnd;
}
