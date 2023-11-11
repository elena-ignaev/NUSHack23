# NUSH hack 2023 submission 
## Setup instructions 
### To run code locally: 

Open project in IntelliJ 
Go to File --> Project Structure --> Libraries 
Click +, then From Maven... 
search jedis, and select redis.clients:jedis:5.0.1 
, Press OK 

Click + again, then From Maven... 
search kafka, and select org.apache.kafka:kafka-clients:3.4.0 
OK 

Repeat similarly with the following Maven libraries.
- org.json:json:20220924
- org.apache.httpcomponents:httpcore:4.4.16
- org.apache.httpcomponents.core5:httpcore5:5.2.3
- org.apache.httpcomponents.client5:httpclient5:5.1.3

Press Apply, then OK.

To setup local server, ensure that Python as well as the Python modules in ```requirements.txt``` have already been installed.
To install the required modules, run ```pip install -r requirements.txt```

Ensure that the vectorizer model has been installed and placed in the same folder as ```word2vec_api.py```.
Drive link: https://drive.google.com/file/d/0B7XkCwpI5KDYNlNUTTlSS21pQmM/edit?resourcekey=0-wjGZdNAUop6WykTtMip30g

Run ```word2vec_api.py```. Note that the vectorizer will load twice before the server is ready.

Finally, run the project in IntelliJ.


