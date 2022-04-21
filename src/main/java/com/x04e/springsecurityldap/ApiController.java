package com.x04e.springsecurityldap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

	@Autowired
	private PersonDao personDao;
	@Autowired
	private FootDao footDao;
	@Autowired
	private GeneralDao generalDao;


	@GetMapping("/people")
	public List<Person> people(){
		return personDao.findAll();
	}

	@GetMapping("/feet")
	public List<Foot> feet(){
		return footDao.findAll();
	}

	@GetMapping("/general")
	public List<Person> general(){
		return generalDao.findAll();
	}

}
