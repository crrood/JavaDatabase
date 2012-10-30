<h1> JavaDatabase </h1>

<h5> A simple in-memory Java Database with multiple session capabilities. </h5>

<h3>Supported Commands:</h3>

SET [key] [value] <br>
	Creates or asigns a key-value pair, ex: "SET a 20" creates
	a variable named "a" and defines its value as 20 <br>
GET [key] <br>
	Outputs the value of specified key, ex: "SET a 20" 
	followed by "GET a" will return 20 <br>
UNSET [key] <br>
	Removes a key from the database <br>
NUMEQUALTO [value] <br>
	Outputs the number of keys with values equal to specified value
	ex: after "SET a 20", "SET b 10", "SET c 20",
		"NUMEQUALTO 20" will return 2 <br>

BEGIN <br>
	Initializes a new session.  Changes made in the current 
	session can be reverted.  Supports multiple open sessions. <br>
ROLLBACK <br>
	Undoes all changes made in the most recently opened session. <br>
COMMIT <br>
	Permantently commits all changes in all open sessions. <br>


<h3>Performance and Implementation: </h3><br>

GET, SET, UNSET, and NUMEQUALTO all run in constant time. <br>
BEGIN runs in constant time, while ROLLBACK and COMMIT both run linearly 
over the number of keys in the database. <br>

Each session is stored as a HashMap (from the java.utils.HashMap package),
and simultaneous sessions are stored in a LIFO stack represented by a Vector 
(from java.utils.Vector). Another HashMap keeps track of the number of 
instances of each value for NUMEQUALTO operations, and a list of keys 
is stored in a java.utils.HashSet object to facilitate easy updating of 
this object on rollbacks and commits.

<h3>Example Usage:</h3>

SET a 20 <br>
SET b 10 <br>
GET a		// 20 <br>
GET b		// 10 <br>
BEGIN <br>
SET a 10 <br>
GET a		// 10 <br>
GET b		// 10 <br>
BEGIN <br>
UNSET b <br>
GET b		// null <br>
NUMEQUALTO 10	// 1 <br>
ROLLBACK <br>
NUMEQUALTO 10	// 2 <br>
ROLLBACK <br>
GET a		// 20 <br>
NUMEQUALTO 10	// 1 <br>