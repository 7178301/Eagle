#Documentation

##fileFolderStandards
ABOUT FILEOLDER HERE

##IDE Setup	
###IDE Recomendation
	- Prefered IDE: Android Studio
###Android Studio Configeration

	- Download the repository from https://github.com/7178301/Eagle.git
	- Change to the development branch
	- Open Android Studio and click "Import Project"
	- Navigate to and select the root folder "eagle"
	- Ensure you allow dependencies to download including appropriate SDK's, build-tools, platform-tools etc...
	- Click Run -> Edit Configurations
	- Add a new Gradle configuration
	- Add the following configurations
	     Gradle Project: eagle
	     Tasks: clean check assemble
	- Click Run -> Run to run the gradle configuration.

	
##repositoryStandards
###Commit Messages

* NEW: Some new feature.
* ENHANCE: Improvement of a feature (not new feature).
* FIX: Bug fixes
* QUALITY: Cleaning up code / general quality of software improvements.


### Branches
**master** Versions, builds will be marked and pushed to master when we believe they reach a milestone or significant point  
**development** This branch is automatically generated from Jenkins, and is a collection of your dev branches.  
**dev/<feature/bugfix>** Pick a name that represents what you feel correctly corresponds to what you are trying to accomplish. This should  be forked off of the latest development branch when you are developing it.

### Workflow

#### New Feature / Bug Fixing
* Fork the latest commit from the **development** and the branch name should have in it **dev/<What you are doing>**.
* Before committing it back to the **remote repo**, merge in any changes from **development** this ensures that all your code conforms to the test **latest** test suite and wont fail tests.
* Create a pull request for one of the **Development Team** members to check.


##codingStandards
### Naming Convention
	.java 		CapitalLetterForEachNameNoSpaces.java  
	Class Name	CapitalLetterForEachNameNoSpaces{  
	variables	firstWordLowercaseThenUpperForTheRest  
	methods		firstWordLowercaseThenUpperForTheRest()

- Variables require logical human readable names. Variables like a, bta etc... are not accepltable  
- Variables use is to be instance or local variables. Global variables are a last resort.  
- Local variables that are required by another class should be used with Getters/Setters


### Documentation ##
#### File/Class Headings ###
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
	* @author			John Smith  [<email>]
	* @author			Jo Bloggs  [<email>]
	*/


### Method Documentation ###
The following is the syntax for dosumentation required.

	Heading + description for method  
	   
	@param parameterName description  
	@return description  
	@throws exceptionClass description  

If there is more than one parameter you will have multiple, separate line of the @param notation

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
