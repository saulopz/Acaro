# Acaro : a very simple chatbot

**Created by Saulo Popov Zambiasi**.
**Version 20150723**.

Acaro is a simple example of a Chatbot for my classes Artificial Intelligence. This software and it's source code are available under BSD (3-clause) license.

## Prerequisites

* Java JDK >= 7
* Postgresql

## Usage

There is a run.sh on project root directory that contains a single line to execute this softwre:

`java -cp .:bin:lib/* program.Main`

Acaro Software will ask for a username. It's important to correct management of chat logs and agent memory.

At first time you run Acaro, go to menu **File** and ***Load a knowledge base directory***. Choose a folder with KB files and all KB files from this folder will be used to Acaro. Anytime you can change the KB folder in this menu option.

## Chatbot Knowledge Base (KB)

The kb files of Acaro are a simplified version of AIML to load information into a database. Acaro kb file ends with .kb and need to be allocated on a sub-folder. When Acaro open a KB, it loads all '.kb' files from this folder. Tags of kb files are the first char of lines.

### Tags based on AIML:

	* # - comments.
	* c - category.
	* p - pattern.
	* t - template.
	* i - template conditional (usage `i condition : template`).
	* a - create a topic.
	* s - set topic (select a topic from a category).
	* w - that (w - where) - Necessary a category name.

### My specific tags:

	* l - link (a link of a homepage).
	* d - do / execute (something. It can be a specific structure).
	* v - declaration of a variable.
	* f - scapes: It is a kind of template that will be used when nothing more was found (randomic response.

## Unimplemented features

Acaro is not using variables **yet**. Also, so it's not working tags: w, i.

