# Enable tab completion
source ~/git-completion.bash

# colors!
green="\[\033[0;32m\]"
blue="\[\033[0;34m\]"
purple="\[\033[0;35m\]"
reset="\[\033[0m\]"

# Change command prompt
source ~/git-prompt.sh
export GIT_PS1_SHOWDIRTYSTATE=1
# '\u' adds the name of the current user to the prompt
# '\$(__git_ps1)' adds git-related stuff
# '\W' adds the name of the current directory
export PS1="$purple\u$green\$(__git_ps1)$blue \W $ $reset"



alias notepad="C:/Program\ Files\ \(x86\)/notepad++.exe"
alias notepad="C:/Program\ Files\ \(x86\)/notepad++/notepad++.exe"
alias chrome="C:/Program\ Files\ \(x86\)/Google/Chrome/Application/chrome.exe"
alias sub="C:/Program\ Files/Sublime\ Text\ 3/sublime_text.exe"

