# Repository Workflow

To understand our workflow we're going to first have to understand the roles and responsibilities of each discrete focus for the project 

* **Testing**  To break the code in a sensible way   
* **Feature Implementation**  Finishing items from the product backlog  
* **Bug Fixing** Fixing what has already been written or improving it

##Commit Messages

* NEW: Some new feature.
* ENHANCE: Improvement of a feature (not new feature).
* FIX: Bug fixes
* QUALITY: Cleaning up code / general quality of software improvements.


## Branches
**master** Versions, builds will be marked and pushed to master when we believe they reach a milestone or significant point  
**development** This branch is automatically generated from Jenkins, and is a collection of your dev branches.  
**dev/test<name>** The testing suite should be developed here.  
**dev/<feature/bugfix>** Pick a name that represents what you feel correctly corresponds to what you are trying to accomplish. This should  be forked off of the latest development branch when you are developing it.

## The Workflow
### Testing team 
- Ensure that the  **dev/test** has the latest code from the **development** branch.
- once it does just add test cases and push it back to the **remote repo**.

### Feature team  / Bug Fixing
* Fork the latest commit from the **development** and the branch name should have in it **dev/<What you are doing>**.
* Before committing it back to the **remote repo**, merge in any changes from **development** this ensures that all your code conforms to the test **latest** test suite and wont fail tests.