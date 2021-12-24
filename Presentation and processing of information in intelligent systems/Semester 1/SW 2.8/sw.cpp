#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

class Graph {
private:

    vector<vector<int>> adjacencyList;
    vector<vector<int>> distanceMatrix;
    vector<int> centers;

    int addVertice() {   // Добавляем вершину в список
        adjacencyList.push_back(vector<int>());
        return adjacencyList.size() - 1;
    }

    void addEdge(int where, int what) {   
        adjacencyList[where].push_back(what);
    }

    int getVerticesCount() {
        return adjacencyList.size();
    }

    vector<vector<int>> makeDistanceMatrix() {

        vector<vector<int>> result;
        for (int i = 0; i < adjacencyList.size(); i++) {
            result.push_back(vector<int>());
            for (int j = 0; j < adjacencyList.size(); j++)
                result[i].push_back(getDistance(i, j));
        }
        return result;

    }

    int getEccentricity(int i) {

        int max = 0;
        for (auto distance : distanceMatrix[i])
            if (distance > max) max = distance;
        return max;

    }

    vector<int> getCenters() {

        vector<int> eccentricites;
        for (int i = 0; i < getVerticesCount() - 1; i++)
            eccentricites.push_back(getEccentricity(i));

        int min = INT16_MAX;
        for (auto eccentricity : eccentricites)
            if (min > eccentricity) min = eccentricity;

        vector<int> result;
        for (int i = 0; i < getVerticesCount() - 1; i++)
            if (eccentricites[i] == min)
                result.push_back(i);

        return result;

    }

    int getDistance(int vStart, int vFinish, vector<int> visited = vector<int>()) {

        if (vStart == vFinish) return 0;

        for (auto vertice : visited)
            if (vertice == vStart) return INT16_MAX;

        visited.push_back(vStart);

        vector<int> distances;
        for (auto vertice : adjacencyList[vStart])
            distances.push_back(1 + getDistance(vertice, vFinish, visited));

        int min = INT16_MAX;
        for (int i = 0; i < distances.size(); i++)
            if (distances[i] < min) min = distances[i];

        return min;
    }

public:
    Graph(string path) {
        ifstream fin(path);

        if (!fin.is_open()) {
            cout << "Problems with opening this file" << endl;
            exit(1);
        }

        do {
            int where = addVertice();
            int token;
            fin >> token;

            while (token != -1) {
                addEdge(where, token - 1);
                fin >> token;
            }
        } while (!fin.eof());

        distanceMatrix = makeDistanceMatrix();
        centers = getCenters();

        fin.close();
    }

    int getMaximumDistanceBetweenCenters() {

        int max = 0;
        for (int i = 0; i < centers.size(); i++)
            for (int j = i + 1; j < centers.size(); j++) {
                int distance = getDistance(centers[i], centers[j]);
                if (distance > max)
                    max = distance;
            }
        return max;

    }

    double getAverageDistanceBetweenCenters() {

        vector<int> distances;
        for (int i = 0; i < centers.size(); i++)
            for (int j = i + 1; j < centers.size(); j++)
                distances.push_back(getDistance(centers[i], centers[j]));

        int sum = 0;
        for (auto distance : distances)
            sum += distance;

        return distances.size() != 0 ? (double)sum/distances.size() : 0;

    }

    // debug
    void printDistanceMatrix() {

        cout << getVerticesCount() << endl;

        for (auto x1 : distanceMatrix) {
            cout << '\n';
            for (auto x2: x1)
                cout << x2 << " ";
        }

    }

};

int main() {

    cout << "File path: ";
    string path; cin >> path;

    Graph myGraph(path);

    cout << "Max distance: "
         << myGraph.getMaximumDistanceBetweenCenters()
         << endl;

    cout << "Average distance: "
         << myGraph.getAverageDistanceBetweenCenters()
         << endl;

    return 0;

}