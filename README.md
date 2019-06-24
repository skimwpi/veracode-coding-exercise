# veracode-coding-exercise

## Table of Contents
* [Overview](#overview)
* [Running the program](#running-the-program)
  * [Running from Intellij](#running-from-intellij)
  * [Running from command line](#running-from-command-line)
  * [Running with mysql](#running-with-mysql)
* [Task1](#task1)
  * [Creating users](#creating-users)
  * [Listing users](#listing-users)
  * [Reading single user](#reading-single-user)
  * [Updating user](#updating-user)
  * [Deleting user](#deleting-user)
  * [Search for user](#search-for-user)
* [Task2](#task2)
* [Bonus](#bonus)

## Overview
  In this section, I discuss the challenges, assumptions, and any interesting details that came up as I worked on this coding exercise.
  
  While I have worked with Spring Boot competitor, Dropwizard, I have never worked with Spring Boot before. Learning Spring Boot presented some challenges as it makes heavy use of annotations to get developers productive quickly. I had to spend fair amount of time reading samples and tutorials to understand parts of Spring Boot. While at first it was a bit frustrating, it became fun and exciting as things came together. A lot challenges had to do with how to do things in Spring Boot. Fortunately, there are plentiful of tutorials and answered questions on StackOverflow. I spent a lot more time reading than coding especially in the early part of this coding exercise.
  
  One interesting detail I learned about mySQL is how it has two table storage engines - InnoDB and MyISAM. The MyISAM is the older engine and it has a commonly experienced problem of short key length that is 'largely' overcome by using InnoDB. MyISAM gave me some problems that was resolved by switching to InnoDB.
 
  I took an assumption about what the User should be and added the fields - userName, firstName, middleName, and lastName where the userName is the primary key. I populated the data with Marvel super heroes, where userName is the hero name and the first, middle, and last names are their birth names. For example, Ironman and Anthony Edward Stark.

  The instructions for Task1 states to implement at least one of the 5 endpoints but I ended up implementing them all plus a bonus of being able to search for an user. It was not too difficult to do them all with Spring boot.
  
## Running the program
  Git clone the repository
  ```
  git clone https://github.com/skimwpi/veracode-coding-exercise.git
  ```
  
### Running from Intellij
  Import a new project by selecting the build.gradle to import and let intellij do its thing to build a new project. 
  
  To run the spring boot application, pass one of the following into the run configuration as environment variable depending on which database you like to run with. If you choose mysql, please read [Running with mysql](#running-with-mysql) first.
  ```
  spring.profiles.active=common,h2
  ```
  ```
  spring.profiles.active=common,mysql
  ```
  
  Run the Task2 RESTful test, [TestRestfulApi.java](https://github.com/skimwpi/veracode-coding-exercise/blob/master/src/test/java/com/veracode/codingexercise/task2/TestRestFulApi.java), as JUnit test. Note that the spring boot application must be running for the test to pass.
  
### Running from command line
  
  Run gradle wrapper in order to build with gradle
  ```
  ./gradlew wrapper
  ```
  
  Run the Task1 Spring Boot Application in one of two following ways, depending on which database you like to use. If you choose mysql, please read [Running with mysql](#running-with-mysql) first.
  ```
  ./gradlew bootRun --args='--spring.profiles.active=common,h2'
  ```
  ```
  ./gradlew bootRun --args='--spring.profiles.active=common,mysql'
  ```
  
  Run the Task2 RESTful test, [TestRestfulApi.java](https://github.com/skimwpi/veracode-coding-exercise/blob/master/src/test/java/com/veracode/codingexercise/task2/TestRestFulApi.java). Note that the spring boot application must be running for the test to pass.
  ```
  ./gradlew clean test
  ```
  
### Running with mysql  
  Please change [application-mysql.properties](https://github.com/skimwpi/veracode-coding-exercise/blob/master/application-mysql.properties) accordingly. You will probably need to change the following in the file:
  
  * spring.datasource.url 
    * This is the URL to mysql that describes the host name and the DB name. I used locally installed mysql that had a database named test. The serverTimezone=UTC is provided to avoid time zone conflicts/misunderstanding.
  * spring.datasource.username
    * The username to login to mysql.
  * spring.datasource.password
    * The password to login to mysql.
  
  
## Task1
  Completed. Run the spring boot application mentioned in [Running the program](#running-the-program).

### Creating users
  Other than with RestTemplate, you can manually create users with curl.
  ```
  curl -i -X POST -H "Content-Type:application/json" -d '{"userName": "ironman", "firstName": "Anthony", "middleName": "Edward", "lastName": "Stark" }' http://localhost:8080/users

HTTP/1.1 201 
Location: http://localhost:8080/users/ironman
Content-Type: application/hal+json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 24 Jun 2019 07:23:04 GMT

{
  "userName" : "ironman",
  "firstName" : "Anthony",
  "middleName" : "Edward",
  "lastName" : "Stark",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/users/ironman"
    },
    "user" : {
      "href" : "http://localhost:8080/users/ironman"
    }
  }
}%
  ```
### Listing users
  Other than with RestTemplate, you can manually list users with curl.
```
curl http://localhost:8080/users
{
  "_embedded" : {
    "users" : [ {
      "userName" : "captainamerica",
      "firstName" : "Steven",
      "middleName" : "",
      "lastName" : "Rogers",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/users/captainamerica"
        },
        "user" : {
          "href" : "http://localhost:8080/users/captainamerica"
        }
      }
    }, {
      "userName" : "ironman",
      "firstName" : "Anthony",
      "middleName" : "Edward",
      "lastName" : "Stark",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/users/ironman"
        },
        "user" : {
          "href" : "http://localhost:8080/users/ironman"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/users{?page,size,sort}",
      "templated" : true
    },
    "profile" : {
      "href" : "http://localhost:8080/profile/users"
    },
    "search" : {
      "href" : "http://localhost:8080/users/search"
    }
  },
  "page" : {
    "size" : 20,
    "totalElements" : 2,
    "totalPages" : 1,
    "number" : 0
  }
}%
```
### Reading single user
  Other than with RestTemplate, you can manually read a single user with curl.
```
curl http://localhost:8080/users/captainamerica
{
  "userName" : "captainamerica",
  "firstName" : "Steven",
  "middleName" : "",
  "lastName" : "Rogers",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/users/captainamerica"
    },
    "user" : {
      "href" : "http://localhost:8080/users/captainamerica"
    }
  }
}%         
```
### Updating user
  Other than with RestTemplate, you can manually update user with curl.
  ```
curl -X PUT -H "Content-Type:application/json" -d '{"userName": "captainamerica", "firstName": "James", "middleName": "Buchanan", "lastName": "Barnes"}' http://localhost:8080/users/captainamerica
{
  "userName" : "captainamerica",
  "firstName" : "James",
  "middleName" : "Buchanan",
  "lastName" : "Barnes",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/users/captainamerica"
    },
    "user" : {
      "href" : "http://localhost:8080/users/captainamerica"
    }
  }
}%
  ```
### Deleting user
  Other than with RestTemplate, you can manually delete user with curl.
```
curl -X DELETE http://localhost:8080/users/ironman
```
### Search for user
  You can manually search for user with curl with either their last or first name.
```
curl http://localhost:8080/users/search/findByLastName\?lastName\="Barnes"
{
  "_embedded" : {
    "users" : [ {
      "userName" : "captainamerica",
      "firstName" : "James",
      "middleName" : "Buchanan",
      "lastName" : "Barnes",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/users/captainamerica"
        },
        "user" : {
          "href" : "http://localhost:8080/users/captainamerica"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/users/search/findByLastName?lastName=Barnes"
    }
  }
}%
```
```
curl http://localhost:8080/users/search/findByFirstName\?firstName\="James"
{
  "_embedded" : {
    "users" : [ {
      "userName" : "captainamerica",
      "firstName" : "James",
      "middleName" : "Buchanan",
      "lastName" : "Barnes",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/users/captainamerica"
        },
        "user" : {
          "href" : "http://localhost:8080/users/captainamerica"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/users/search/findByFirstName?firstName=James"
    }
  }
}%                                     
```
  
## Task2
  Completed. Run the test mentioned in [Running the program](#running-the-program). The test will create users, get user list, update user, and finally delete users.

## Bonus
  Completed. Toggle between spring profiles h2 and mysql as mentioned in [Running the program](#running-the-program).
