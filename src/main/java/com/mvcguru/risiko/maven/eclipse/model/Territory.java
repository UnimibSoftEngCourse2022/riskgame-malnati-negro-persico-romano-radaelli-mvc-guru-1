package com.mvcguru.risiko.maven.eclipse.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class Territory implements Serializable{
	
	private int continent;
	
	private String name;
	
	private int armies;
	
	private String idOwner;
	
	private List<String> neighbors;
	
	private String svgPath;

	@Override
	public String toString() {
		return "Territory [name=" + name + ", armies=" + armies + ", idOwner=" + idOwner + "]";
	}
}
