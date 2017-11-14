package com.zx.pdv.service;

public class Utils {

//	private static final int[] CNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
//
//	private static int calculateDigite(String str, int[] peso) {
//		int sum = 0;
//		for (int index = str.length() - 1, digite; index >= 0; index--) {
//			digite = Integer.parseInt(str.substring(index, index + 1));
//			sum += digite * peso[peso.length - str.length() + index];
//		}
//		sum = 11 - sum % 11;
//		return sum > 9 ? 0 : sum;
//	}
//
//	public boolean cnpjIsValid(String document) {
//		if (document.length() != 14) {
//			return false;
//		}
//
//		Integer digite1 = calculateDigite(document.substring(0, 12), CNPJ);
//		Integer digite2 = calculateDigite(document.substring(0, 12) + digite1, CNPJ);
//		return document.equals(document.substring(0, 12) + digite1.toString() + digite2.toString());
//
//	}
	
	public String clearDocument(String document) {
		document = document.trim();
		document = document.replace(".", "");
		document = document.replace("/", "");
		document = document.replace("-", "");
		return document;
	}
}
