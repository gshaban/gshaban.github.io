//============================================================================
// Name        : BinarySearchTree.cpp
// Author      : Shaban Ghaith
// Version     : 2.0
// Description : Enhanced CS 300 eBid Binary Search Tree for CS 499
//============================================================================

#include <algorithm>
#include <cctype>
#include <cstdlib>
#include <iomanip>
#include <iostream>
#include <stdexcept>
#include <string>
#include <time.h>

#include "CSVparser.hpp"

using namespace std;

double strToDouble(string str, char ch);
string normalizeAmount(string str);
bool isValidBidRow(const csv::Row& row, string& reason);

struct Bid {
    string bidId;
    string title;
    string fund;
    double amount;

    Bid() {
        amount = 0.0;
    }
};

void displayBid(Bid bid);

struct Node {
    Bid bid;
    Node* left;
    Node* right;

    Node() {
        left = nullptr;
        right = nullptr;
    }

    Node(Bid aBid) : Node() {
        bid = aBid;
    }
};

class BinarySearchTree {
private:
    Node* root;
    unsigned int nodeCount;

    void addNode(Node* node, Bid bid);
    void inOrder(Node* node);
    void postOrder(Node* node);
    void preOrder(Node* node);
    Node* removeNode(Node* node, string bidId);
    void deleteTree(Node* node);

public:
    BinarySearchTree();
    virtual ~BinarySearchTree();
    void InOrder();
    void PostOrder();
    void PreOrder();
    void Insert(Bid bid);
    void Remove(string bidId);
    Bid Search(string bidId);
    unsigned int Size();
};

BinarySearchTree::BinarySearchTree() {
    root = nullptr;
    nodeCount = 0;
}

BinarySearchTree::~BinarySearchTree() {
    deleteTree(root);
    root = nullptr;
    nodeCount = 0;
}

void BinarySearchTree::deleteTree(Node* node) {
    if (node == nullptr) {
        return;
    }
    deleteTree(node->left);
    deleteTree(node->right);
    delete node;
}

void BinarySearchTree::InOrder() {
    inOrder(root);
}

void BinarySearchTree::PostOrder() {
    postOrder(root);
}

void BinarySearchTree::PreOrder() {
    preOrder(root);
}

void BinarySearchTree::Insert(Bid bid) {
    if (bid.bidId.empty()) {
        throw invalid_argument("Bid ID cannot be empty.");
    }

    if (root == nullptr) {
        root = new Node(bid);
        ++nodeCount;
    } else {
        addNode(root, bid);
    }
}

void BinarySearchTree::Remove(string bidId) {
    if (!Search(bidId).bidId.empty()) {
        root = removeNode(root, bidId);
        --nodeCount;
    }
}

Bid BinarySearchTree::Search(string bidId) {
    Node* current = root;

    while (current != nullptr) {
        if (current->bid.bidId == bidId) {
            return current->bid;
        }

        if (bidId < current->bid.bidId) {
            current = current->left;
        } else {
            current = current->right;
        }
    }

    return Bid();
}

unsigned int BinarySearchTree::Size() {
    return nodeCount;
}

void BinarySearchTree::addNode(Node* node, Bid bid) {
    if (bid.bidId == node->bid.bidId) {
        node->bid = bid;
        return;
    }

    if (bid.bidId < node->bid.bidId) {
        if (node->left == nullptr) {
            node->left = new Node(bid);
            ++nodeCount;
        } else {
            addNode(node->left, bid);
        }
    } else {
        if (node->right == nullptr) {
            node->right = new Node(bid);
            ++nodeCount;
        } else {
            addNode(node->right, bid);
        }
    }
}

void BinarySearchTree::inOrder(Node* node) {
    if (node == nullptr) {
        return;
    }
    inOrder(node->left);
    displayBid(node->bid);
    inOrder(node->right);
}

void BinarySearchTree::postOrder(Node* node) {
    if (node == nullptr) {
        return;
    }
    postOrder(node->left);
    postOrder(node->right);
    displayBid(node->bid);
}

void BinarySearchTree::preOrder(Node* node) {
    if (node == nullptr) {
        return;
    }
    displayBid(node->bid);
    preOrder(node->left);
    preOrder(node->right);
}

Node* BinarySearchTree::removeNode(Node* node, string bidId) {
    if (node == nullptr) {
        return node;
    }

    if (bidId < node->bid.bidId) {
        node->left = removeNode(node->left, bidId);
    } else if (bidId > node->bid.bidId) {
        node->right = removeNode(node->right, bidId);
    } else {
        if (node->left == nullptr && node->right == nullptr) {
            delete node;
            return nullptr;
        }

        if (node->left == nullptr) {
            Node* temp = node->right;
            delete node;
            return temp;
        }

        if (node->right == nullptr) {
            Node* temp = node->left;
            delete node;
            return temp;
        }

        Node* successor = node->right;
        while (successor->left != nullptr) {
            successor = successor->left;
        }

        node->bid = successor->bid;
        node->right = removeNode(node->right, successor->bid.bidId);
    }

    return node;
}

void displayBid(Bid bid) {
    cout << bid.bidId << ": " << bid.title << " | "
         << fixed << setprecision(2) << bid.amount << " | "
         << bid.fund << endl;
}

void loadBids(string csvPath, BinarySearchTree* bst) {
    cout << "Loading CSV file " << csvPath << endl;

    unsigned int loaded = 0;
    unsigned int skipped = 0;

    try {
        csv::Parser file = csv::Parser(csvPath);

        for (unsigned int i = 0; i < file.rowCount(); i++) {
            string reason;
            if (!isValidBidRow(file[i], reason)) {
                ++skipped;
                cerr << "Skipping row " << (i + 1) << ": " << reason << endl;
                continue;
            }

            Bid bid;
            bid.bidId = file[i][1];
            bid.title = file[i][0];
            bid.fund = file[i][8];
            bid.amount = strToDouble(file[i][4], '$');

            bst->Insert(bid);
            ++loaded;
        }
    } catch (csv::Error& e) {
        cerr << e.what() << endl;
    } catch (exception& e) {
        cerr << "Unexpected load error: " << e.what() << endl;
    }

    cout << loaded << " bids loaded";
    if (skipped > 0) {
        cout << " (" << skipped << " skipped)";
    }
    cout << endl;
}

bool isValidBidRow(const csv::Row& row, string& reason) {
    if (row.size() < 9) {
        reason = "expected at least 9 columns";
        return false;
    }

    string title = row[0];
    string bidId = row[1];
    string amount = row[4];

    if (title.empty()) {
        reason = "missing title";
        return false;
    }

    if (bidId.empty()) {
        reason = "missing bid ID";
        return false;
    }

    if (amount.empty()) {
        reason = "missing amount";
        return false;
    }

    string normalized = normalizeAmount(amount);

    char* end = nullptr;
    strtod(normalized.c_str(), &end);
    if (end == normalized.c_str() || *end != '\0') {
        reason = "amount is not numeric";
        return false;
    }

    return true;
}

string normalizeAmount(string str) {
    str.erase(remove(str.begin(), str.end(), '$'), str.end());
    str.erase(remove(str.begin(), str.end(), ','), str.end());
    str.erase(remove(str.begin(), str.end(), '"'), str.end());
    str.erase(remove_if(str.begin(), str.end(), [](unsigned char c) { return isspace(c); }), str.end());
    return str;
}

double strToDouble(string str, char ch) {
    (void)ch;
    string normalized = normalizeAmount(str);
    return atof(normalized.c_str());
}

bool runSelfTest() {
    BinarySearchTree bst;

    Bid c;
    c.bidId = "300";
    c.title = "Center";
    c.fund = "General";
    c.amount = 300.0;

    Bid a;
    a.bidId = "100";
    a.title = "Left";
    a.fund = "General";
    a.amount = 100.0;

    Bid b;
    b.bidId = "200";
    b.title = "Left Right";
    b.fund = "General";
    b.amount = 200.0;

    Bid d;
    d.bidId = "400";
    d.title = "Right";
    d.fund = "General";
    d.amount = 400.0;

    bst.Insert(c);
    bst.Insert(a);
    bst.Insert(b);
    bst.Insert(d);

    if (bst.Size() != 4) {
        return false;
    }
    if (bst.Search("200").title != "Left Right") {
        return false;
    }

    bst.Remove("100");
    if (!bst.Search("100").bidId.empty() || bst.Size() != 3) {
        return false;
    }

    bst.Remove("300");
    if (!bst.Search("300").bidId.empty() || bst.Size() != 2) {
        return false;
    }

    return true;
}

int main(int argc, char* argv[]) {
    string csvPath;
    string bidKey;

    if (argc == 2 && string(argv[1]) == "--self-test") {
        cout << (runSelfTest() ? "Self-test passed." : "Self-test failed.") << endl;
        return runSelfTest() ? 0 : 1;
    }

    switch (argc) {
    case 2:
        csvPath = argv[1];
        bidKey = "98223";
        break;
    case 3:
        csvPath = argv[1];
        bidKey = argv[2];
        break;
    default:
        csvPath = "eBid_Monthly_Sales_Dec_2016.csv";
        bidKey = "98223";
    }

    clock_t ticks;
    BinarySearchTree* bst = new BinarySearchTree();
    Bid bid;

    int choice = 0;
    while (choice != 9) {
        cout << "Menu:" << endl;
        cout << "  1. Load Bids" << endl;
        cout << "  2. Display All Bids" << endl;
        cout << "  3. Find Bid" << endl;
        cout << "  4. Remove Bid" << endl;
        cout << "  5. Display Tree Size" << endl;
        cout << "  9. Exit" << endl;
        cout << "Enter choice: ";
        cin >> choice;

        switch (choice) {
        case 1:
            ticks = clock();
            loadBids(csvPath, bst);
            cout << bst->Size() << " bids in tree" << endl;
            ticks = clock() - ticks;
            cout << "time: " << ticks << " clock ticks" << endl;
            cout << "time: " << ticks * 1.0 / CLOCKS_PER_SEC << " seconds" << endl;
            break;

        case 2:
            bst->InOrder();
            break;

        case 3:
            ticks = clock();
            bid = bst->Search(bidKey);
            ticks = clock() - ticks;

            if (!bid.bidId.empty()) {
                displayBid(bid);
            } else {
                cout << "Bid Id " << bidKey << " not found." << endl;
            }

            cout << "time: " << ticks << " clock ticks" << endl;
            cout << "time: " << ticks * 1.0 / CLOCKS_PER_SEC << " seconds" << endl;
            break;

        case 4:
            bst->Remove(bidKey);
            break;

        case 5:
            cout << bst->Size() << " bids in tree" << endl;
            break;

        case 9:
            break;

        default:
            cout << "Invalid choice. Please enter a menu option." << endl;
        }
    }

    delete bst;
    cout << "Good bye." << endl;
    return 0;
}


