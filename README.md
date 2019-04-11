#HEIGVD-RES-2019-Labo-SMTP
## -MailPrank- Budry Nohan, Moreno Andrés
###Objectives
In this lab, we developed a client application (TCP) in Java. This client application uses the Socket API to communicate with an SMTP server. The code include a partial implementation of the SMTP protocol. These were the objectives of the lab:

* Make practical experiments to become familiar with the SMTP protocol. 

* Understand the notions of test double and mock server, which are useful when developing and testing a client-server application.

* Understand what it means to implement the SMTP protocol and be able to send e-mail messages, by working directly on top of the Socket API (i.e. you are not allowed to use a SMTP library).

* See how easy it is to send forged e-mails, which appear to be sent by certain people but in reality are issued by malicious users.

* Design a simple object-oriented model to implement the functional requirements described in the next paragraph.

### The labo/project

The mission was to develop a client application that automatically plays pranks on a list of victims:

* The user is able to define a list of victims ( file containing a list of e-mail addresses).

* The user is able to define how many groups of victims should be formed in a given campaign. In every group of victims, there is 1 sender and at least 2 recipients (i.e. the minimum size for a group is 3).

* The user is able to define a list of e-mail messages. When a prank is played on a group of victims, then one of these messages is selected. The mail is sent to all group recipients, from the address of the group sender. In other words, the recipient victims is lead to believe that the sender victim has sent them.

### Main source of inspiration

In order to start our project, our main source of inspiratin were four WebCast given by our teacher, Olivier Liechti. (Youtube channel https://www.youtube.com/user/oliechti/playlists ).

#------STILL NEED TO ADD---------
### * Instructions for setting up a mock SMTP server (with Docker)

### * Clear and simple instructions for configuring your tool and running a prank campaign

### * A description of your implementation:

document the key aspects of your code. 
It is probably a good idea to start with a class diagram. Decide which classes you want to show (focus on the important ones) and describe their responsibilities in text. It is also certainly a good idea to include examples of dialogues between your client and an SMTP server (maybe you also want to include some screenshots here).






