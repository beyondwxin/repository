package com.newgen.tools;

public class ArticlesTools {
	
	public static String  getNums(String num,int x,int y) {
		String[] nums = num.split(",");
		float[] nums_int = new float [nums.length];
		String num_str="";
		for (int i = 0; i < nums.length; i++) {
				nums_int[i] = Float.parseFloat(nums[i])/100;
			if(i%2==0){
				num_str=num_str+(nums_int[i]*x)+",";
			}else{
				num_str=num_str+(nums_int[i]*y)+",";
			}
		}
		return num_str;
	}

}
