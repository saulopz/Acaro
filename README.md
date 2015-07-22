# Acaro - A very simple Chatbot
## Developer: Saulo Popov Zambiasi
## Last modification: Jul 22 2015
## Version: 20150722 (I use the date for my version control)
Acaro is a simple example of a Chatbot for my classes Artificial Intelligence. This software and it's source code are available under BSD (3-clause) license.

### Prerequisites
You must have installed:
* Java JDK >= 7 (to compile);
* Java RC >= 7 (to use);
* Postgresql database

### Usage
There is a run.sh on project root directory that contains a single line:
<br/>
java -cp .:bin:lib/* program.Main
<br/>
Acaro Software will ask for a username. It's important to correct management of chat logs and agent memory.
<br>
At first time you run Acaro, go to menu **File** and ***Load a knowledge base directory***. Choose a folder with KB files and all KB files from this folder will be used to Acaro. Anytime you can change the KB folder in this menu option.

### Chatbot Knowledge Base (KB)
The kb files of Acaro are a simplified version of AIML to load information into a database. Acaro kb file ends with .kb and need to be allocated on a sub-folder. When Acaro open a KB, it loads all '.kb' files from this folder.
<br/>
Tags of files are the first char of line:
* # - comments
* c - category
* p - pattern
* t - template
* i - template conditional
** usage - i condition : template
* a - create a topic
* s - set topic (select a topic from a category)
* w - that (w - where) - Necessary a category name
<br/>
New tags created to this software:
* l - link (a link of a homepage)
* d - do / execute (something. It can be a specific structure)
* v - declaration of a variable
* f - scapes: It is a kind of template that will be used when nothing more was found (randomic response.

### Unimplemented features
Acaro is not using variables yet. Also, so it's not working tags: w, i.

