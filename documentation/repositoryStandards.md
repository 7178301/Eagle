# Repository Workflow

To understand our workflow we're going to first have to understand the roles and responsibilities of each discrete focus for the project 

* **Testing**  To break the code in a senible way   
* **Feature Implementation**  Finishing items from the product backlog  
* **Bug Fixing** Fixing what has already been written or improving it


## Branches
**master** Versions, builds will be marked and pushed to master when we believe they reach a milestone or significant point  
**builds** This branch is auotmatically generated from Jenkins, and is a collection of your dev branches.  
**dev/test** The testing suite should be developed here.  
**dev/\<feature/bugfix\>** You pick a name that represents what you feel correctly corrosponds to what you are trying to accomplish. This should  be forked off of the latest builds branch when you are developing it, and merged with the latest testing branch before you push it.

## The Workflow
### Testing team 
- Ensure that the  **dev/test** has the latest code from the **builds** branch.
- once it does just add test cases and push it back to the **remote repo**.

### Feature team  / Bug Fixing
* Fork the latest commit from the **builds** and the branch name should have in it **dev/\<What you are doing\>**.
* Before committing it back to the **remote repo**, merge in any changes from **dev/test** this ensures that all your code conforms to the test **latest** test suite and wont fail Jenkins' tests.
* Push the branch to the **remote repo** for testing purposes, if your branch fails to merge to builds it is because it has failed a test. Check [Jenkins](http://dalesalter.net:8080) for a detailed description as to why your code has failed, fix it up and then push it again. 
* Once you are happy with your changes, remove the branch.
* Start a new branch for a new feature/change, start following these steps again

## Documentation
Just push all the documentation to **master**, if you wish to keep you current branch up to date with master, just do so by merging it into your current branch.