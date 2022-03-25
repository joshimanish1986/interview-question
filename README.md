Steps to deploy and make the API calls
================================================

•	Clone the branch:- git clone https://github.com/joshimanish1986/interview-question.git
•	In the command prompt - cd ~/interview-question
•	Run mvn clean install
•	It will generate backbase-assignment-docker.jar in the target folder. 
•	Run java -jar target/backbase-assignment-docker.jar to bring the application up.

The application should start and following logs should be generated:-
2022-03-25 15:26:22.794  INFO 6768 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2022-03-25 15:26:22.806  INFO 6768 --- [           main] com.example.demo.DemoApplication         : Started DemoApplication in 4.089 seconds (JVM running for 4.551)


Use Postman to send request, some example below:-
Request 1: Create course (POST http://localhost:8080/courses)
 {
    "title": "CT2",
    "startDate": "2021-05-01",
    "endDate": "2021-05-05",
    "capacity": 10
}
Response 1: Response body with status 201:- 
 
{
    "id": 1,
    "title": "CT2",
    "startDate": "2021-05-01",
    "endDate": "2021-05-05",
    "capacity": 10,
    "remaining": 10,
    "partcipant": []
}

Request 2: Sign up user for course (POST http://localhost:8080/courses/1/add)

{
  "courseId": "1",
  "registrationDate": "2021-04-27",
  "name": "manish156"
}

Response 2: Response body with status 200 :-
{
    "id": 1,
    "title": "CT2",
    "startDate": "2021-05-01",
    "endDate": "2021-05-05",
    "capacity": 10,
    "remaining": 9,
    "partcipant": [
        {
            "id": 2,
            "name": "manish156",
            "registrationDate": "2021-04-27",
            "cancelDate": null
        }
    ]
}


Request 3: Get course details (GET http://localhost:8080/courses/1)

Response 3: Response body with status 200 :-
{
    "id": 1,
    "title": "CT2",
    "startDate": "2021-05-01",
    "endDate": "2021-05-05",
    "capacity": 10,
    "remaining": 9,
    "partcipant": [
        {
            "id": 2,
            "name": "manish156",
            "registrationDate": "2021-04-27",
            "cancelDate": null
        }
    ]
}

Request 4: Search course by title (GET http://localhost:8080/courses?q=CT2)

Response 4: Response body with status 200 :-
[
    {
        "id": 1,
        "title": "CT2",
        "startDate": "2021-05-01",
        "endDate": "2021-05-05",
        "capacity": 10,
        "remaining": 9,
        "partcipant": [
            {
                "id": 2,
                "name": "manish156",
                "registrationDate": "2021-04-27",
                "cancelDate": null
            }
        ]
    }
]

Request 4: Cancel user enrollment (POST  http://localhost:8080/courses/1/remove)
{
  "courseId": 1,
  "cancelDate": "2021-04-27",
  "name": "manish156"
}
Response 4: Response body with status 200 :-
{
    "id": 1,
    "title": "CT2",
    "startDate": "2021-05-01",
    "endDate": "2021-05-05",
    "capacity": 10,
    "remaining": 10,
    "partcipant": []
}