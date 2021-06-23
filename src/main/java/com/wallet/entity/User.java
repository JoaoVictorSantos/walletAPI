package com.wallet.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name="users")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6247426483779448189L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String email;
}
