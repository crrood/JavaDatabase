JavaDatabase

A simple in-memory Java Database with multiple session capabilities.


Supported Commands:

SET [key] [value]
	Creates or asigns a key-value pair, ex: "SET a 20" creates 
	a variable named "a" and defines its value as 20
GET [key]
	Outputs the value of specified key, ex: "SET a 20" 
	followed by "GET a" will return 20
UNSET [key]
	Removes a key from the database
NUMEQUALTO [value]
	Outputs the number of keys with values equal to specified value
	ex: after "SET a 20", "SET b 10", "SET c 20",
		"NUMEQUALTO 20" will return 2

BEGIN
	Initializes a new session.  Changes made in the current 
	session can be reverted.  Supports multiple open sessions.
ROLLBACK
	Undoes all changes made in the most recently opened session.
COMMIT
	Permantently commits all changes in all open sessions.


Performance and Implementation:

GET, SET, UNSET, and NUMEQUALTO all run in constant time.
BEGIN runs in constant time, while ROLLBACK and COMMIT both run linearly 
over the number of keys in the database.

Each session is stored as a HashMap (from the java.utils.HashMap package),
and simultaneous sessions are stored in a LIFO stack represented by a Vector 
(from java.utils.Vector). Another HashMap keeps track of the number of 
instances of each value for NUMEQUALTO operations, and a list of keys 
is stored in a java.utils.HashSet object to facilitate easy updating of 
this object on rollbacks and commits.

Example Usage:

SET a 20
SET b 10
GET a		// 20
GET b		// 10
BEGIN
SET a 10
GET a		// 10
GET b		// 10
BEGIN
UNSET b
GET b		// null
NUMEQUALTO 10	// 1
ROLLBACK
NUMEQUALTO 10	// 2
ROLLBACK
GET a		// 20
NUMEQUALTO 10	// 1