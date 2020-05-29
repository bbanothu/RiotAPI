package com.server.main.controller;

public class champStats{
		public String champName;
		public int wins;
		public int losses;
		
		public champStats(String champName) {
			this.champName = FunctionController.myChampLookup.champName(champName);
			wins = 0;
			losses = 0;
		}
		
}
	
	

