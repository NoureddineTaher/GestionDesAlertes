package fr.real.supervision.appliinfo.web.status.config;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Ticket {

	private String name;
	private String severity;
	private String reference;
	private String status;
	private LocalDateTime datePssoft;

}
