package com.poscodx.container.config.videosystem.mixing;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.poscodx.container.videosystem.DVDPlayer;
import com.poscodx.container.videosystem.DigitalVideoDisc;

/**
 * JavaConfig2 <------- JavaConfig1
 *              import
 */

@Configuration
@Import({DVDConfig.class})
public class DVDPlayerConfig {
	
	@Bean
	public DVDPlayer dvdPlayer(@Qualifier("avengers") DigitalVideoDisc dvd) {
		return new DVDPlayer(dvd);
	}
}
