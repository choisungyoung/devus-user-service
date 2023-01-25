package com.devus.userservice.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@SequenceGenerator(name = "DVS_USER_SEQ_GEN", sequenceName = "DVS_USER_SEQ", initialValue = 1, allocationSize = 1)
@Table(name = "DVS_USER")
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DVS_USER_SEQ_GEN")
	private long id;

	@Column(unique = true)
	String email;
	
	@Column(unique = true)
	String name;
	String pwd;
	String introduction;
	String gitUrl;
	String webSiteUrl;
	String groupName;
	String pictureUrl;
	
	int point = 0;
	int popularity = 0;
}