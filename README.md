Fields of Wikipedia Articles and WikiNews articles that were indexed using Data Import Handler:
• DocId
• Title
• Description
• Timestamp
• Copy field: phrase_suggest for optimization of Wildcard queries

NOTE: MAKE SURE YOU CHANGE THE PATH OF DATASET in data-config.xml

I. Similarity Model:
We have defined our custom implementation for finding related Wikipedia Articles and WikiNews Articles. Solr, by default, uses tf-idf model which we would to be flawed given our dataset. It was preferring documents with shorter field length (Title field, Description field). Therefore, we found a research paper with SweetSpot Similarity Model, through which we were able to achieve greater control over varied lengthNorm values of different fields which was then found to be supported by Solr. Using this, we defined per field similarity for Title/Description in Wikipedia Articles and Title/Description in WikiNews Articles. We defined the steepness as 0.5, so that the documents are not penalized more for their field length.
Example (Wikipedia Articles)
Title:
. lengthNormMax =10 (Tokens) and lengthNormMin = 2
. lengthNorm steepness = 0.5
Description:
. lengthNormMax =350 and lengthNormMin = 75
Example (WikiNews Articles)
Title:
. lengthNormMax =10 (Tokens) and lengthNormMin = 2
. lengthNorm steepness = 0.5
Description:
. lengthNormMax =50 and lengthNormMin = 500

II. Scoring Model:
We were successful to implement the scoring logic using what we learnt in IR classes where we
were taught to give the highest importance to phrase matches. Our scoring logic uses shingles and
gives higher weightage to the following order,
1. Phrase Matches in Title: If all the words of the user query match the title of the document exactly
in same order. [Highest Weightage]
2. Phrase Matches in Description: If the phrases appears in the articles description
3. Contains All Words in Title: If all the words of user query are present in the Title (Need not be
present together)
4. Recent TimeStamp: This was done only for WikiNews articles (Recent documents were given
boost)
5. Word Present in Title: Individual word matches in title
6. Word Present in Description: Individual word present in Description
We used edismax Query Parser, and created a separate RequestHandler (/better) and the mainQuery
formation was done using magic fields and functions. In order to optimize the computation for
timestamp, we have considered only the day/month/year as the parameter avoiding processing of
Hour/Seconds as they take more time and we found them of little value.


III. Relatedness of News Articles
Our implementation of retrieving the related news stories is inspired from an idea that was mentioned in the thesis – “Extracting Named Entities and Synonyms from Wikipedia for use in News Search.” by “Christian Bøhn”.
Summary of thesis: Each wikipedia page has internal links, redirects and disambiguation which make it very easy for the users to find more information about a specific keyword mentioned in the article text.
This research work intend to collect all links pointing to the same article and then aggregate them based on the label to find synonyms as well as the popularity of that synonym.
Then a simple prototype is made up to two components:
 Named entity mining component that is used to extract entities from Wikipedia in order to
build a dictionary of named entities,
 List of common synonyms for each entity.
Adaptation of this concept in project:
1. Terms of query could be used as entities.
2. Links in user selected wikipedia article are extracted. These links can be used as list of terms related
to entities defined in step 1 .
4. These terms are ranked on basis of term frequency in selected wikipedia article, to find
top three relevant terms.
5. Query formulation for news article = terms from user query and top three terms found in previous
step.
A high-level overview of our implementation is given below,
1. Extract all the internal Wikipedia links that are present in the user-selected Wikipedia article. These will be the links that have to be scored on the basis of the term frequency in the article.
2. Create a local in-memory index of the Wikipedia article content, so we can make use of Lucene core APIs to compute scoring of the links against the wiki article content.
3. Save the link and it’s in a map, and reverse sort the same to obtain the top 3 results.
4. Perform string comparisons and concatenations, where necessary, to generate a single search query string.
5. Using SolrJ API trigger a request to Solr to retrieve the news stories in a ranked order for the search query we have formulated in the above steps.
6. Format the Solr response from JSON string to Java object, which can be used to render the related news stories, title and link, onto the UI.

IV. Social Media Posts
The project required fetching of Social Media Posts (Facebook) for the user query term. The fetched posts are ranked according to the tf-idf and a small boost is also given to the date parameter to fetch the latest articles. Used Graph API v 2.2 for the purpose.
It also lets you define new objects and actions in the social graph from your website, and the way that you create new instances of those actions and objects is by using the Graph API.
In order to make calls to the Graph API on behalf of a user, either to retrieve some user information or to post on the user's behalf, you will need the user to grant the necessary permissions to your website.
The query fed to the API after a click on a specific Wikipedia article was generated in a similar fashion as it was generated for fetching news articles.
The format for search Facebook API GET request is
graph.facebook.com/search?q={your-query}&[type={object-type}]&access_token=<AccessToken>