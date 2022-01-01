#ifndef TREE_H
#define TREE_H

#include <iostream>
#include <vector>

#define person std::pair<int, std::string>

struct Tree {

    private:

    struct Node {
        int key;
        std::string name;

        Node *left, *right;
    } * root;

    Node * createLeaf(int key, std::string name) {
        Node * temp = new Node;
        temp->key = key;
        temp->name = name;
        temp->left = temp->right = nullptr;
        return temp;
    }

    std::string printTree(Node * node, int level) {
        std::string str, result;
        if (node) {
            result += printTree(node->right, level+1);
            for (int i = 0; i < level; i++)
                str += "       ";
            std::cout << str << node->key << std::endl;
            result += str + std::to_string(node->key) + "\n";
            result += printTree(node->left, level+1);
        }
        return result;
    }

    std::string printInfo(Node * node) {
        std::string result;
        if (node) {
            result += printInfo(node->right);
            std::cout << node->key << " " << node->name << std::endl;
            result += std::to_string(node->key) + " " + node->name + "\n";
            result += printInfo(node->left);
        }
        return result;
    }

    std::string printInfoReverse(Node * node) {
        std::string result;
        if (node) {
            result += printInfoReverse(node->left);
            std::cout << node->key << " " << node->name << std::endl;
            result += std::to_string(node->key) + " " + node->name + "\n";
            result += printInfoReverse(node->right);
        }
        return result;
    }

    int countNodesWithTwoAncestors(Node * node) {
        int result = 0;
        if (node->left && node->right) result++;
        if (node->right) result += countNodesWithTwoAncestors(node->right);
        if (node->left) result += countNodesWithTwoAncestors(node->left);
        return result;
    }

    static std::vector<person> toVector(Node * n) {

        std::vector<person> vect, temp;
        if (n->left) vect = toVector(n->left);
        vect.push_back(person(n->key, n->name));
        if (n->right) {
        temp = toVector(n->right);
        for (int i = 0; i < temp.size(); i++)
            vect.push_back(temp[i]);
        }
        return vect;

    }

    void makeBalanced(Node *& node, int first, int last, std::vector<person> arr) {
        if (first == last) {
            node = nullptr;
            return;
        }
        int middle = (first + last) / 2;
        node = new Node;
        node->key = arr[middle].first;
        node->name = arr[middle].second;
        makeBalanced(node->left, first, middle, arr);
        makeBalanced(node->right, middle+1, last, arr);
    }

    public:

    Tree(int key, std::string name) {
        root = createLeaf(key, name);
    }

    void addLeaf(int key, std::string name) {
        Node *prev, *temp;
        bool found = true;
        temp = root;
        while (temp && found) {
            prev = temp;
            if (key == temp->key) {
                found = false;
                std::cout << "Duplicated key!" << std::endl;
            } else {
                if (key < temp->key) temp = temp->left;
                else temp = temp->right;
            }
        }
        if (found) {
            temp = createLeaf(key, name);
            if (key < prev->key) prev->left = temp;
            else prev->right = temp;
        }
    }

    void removeNode(int key) {
        Node * del, *prevDel, *r, *prevR;
        del = root;
        prevDel = nullptr;

        // Searching for Node
        while (del && del->key != key) {
            prevDel = del;
            if (del->key > key) del = del->left;
            else del = del->right;
            if (!del) {
                std::cout << "Not found." << std::endl;
                return;
            }
        }

        // Replacement
        if (del->right == nullptr) r = del->left;
        else if (del->left == nullptr) r = del->right;
        else {
            prevR = del;
            r = del->left;
            while (r->right) {
                prevR = r;
                r = r->right;
            }
            if (prevR == del) r->right = del->right;
            else {
                r->right = del->right;
                prevR->right = r->left;
                r->left = del->left;
            }
        }
        if (del == root) root = r;
        else if (del->key < prevDel->key)
            prevDel->left = r;
        else
            prevDel->right = r;
        delete del;
    }

    std::string printTree() {
        return printTree(root, 0);
    }

    std::string printInfo() {
        return printInfo(root);
    }

    std::string printInfoReverse() {
        return printInfoReverse(root);
    }

    std::string printInfoByKey() {
        std::string result;
        std::vector<person> vect = toVector(root);
        for(int i = 0; i < vect.size(); i++) {
            std::cout << vect[i].first << " " << vect[i].second << std::endl;
            result += std::to_string(vect[i].first) + " " + vect[i].second + "\n";
        }
        return result;
    }

    Tree getBalanced() {
        Tree balanced(-1, "");
        std::vector<person> vect = toVector(root);
        balanced.makeBalanced(balanced.root, 0, vect.size(), vect);
        return balanced;
    }

    person get(int key) {
        Node * node;
        node = root;

        // Searching for Node
        while (node && node->key != key) {
            if (node->key > key) node = node->left;
            else node = node->right;
            if (!node) {
                std::cout << "Not found." << std::endl;
                return person(-1, "");
            }
        }

        return person(node->key, node->name);
    }

    int countNodesWithTwoAncestors() {
        return countNodesWithTwoAncestors(root);
    }

};

#endif // TREE_H
