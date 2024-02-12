package it.dgspa.skeleton;

import it.dgspa.skeleton.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class SkeletonApplication {



	public static void main(String[] args) {

		SpringApplication.run(SkeletonApplication.class, args);



	}



}
