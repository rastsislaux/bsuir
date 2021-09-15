//---------------------------------------------------------------------------

#include <vcl.h>
#include <iostream.h>
#include <conio.h>
#include <cstdlib.h>
#pragma hdrstop

//---------------------------------------------------------------------------

#pragma argsused
int main(int argc, char* argv[])
{
        cout << "Vvedite razmer massiva (n<=100): ";
        int n;
        cin >> n;
        srand(time(NULL));
        bool r;
        cout << "Vvedite 0, esli hotite vvesti sami ili 1, esli randomno: ";
        cin >> r;

        int arr[100], min = 0;

        if (r) {
                cout << "Vash massiv: ";
                for(int i = 0; i < n; i++) {
                        arr[i] = rand()%100-53;
                        cout << arr[i] << " ";
                }
        } else
                for(int i = 0; i < n; i++) cin >> arr[i];

        for (int i = 0; i < n; i++)
                if (abs(arr[min]) > abs(arr[i])) min = i;

        int sum = 0;
        for (int i = min+1; i < n; i++) {
                sum += abs(arr[i]);
        }

        cout << endl <<"Summa: " << sum;
        getch();
        
        return 0;
}
//---------------------------------------------------------------------------
 