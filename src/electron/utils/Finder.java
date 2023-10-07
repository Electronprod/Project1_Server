package electron.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Finder {
	/**
	 * @author OpenAI ChatGPT 3.5
	 */
	private static int findLCSLength(String str1, String str2) {
	    int m = str1.length();
	    int n = str2.length();
	    int[][] dp = new int[m + 1][n + 1];

	    for (int i = 0; i <= m; i++) {
	        for (int j = 0; j <= n; j++) {
	            if (i == 0 || j == 0) {
	                dp[i][j] = 0;
	            } else if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
	                dp[i][j] = dp[i - 1][j - 1] + 1;
	            } else {
	                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
	            }
	        }
	    }
	    return dp[m][n];
	}
	/**
	 * @author OpenAI ChatGPT 3.5
	 */
	private static String findMostSimilarString(String input, List<String> stringList) {
	    String mostSimilarString = "";
	    int maxLCSLength = 0;

	    for (String str : stringList) {
	        int lcsLength = findLCSLength(input, str);
	        if (lcsLength > maxLCSLength) {
	            maxLCSLength = lcsLength;
	            mostSimilarString = str;
	        }
	    }
	    return mostSimilarString;
	}
	/**
	 * @author OpenAI ChatGPT 3.5
	 */
	public static String get(List<String> names,String tofind) {
		return findMostSimilarString(tofind, names);
	}
	
	/**
	 * From Project0_Server
	 * Duplicate deleter
	 * @param echos - list with duplicates
	 * @return list without duplicates
	 * @author Electron_prod
	 */
	  public static List<String> removeDuplicates(List<String> echos) {
		    Map<String, Integer> letters = new HashMap<>();
		    for (int i = 0; i < echos.size(); i++) {
		      String tempChar = echos.get(i);
		      if (!letters.containsKey(tempChar)) {
		        letters.put(tempChar, Integer.valueOf(1));
		      } else {
		        letters.put(tempChar, Integer.valueOf(((Integer)letters.get(tempChar)).intValue() + 1));
		      } 
		    } 
		    List<String> ans = new ArrayList<>();
		    for (Map.Entry<String, Integer> entry : letters.entrySet())
		      ans.add(entry.getKey()); 
		    
		    return ans;
		  }
}
