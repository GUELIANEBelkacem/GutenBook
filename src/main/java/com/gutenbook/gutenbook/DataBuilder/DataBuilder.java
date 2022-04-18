package com.gutenbook.gutenbook.DataBuilder;

import com.gutenbook.gutenbook.Data.Jaccard;
import com.gutenbook.gutenbook.Data.Downloader;
import com.gutenbook.gutenbook.Data.Indexation;

public class DataBuilder {

	public static void main(String [] args) {
		try {
			
			//download the library
			Downloader.download(2000);
			
			//index the data 
			Indexation.index();
			
			//build the Jaccard Matrix and Graph
			Jaccard.build(0.6);
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
