package fr.real.supervision.appliinfo.web.status.config;

import java.util.List;

import lombok.Data;

@Data
public class Tool {
	
	private String name;
	private List<Ticket> tickets;

}
