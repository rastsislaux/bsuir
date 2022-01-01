#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>

QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private slots:
    void on_addPushButton_clicked();

    void on_balancePushButton_clicked();

    void on_removeKeySpinBox_valueChanged(int arg1);

    void on_removePushButton_clicked();

    void on_clearPushButton_clicked();

    void on_printPushButton_clicked();

    void on_printReversePushButton_clicked();

    void on_pushButton_clicked();

    void on_individualTaskButton_clicked();

private:
    Ui::MainWindow *ui;
};
#endif // MAINWINDOW_H
