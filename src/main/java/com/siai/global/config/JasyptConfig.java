package com.siai.global.config;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

	@Value("${jasypt.password}")
	private String password;

	@Bean
	public PooledPBEStringEncryptor jasyptStringEncryptor(){

		// PooledPBEStringEncryptor - 멀티코어 시스템에서 해독을 병렬 처리
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setPoolSize(4);
		encryptor.setPassword(password);
		encryptor.setAlgorithm("PBEWithMD5AndTripleDES"); // 암호화 알고리즘 설정
		return encryptor;
	}
}
