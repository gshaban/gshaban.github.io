# CS499 Milestone Three - Enhanced eBid Binary Search Tree

Student: Shaban Ghaith
Category: Algorithms and Data Structures
Original course artifact: CS 300 eBid Binary Search Tree

## Enhancement Summary

This artifact enhances the original eBid Binary Search Tree program by completing the planned algorithms and data structure improvements:

- Initialized the tree root and node count correctly.
- Implemented recursive insertion into the binary search tree.
- Implemented in-order, pre-order and post-order traversal methods.
- Implemented iterative search by bid ID.
- Implemented bid removal for leaf nodes, one-child nodes and two-child nodes.
- Added recursive cleanup in the destructor to prevent memory leaks.
- Added a tree size counter for verification.
- Added CSV row validation and amount normalization before records enter the tree.
- Added a self-test mode to verify insertion, search, removal and size behavior.

## Files

- `BinarySearchTree.cpp`: Enhanced C++ artifact.
- `CSVparser.cpp` and `CSVparser.hpp`: Parser files used by the enhanced artifact.
- `eBid_Monthly_Sales_Dec_2016.csv`: Sample dataset used for verification.
- `original_artifact/`: Original source files preserved for comparison.

## Build and Run

Compile with g++:

```bash
g++ -std=c++17 -Wall -Wextra -pedantic BinarySearchTree.cpp CSVparser.cpp -o ebid_bst.exe
```

Run the one-step Windows build/test script if g++ is installed:`r`n`r`n```bash`r`nbuild_and_test.bat`r`n``` `r`n`r`nOr compile manually and run the built-in self-test:

```bash
./ebid_bst.exe --self-test
```

Run with the included sample CSV and default bid search key:

```bash
./ebid_bst.exe eBid_Monthly_Sales_Dec_2016.csv 98223
```

## Verification Completed

I compiled the enhanced artifact with `g++ -std=c++17 -Wall -Wextra -pedantic`.

I ran `./ebid_bst.exe --self-test` and the output was:

```text
Self-test passed.
```

I also loaded `eBid_Monthly_Sales_Dec_2016.csv`, searched for bid `98223`, removed it, searched again and confirmed the tree size changed from 76 to 75.

## AI Use Acknowledgment

I used ChatGPT to understand the assignment and get help with writing, code and debugging. I made the final decisions about the design, goals, learning and final work.

