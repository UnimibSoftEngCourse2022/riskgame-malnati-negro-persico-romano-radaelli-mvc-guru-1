package com.mvcguru.risiko.maven.eclipse.controller.bodyRequest;

import java.util.List;

import com.mvcguru.risiko.maven.eclipse.model.Territory;

import lombok.Data;

@Data
public class SetUpBody {
	
	private String username;
	
	private List<Territory> territories;
}
