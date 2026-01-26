#!/usr/bin/env bash

main="master"
develop="develop"
prefixes=(
    "feat"
    "bugfix"
    "hotfix"
    "chore"
    "refactor"
    "docs"
    "style"
    "test"
    "perf"
    "ci"
    "build"
    "revert"
    "release"
)
suffix="[-.0-9_a-z]+"

IFS="|"
regex="^($main|$develop|(${prefixes[*]})\/$suffix)$"
unset IFS

branch_name=$(git symbolic-ref --short HEAD)

if [[ ! $branch_name =~ $regex ]]; then
    echo "--------------------------------------------------------------------------------"
    echo "ERROR: Branch name '$branch_name' is invalid"
    echo "All branch names must match the following regular expression:"
    printf "\n\t$regex\n\n"
    echo "Allowed prefixes:"
    printf "\n\t${prefixes[*]}\n\n"
    echo "--------------------------------------------------------------------------------"
    exit 1
fi

exit 0
