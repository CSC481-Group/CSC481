## CSC 481 Project Workflow

#### I. Clone the repo

If using SSH Keys: 
```
git clone git@github.com:CSC481-Group/CSC481.git
cd CSC481
```

If using HTTPS: 
```
git clone https://github.com/CSC481-Group/CSC481.git
cd CSC481
```

#### II. Pull latest changes

You always want to update before working on anything! Helps cut down on merge conflicts.

```
git checkout main
git pull origin main
```

---
#### (!) III. Creating a feature branch

The `main` branch is protected, so you won't be able to work on it directly. You can create a new branch with: 
```
git checkout -b my-branch-name
```

(you can switch to a preexisting branch without creating it by leaving out the `-b` flag.)

#### IV. Saving changes

Whenever you're stepping away or just want to save progress / make a checkpoint, do 
```
git add . 
git commit -m "A useful commit message"
```
bearing in mind that `.` refers to your current directory (change this to whatever particular files/paths are relevant as needed) and that commits are immutable. If you do need to "rewrite history", you can run `git commit --amend` to include files you left out, change commit messages, or whatever other small tweaks. 

---
#### V. Pushing to the remote

To take your local history on your machine and move it up to GitHub, run 
```
git push -u origin my-branch-name
```

After the first push, all subsequent pushes from your current branch can be done with just `git push`.

#### VI. Creating a Pull Request (/ Merge Request)

If your stuff is ready to make it onto the main branch, **GitHub should (ideally) prompt you to create a Merge Request near the top of the screen just after your last** `push`<u>.</u> If that doesn't happen, from the Repo, just click "Pull requests" on the toolbar at the top, then "New pull request". Make sure that "base" is `main`, and "compare" is whatever your branch is called. 

I *think*, with the current branch rules, everyone is able to just approve their own merge requests whenever they want. **If you're able to do a *fast-forward merge*, (i.e., there are no conflicts), feel free to go ahead and do that.** 

On the other hand, **If there are merge conflicts, be careful!** The authors of the relevant portions might need to talk about what is/is not getting kept. Ideally, this won't happen very often or at all. 

After your pull request is merged, update your local main branch:

```
git checkout main
git pull origin main
```

And you're free to delete the branch that just got merged, as well (assuming you no longer need it for anything).
