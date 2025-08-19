package com.ilway.skystat.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import com.ilway.skystat.application.usecase.WindRoseUseCase;

@SpringBootTest(classes = ItTestApp.class)
public class WindRoseUseCaseIT {

	@Autowired
	WindRoseUseCase windRoseUseCase;

	@Autowired
	ApplicationContext ctx;

	@Test
	void WindRoseUseCase_의존성_주입에_성공해야한다() {
		System.out.println(windRoseUseCase.getClass().getSimpleName());
	}

}
