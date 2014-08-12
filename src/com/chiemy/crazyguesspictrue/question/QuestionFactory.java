package com.chiemy.crazyguesspictrue.question;

public class QuestionFactory {
	public static IQuestion getQuestion(int no){
		//此处应该是查询数据库等操作
		String rightAnswer = "";
		String confuseString = "";
		if(no == 1){
			rightAnswer = "abcd";
			confuseString = "abcdefghigklmnopqrstuvwx";
		}else{
			rightAnswer = "123456";
			confuseString = "123456789abcdefgh一二三四五六七";
		}
		return new Question(no,rightAnswer,confuseString);
	}
}
