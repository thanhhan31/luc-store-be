package com.lucistore.lucistorebe.entity.user;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.lucistore.lucistorebe.utility.ERolePermission;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "user_role")
public class UserRole {
	@Id
	private String name;
	
	@ElementCollection(targetClass = ERolePermission.class)
	@CollectionTable(uniqueConstraints = @UniqueConstraint(columnNames = {"user_role_name", "permissions"} ))
	@Enumerated(EnumType.STRING)
	private Set<ERolePermission> permissions;
}
