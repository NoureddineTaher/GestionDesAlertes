package fr.real.supervision.appliinfo.web.status.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "adsn.supervision")
@Data
@Component
@RefreshScope
public class MeteoStatusConfigProps {

	private List<Domain> domains;

	public Map<String, MeteoStatusDto> getFlattenedDatas() {
		Map<String, MeteoStatusDto> result = new HashMap<>();

		for (Domain domain : domains) {
			for (Tool tool : domain.getTools()) {
				for (Ticket ticket : tool.getTickets()) {
					MeteoStatusDto dto = new MeteoStatusDto();
					dto.setDomain(domain.getName());
					dto.setTool(tool.getName());
					dto.setTicket(ticket);
					result.put(ticket.getName(), dto);
				}
			}
		}
		return result;
	}

}
