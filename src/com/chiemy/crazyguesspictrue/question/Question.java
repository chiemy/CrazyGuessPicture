package com.chiemy.crazyguesspictrue.question;

public class Question implements IQuestion{
	private int no;
	private String rightAnswer;
	private String confuseString;
	public Question(int no,String rightAnswer,String confuseString){
		this.no = no;
		this.confuseString = confuseString;
		this.rightAnswer = rightAnswer;
	}
	@Override
	public String getAnswer() {
		return rightAnswer;
	}
	public String getConfuseString(){
		return confuseString;
	}
}
