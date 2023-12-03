# firefly-interview-task

Firefly Home assignment summary

What I’ve done
 created a java microservice using spring boot and mongo called: Essay-service.
This service runs everything in the essay domain.

Database
The DB that I’ve chosen is MongoDB. Why? Because It is NoSQL DB which I think is a good ft
since it could vertically scale and no SQL specific purposes such as joins or transaction or schema
were needed here in my option.

How the service work on high-level:
- The service is in charge of the endpoint from the client. It was done under the
REST convention (GET/POST...). see POSTMAN import for more details.
- The service is also in charge of creating a document of essayParsingRequest 
collection in the DB when new essay is created.
- The essay service has a scheduled method which creates a thread pool. It is scheduled
at a fixed time (every 10 minutes, can be configured). Each time it is awaken, each
thread reads from the of essayParsingRequest collection and parse it for words occurrences.
Then saves it in the DB under word Collection.
- When finding a word, the essay service looks for that word under the word
Collection


Why I chose to implement to word store in such a way?

I wanted to separated that ﬂow of processing the content from the essay creating. We return a
response to the client that the creating of the essay was done. In the background there is
another process (run with thread execution pool…) that in charge of the word
processing.

What I focus in the task:
1. Correctness – the code was checked manually and also is backed up with unit-test that
test the main methods (i.e., that processing of the words)
2. Clean-code (and readability) – I work by the popular convention such as SOLID:
• Single responsibility - each class has a single purpose
• Open–closed principle- the code is separated into modules and can be extended
easily – classes have an abstract parent when needed, I used polymorphism,
created interfaces…
• Dependency inversion principle: used Spring for that.
• There isn’t code duplication (from inside each service)
• I used configuration so it can be easily configured
• Main methods are logged with severity (INFO, WARNNG...).
• The main services and method are backed up with unit tests.
3. Efficiency and scalability – by using thread pool I can get a good parallel of the execution
of the word location in essay (the heavy task) also because it is a microservice multiple
instances can be used (i.e., on aws)
4. Architecture – everything is model in the mvc architecture of controller, service, db
client. There is separation of DTO’s and actual Object. I used Java libarys as needed
(Orika for mapping DTO to Business object for example). Used SpringBoot capabilities
(such as the controller and the access to mongoDB). The project is a maven project
which includes running the task each time we do mvn-install.

How to run this?
1. Run local mongo on the default port (cmd: mongod --port 27017)
2. Mvn clean install the service
3. Java run the created Jar for the service
4. Import to postman (or edit to see the CURL)
5. Run the HTTP endpoint (from postman or other way).
For any question, please feel free to contact me: 050-5795099 ☺
Thanks!
Yotam Eliraz
