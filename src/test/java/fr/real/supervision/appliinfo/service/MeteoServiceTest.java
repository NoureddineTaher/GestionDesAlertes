package fr.real.supervision.appliinfo.service;


import fr.real.supervision.appliinfo.model.Meteo;
import fr.real.supervision.appliinfo.web.meteo.service.MeteoService;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



import static org.junit.Assert.assertFalse;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MeteoServiceTest {


	@Autowired
	private MeteoService meteoService;

	@Test
	public void shouldAddMeteo()  {

			 Meteo meteo =  new Meteo();
			 meteo.setIcone("rouge");
			 meteo.setAgendaImpact("sans Impact");
			 meteo.setSection(3L);
			 meteoService.saveMeteoSection3();
			 assertFalse(meteoService.getMeteoBySection(3L).isEmpty());
	}

}
