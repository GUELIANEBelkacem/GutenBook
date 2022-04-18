# GutenBook

Link to [Frontend](https://github.com/GUELIANEBelkacem/GutenBookFront)

GuetenBook is a SpringBoot/JAVA backend of a search engine App designed for the [Gutenburg project's](https://www.gutenberg.org/) library.

To run, you first need to download the data from the Gutendex API, and construct the database by indexing the data.

All the functions necessary for this are found in DataBuilder/DataBuilder.java

```
      
      			//download the library (20min)
			Downloader.download(2000);
			
			//index the data (30min)
			Indexation.index();
			
			//build the Jaccard Matrix and Graph (3hours)
			Jaccard.build(0.6);
```
Comment the parts you don't need.

After that, run the server and enjoy!

Here are the available API calls:

Simple Search
http://localhost:8080/simplesearch/?word=inferno

Multiple Search
http://localhost:8080/multiplesearch/?words=inferno angels dante

RegEx Search
http://localhost:8080/regexsearch/?regex=inf(er*)no


**N.B:** the index along side the Jaccard matrix and graph have been provided with this repository for the first 2000 books of the API pages, you can run the server directly for tests no need to reconstruct the database 
