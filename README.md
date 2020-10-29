## Magic Command for the raspberry pi
```
$ sudo raspi-config
```
Goes to the config menu of the card.
```
$ ifconfig
```
Shows IP adresses and connectivity
```
$ sudo apt-get <module name>
```
Install some module in general but always look at the documentation first
### Linux commands
```
$ mkdir      create directory
$ cd         goes to directory
$ cd ..      goes to mother directory
$ ls         shows list of stuff in directory
$ rm         remove a file
$ rm -r      remove a directory
$ sudo       for securised access
$ nano       text editor
$ vim        text editor
```

## Git commands
git simulator : https://learngitbranching.js.org/?NODEMO=&locale=fr_FR
```
$ git init                    create a git depositery(.git)
$ git clone <link_to_github>  clone repositery and create remote
$ git pull                    fetch changes from server
$ git status                  shows current status
$ git add <file_name>         add file to next commit (put a . for all files)
$ git commit -m "message"     create the commit
$ git push origin <branch_name>     send the commit
$ git log                     list the history of commits
$ git branch <branch_name>    create new branch
$ git checkout <branch_name>  move to branch
$ git rebase <commit_bash>    change the current position in the history
$ git merge <branch_name>     merge branch_name to current branch
```
