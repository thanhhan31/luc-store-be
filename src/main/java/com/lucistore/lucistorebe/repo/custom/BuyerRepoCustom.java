package com.lucistore.lucistorebe.repo.custom;

import java.util.Date;
import java.util.List;

import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.utility.EGender;
import com.lucistore.lucistorebe.utility.EUserStatus;
import com.lucistore.lucistorebe.utility.Page;

public interface BuyerRepoCustom {

	List<Buyer> searchBuyer(String searchFullname, String searchUsername, String searchEmail, String searchPhone,
			EUserStatus status, Date dob, EGender gender, Boolean emailConfirmed, Boolean phoneConfirmed,
			Date createdDate, String lastModifiedBy, Page page);

	Long searchBuyerCount(String searchFullname, String searchUsername, String searchEmail, String searchPhone,
			EUserStatus status, Date dob, EGender gender, Boolean emailConfirmed, Boolean phoneConfirmed,
			Date createdDate, String lastModifiedBy);

}