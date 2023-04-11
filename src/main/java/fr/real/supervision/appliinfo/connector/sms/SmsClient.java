package fr.real.supervision.appliinfo.connector.sms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import fr.real.supervision.appliinfo.connector.sms.model.OpsGenieRequest;
import fr.real.supervision.appliinfo.connector.sms.model.OpsGenieResponse;
import fr.real.supervision.appliinfo.connector.sms.model.SmsApiResponse;
import fr.real.supervision.appliinfo.connector.sms.model.SmsRequest;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestParsingException;

@Service
@EnableConfigurationProperties(SmsProperties.class)
public class SmsClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsClient.class);

	@Autowired
	SmsProperties properties;

	@PostConstruct
	public void init() {

		Unirest.config().socketTimeout(60000).connectTimeout(10000).concurrency(200, 20)
				.setDefaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.setDefaultHeader("cache-control", "no-cache").followRedirects(false).enableCookieManagement(false)
				.setObjectMapper(new JacksonObjectMapper());

		if (properties.isProxyEnabled()) {
			Unirest.config().proxy(properties.getProxyHost(), properties.getProxyPort());
		}

		// TODO ajouter gestion du ssl
	}

	public void send(String message) throws SmsException {

		OpsGenieRequest opsGenieRequest = new OpsGenieRequest(message);

		LOGGER.info("Envoi d'une astreinte a OpsGenie {}", opsGenieRequest);

		HttpResponse<OpsGenieResponse> response = Unirest.post(properties.getOpsGenieUrl() + "alerts")
				.header("Authorization", String.format("GenieKey %s", properties.getOpsGenieToken()))
				.header("Accept", MediaType.APPLICATION_JSON_VALUE).body(opsGenieRequest)
				.asObject(OpsGenieResponse.class);

		// Traiter tous les cas ou l'astreint ne semble pas parti
		if (response.getStatus() != 200 && response.getStatus() != 202) {
			throw new SmsException(response.getStatus() + ":" + response.getStatusText());
		}

		Optional<UnirestParsingException> exception = response.getParsingError();

		if (exception.isPresent()) {
			throw new SmsException("SMS probleme de parsing : ", exception.get());
		}

	}

	public void send(String[] recipients, String message) throws SmsException {

		try {

			for (String recipient : recipients) {

				SmsRequest smsRequest = new SmsRequest(recipient, message);

				LOGGER.info("Envoi d'un sms a SmsFactor {}", smsRequest);

				String parametersEncoded = smsRequest.getContent();
				parametersEncoded = encodeValue(parametersEncoded);

				String encodedUrl = String.format("%s/%s?text=%s&to=%s", properties.getApiUrl(), "send",
						parametersEncoded, smsRequest.getTo());

				HttpResponse<SmsApiResponse> response = Unirest.get(encodedUrl)
						.header("Authorization", String.format("Bearer %s", properties.getApiToken()))
						.header("Accept", MediaType.APPLICATION_JSON_VALUE).asObject(SmsApiResponse.class);

				// Traiter tous les cas ou le sms ne semble pas parti
				if (response.getStatus() != 200) {
					throw new SmsException(response.getStatus() + ":" + response.getStatusText());
				}

				Optional<UnirestParsingException> exception = response.getParsingError();

				if (exception.isPresent()) {
					throw new SmsException("SMS probleme de parsing : ", exception.get());
				}

			}

		} catch (UnsupportedEncodingException exception) {
			throw new SmsException("SMS probleme d'encoding : " + exception.getMessage());
		}

	}

	public boolean ping() {
		return Unirest.get(properties.getUrl()).asString().getBody().contains("pong");
	}

	private String encodeValue(String value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
	}

}
