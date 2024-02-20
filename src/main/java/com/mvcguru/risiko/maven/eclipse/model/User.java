package com.mvcguru.risiko.maven.eclipse.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class User {
	
    private String username;
    
    private String password;

}
