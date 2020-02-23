package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.UserRewardPoints;
import com.example.demo.service.RewardPointsCalculationService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/demo")

/**
 * API to get rewards for each user
 * 
 *
 */
public class RewardPointsCalculatorContoller {
	
	@Autowired
	RewardPointsCalculationService service;
	
	@GetMapping(value="/rewards", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="getting reward points",notes="getting reward points")
	
	public ResponseEntity<UserRewardPoints> getRewardPoints(@RequestParam(name="userId", required=true) String userId) {
		UserRewardPoints u=service.calculateRewardPoints(userId);
	return new ResponseEntity<UserRewardPoints>(u, HttpStatus.OK);
		
		
	}
	
	

}
