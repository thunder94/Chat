# Chat
Chat application using TCP and UDP sockets

## Getting Started

These instructions will get you a copy of the project up and running on your local machine
### Prerequisites

Tested on Java SE 9

No additional libraries needed

### Installing

Clone or download

Compile:

```
javac ChatServer.java
javac ChatClient.java
```

### Running
Start server:

```
java ChatServer portNumber
```
where ```portNumber``` is the port you want to start server on.
Sample usage:
```java ChatServer 12345```

Start client:

```
java ChatClient hostName portNumber
```
where ```hostName``` and ```portNumber``` parameters specify server's address.
For example: ```java ChatClient localhost 12345```

You can now launch multiple clients and talk to yourself!
