//---------------------------------------------------------------------------

#include <vcl.h>
#include <math.h>
#include <stdio.h>
#pragma hdrstop

#include "Unit1.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TForm1 *Form1;
//---------------------------------------------------------------------------
__fastcall TForm1::TForm1(TComponent* Owner)
        : TForm(Owner)
{
}
//---------------------------------------------------------------------------
void __fastcall TForm1::Button1Click(TObject *Sender)
{
        double  z = StrToFloat(Edit1->Text),
                c = StrToFloat(Edit3->Text),
                d = StrToFloat(Edit2->Text),
                x, y, f;

        String function, condition;

        if (z<1) {
                x = z*z+1;
                condition="z<1";
        } else {
                x=z-1;
                condition="z>=1";
        }

        switch(RadioGroup1->ItemIndex) {
        case 0:
                f = 2*x;
                function = "2x";
                break;
        case 1:
                f = pow(x, 2);
                function = "x^2";
                break;
        case 2:
                f = x/3.;
                function = "x/3";
                break;
        }

        y =     (double)(d*f*pow(M_E, pow(sin(x), 3)) +
                c*log(x+1)) /
                sqrt(x);

        Results->Lines->Add("�������: " + condition);
        Results->Lines->Add("�������: f(x)=" + function);
        Results->Lines->Add("���������: " + (String)y);
        Results->Lines->Add("");
}
//---------------------------------------------------------------------------

