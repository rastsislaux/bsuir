//---------------------------------------------------------------------------

#include <vcl.h>
#include <iostream.h>
#include <conio.h>
#include <math.h>

#pragma hdrstop

//---------------------------------------------------------------------------

#pragma argsused
int main(int argc, char* argv[])
{
        double x, y, z;
        cout << "Vvedite x, y, z: ";
        cin >> x >> y >> z;
        cout << endl;

        /*double  x = 3.981e-2,
                y = -1.625e3,
                z = 0.512;*/

        /*double a =
                pow(2, -x) *
                sqrt(x + pow(abs(y), 1./4)) *
                pow(pow(M_E, x-1/sin(z)), 1./3);*/

        double a =
                pow(2, -x) *
                sqrt(x + pow(abs(y), 1./4)) *
                pow(exp(x-1/sin(z)), 1./3);

        cout << a;
        getch();

        return 0;
}
//---------------------------------------------------------------------------
 