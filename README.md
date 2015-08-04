# Sparrow
INSERT ABOUT PROJECT HERE

##Eagle (Drone API)
### IDE Recomendation
	- Prefered IDE: IntelliJ
	- Alternative: Android Studio or any gradle compatable IDE
###IntelliJ Configuration
	- Download the repository from https://github.com/7178301/Eagle.git
	- Change to the development branch
	- Open IntelliJ and click "Import Project"
	- Navigate to and select the root folder "eagle"
	- Click "Import Project from External Model" and select "gradle"
	- Ensure a java JDK or 1.7+ is selected for "gradle JVM" and click finish. This process could take a few minutes while project dependencies are downloaded. 
	- Click Run -> Edit Configurations
	- Add a new Gradle configuration
	- Add the following configurations
	     Gradle Project: eagle
	     Tasks: clean test assemble javadoc eagleClean jarMake jarCopy
	- Click Run -> Run to run the gradle configuration.


##Sparrow (Android Cleaning Application)
###IDE Recomendation
	- Prefered IDE: Android Studio
###Android Studio Configeration

	- Download the repository from https://github.com/7178301/Eagle.git
	- Change to the development branch
	- Open Android Studio and click "Import Project"
	- Navigate to and select the root folder "sparrow"
	- Ensure you allow dependencies to download including appropriate SDK's, build-tools, platform-tools etc...
	- Click Run -> Edit Configurations
	- Add a new Gradle configuration
	- Add the following configurations
	     Gradle Project: sparrow
	     Tasks: clean check assemble
	- Click Run -> Run to run the gradle configuration.

##Documentation
All relevant project documentation will be found in the documentation folder.  
If you have any further questions, please contact a member form the development team.

##Bugs
Please submit a Github issue regarding and bugs you might find.  
Please give a clear description of the error including the appropriate system configurations and environment where the bug was generated.
Lastly, please attach the appropriate label that applies to the issue.  

A member from the development team will review each issue and assign the appropriate person to address each issue as quick as possible.

##Development Team
Swinburne University Capstone Team  
  
Angela Lau (Team Leader) - 7160852@student.swin.edu.au  
Nicholas Alards - 7178301@student.swin.edu.au  
Cameron Cross - 7193432@student.swin.edu.au  
Lara Mir - 9693726@student.swin.edu.au  

##Licensing
The MIT License (MIT)

Copyright (c) 2015 Lara-m

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

