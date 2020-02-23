package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.RewardPoints;
import com.example.demo.model.Transaction;
import com.example.demo.model.UserRewardPoints;
import com.example.demo.repository.TransactionRespository;
import com.example.demo.service.RewardPointsCalculationService;

/**
 * 
 * Service implementation to calculate total reward points  for given user 
 * 
 *
 */

@Service
public class RewardPointsCalculationServiceImpl implements RewardPointsCalculationService {
	
	private static final int MIN_AMOUNT_ONE_REWARD=50;
	private static final int MIN_AMOUNT_TWO_REWARD=100;
	
	@Autowired
	
	TransactionRespository transactionRepository;
	
	/**
	 * calculate reward points for each user
	 */

	@Override
	public UserRewardPoints calculateRewardPoints(final String userId) {
		
		List<Transaction> transaction= transactionRepository.findByUserId(userId);
		Map<String, List<BigDecimal>> transactionsMonthMap=	processTransactions(transaction);
		Map<String, Long> rewardpointsMonthMap=calculateRewardForEachUser(transactionsMonthMap);
		
		return transformRewardPoints(rewardpointsMonthMap,userId);
	}
	
	/**
	 * transforms the reward details to {@UserRewardPoints}
	 * 
	 * @param rewardpointsMonthMap
	 * @param userId
	 * @return
	 */
	
	private UserRewardPoints transformRewardPoints(final Map<String, Long> rewardpointsMonthMap,final String userId) {
		List<RewardPoints> rewardpointsList= new ArrayList<>();
		UserRewardPoints userRewardPoints= new UserRewardPoints();
		rewardpointsMonthMap.forEach((k,v)->{
			RewardPoints rewardpoints= new RewardPoints();
			rewardpoints.setMonth(k);
			rewardpoints.setPoints(v.toString());
			rewardpointsList.add(rewardpoints);
		});
		
		Long totalRewardPoints=rewardpointsList.stream().mapToLong(o -> Long. parseLong(o.getPoints())).sum();
		
		userRewardPoints.setUserId(userId);
		userRewardPoints.setRewardpoints(rewardpointsList);
		userRewardPoints.setTotalRewardPoints(totalRewardPoints.toString());
		return userRewardPoints;
		
	}
	
	
	/**
	 * takes List of transactions per user an convert it to map contains transactions per month
	 * @param transactionsList
	 * @return
	 */
	
	private Map<String, List<BigDecimal>>  processTransactions(final List<Transaction> transactionsList) {
		 Map<String, List<BigDecimal>> transactionsMap = new HashMap<>();
		 		 
		 transactionsList.forEach(transaction->{
			 if( transactionsMap.containsKey(transaction.getMonth())) {
				 List<BigDecimal> originalTransactionAmoutList=	 transactionsMap.get(transaction.getMonth());
				 originalTransactionAmoutList.add(transaction.getTransactionAmount());
				 transactionsMap.replace(transaction.getMonth(), originalTransactionAmoutList);
			 }else {
				 List<BigDecimal> newtransactionAmountamoutList=new ArrayList<>();;
				 newtransactionAmountamoutList.add(transaction.getTransactionAmount());
				 transactionsMap.put(transaction.getMonth(), newtransactionAmountamoutList);
			 }
			 
			
				
			});
		 
		 return transactionsMap;
	}
	
	
	
	/**Calculates reward points for each month
	 * 
	 * @param transactionsOfMonths
	 * @return reward points  for each month
	 */
	
	private  Map<String, Long> calculateRewardForEachUser(
           final  Map<String, List<BigDecimal>> transactionsOfMonths) {
		Map<String, Long> hashMap= transactionsOfMonths.entrySet().stream().collect(Collectors.toMap(
				entry -> entry.getKey(), 
				entry -> calculateTotalRewardPoints(entry.getValue()))
			);
        return hashMap;
    }
	
	
	/**
	 * Calculates reward points for the transaction
	 * 
	 * @param transactionAmt for each transaction
	 * @return reward points for that transaction
	 */

   
    private  Long calculateRewardPoints(final BigDecimal transactionAmt) {
        if (transactionAmt.compareTo(new BigDecimal(MIN_AMOUNT_ONE_REWARD))>=0 && transactionAmt.compareTo(new BigDecimal(MIN_AMOUNT_TWO_REWARD)) <=0) {
            return transactionAmt.subtract (new BigDecimal(MIN_AMOUNT_ONE_REWARD)).longValue();
        } else if (transactionAmt. compareTo(new BigDecimal(MIN_AMOUNT_TWO_REWARD)) >0) {
            return (transactionAmt. subtract (new BigDecimal(MIN_AMOUNT_TWO_REWARD)).longValue() * 2 + MIN_AMOUNT_ONE_REWARD);
        } else {
            return Long.valueOf(0);
        }
    }
    
    
    /**
     * takes transaction amounts per month and returns total reward points for that month
     * 	
     * @param transactionamountList for each each month
     * @return total reward points 
     */

   
    private  long calculateTotalRewardPoints(
            final List<BigDecimal> transactionamountList) {
        return transactionamountList.stream()
                .mapToLong((transactionAmt) -> {
                    return calculateRewardPoints(transactionAmt);
                }).sum();
    }
	
	

}
