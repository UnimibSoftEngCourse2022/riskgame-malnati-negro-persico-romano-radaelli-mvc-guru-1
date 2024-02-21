package com.mvcguru.risiko.maven.eclipse.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class User {
	
    private String username;
    
    private String password;

}
