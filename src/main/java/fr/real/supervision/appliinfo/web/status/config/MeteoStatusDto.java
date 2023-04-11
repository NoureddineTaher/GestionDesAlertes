package fr.real.supervision.appliinfo.web.status.config;

import lombok.Data;

@Data
public class MeteoStatusDto {

	private String domain;
	private String tool;
	private Ticket ticket;


}
