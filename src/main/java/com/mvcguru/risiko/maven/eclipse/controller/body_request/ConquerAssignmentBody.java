package com.mvcguru.risiko.maven.eclipse.controller.body_request;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ConquerAssignmentBody {

	private String username;
	
	private int troops;
}
