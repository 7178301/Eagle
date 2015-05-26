# Coding Standards #
## Naming Convention ##
	.java 		CapitalLetterForEachNameNoSpaces.java  
	Class Name	CapitalLetterForEachNameNoSpaces{  
	variables	firstWordLowercaseThenUpperForTheRest  
	methods		firstWordLowercaseThenUpperForTheRest()

- Variables require logical human readable names. Variables like a, bta etc... are not accepltable  
- Variables use is to be instance or local variables. Global variables are a last resort.  
- Local variables that are required by another class should be used with Getters/Setters


## Documentation ##
### File/Class Headings ###
Each file will be required to have headings and some javadoc notation.  
here are some descriptions of the javadoc notation and some extra values we require

	Date Created		Day/Month/Year  
	Date Modified		Day/Month/Year - Editor  
	@author				First and Last name of Authors only  

#### Version Control ####
Version control will lead up to version 1.0 hopefully.  
We will be using a 3 tier version notation  

	[Major Functional Enhancement].[Minor Functional Enhancement].[Minor Bug Fix]
	0.1.10

#### Demonstration Example ####
	/**  
	* This is the description for the class. I am giving a brief couple
	* of sentences into its uses and what this class is used for  
	*  
	* Date Created		19/08/2014  
	* Last Modified		24/08/2014 - Jo Bloggs  
	* @author			John Smith  
	* @author			Jo Bloggs  
	*/


### Method Documentation ###
The following is the syntax for dosumentation required.

	Heading + description for method  
	   
	@param parameterName description  
	@return description  
	@throws exceptionClass description  

If there is more than one parameter you will have multiple, separate line of the @param notation

#### Demonstration Example ####

	/**  
	* This is a brief single sentence into what this method is used for  
	*  
	* @param int The maximum number of pizza's that can be ordered  
	* @return ArrayList of pizza's to be ordered  
	* @throws IndexOutOfBoundsException The array has been indexed outside it boundaries  
	*/  

## Control Structures ##
The following statements will require the following structures.  
if, else, for, while, do, switch and exception handling structure

Example:  

	for (int i = initial; i < max; i++) {
	    statements;
	}

- Operators must have a space on either side of the operator.  
- A statement must have a space before its open bracket  
- Braces (Curly brackets) must be on the same line as its statement  
- Code inside braces must be indented 1 level  
- The if statement does no require braces if there is only one following statement