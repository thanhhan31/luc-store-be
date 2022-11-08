package com.lucistore.lucistorebe.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.utility.ELogType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "log")
public class Log {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "id_user")
	private User user;
	
	@Column(name = "date")
	@CreatedDate
	private Date date;
	
	@Column(name = "log_type")
	@Enumerated(EnumType.STRING)
	private ELogType logType;
	
	private String content;

	public Log(User user, String content, ELogType logType) {
		this.user = user;
		this.content = content;
		this.logType = logType;
	}
}
