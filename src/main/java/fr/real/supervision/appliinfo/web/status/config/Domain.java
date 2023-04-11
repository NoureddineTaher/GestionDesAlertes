package fr.real.supervision.appliinfo.web.status.config;

import java.util.List;

import lombok.Data;

@Data
public class Domain {

	private String name;
	private List<Tool> tools;

}
