#include "mainwindow.h"
#include "./ui_mainwindow.h"
#include "tree.h"
#include "QMessageBox"
#include <cstdlib>

Tree * myTree = nullptr;

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_addPushButton_clicked()
{
    if (!myTree)
        myTree = new Tree(ui->addKeySpinBox->value(), ui->addNameLineEdit->text().toStdString());
    else
        myTree->addLeaf(ui->addKeySpinBox->value(), ui->addNameLineEdit->text().toStdString());

    ui->outputTextBrowser->clear();
    ui->outputTextBrowser->setText(QString::fromStdString(myTree->printTree()));
    ui->addKeySpinBox->setValue(ui->addKeySpinBox->value() + 1);
}

void MainWindow::on_balancePushButton_clicked()
{
    if (myTree) {
        myTree = new Tree(myTree->getBalanced());
        ui->outputTextBrowser->clear();
        ui->outputTextBrowser->setText(QString::fromStdString(myTree->printTree()));
    } else {
        QMessageBox::warning(this, "Ошибка!", "Дерево не инициализировано.");
    }
}

void MainWindow::on_removeKeySpinBox_valueChanged(int arg1)
{
    if (myTree) {
        person temp = myTree->get(arg1);
        ui->removeNameLineEdit->setText(QString::fromStdString(temp.second));
    } else {
        QMessageBox::warning(this, "Ошибка!", "Дерево не инициализировано.");
    }
}

void MainWindow::on_removePushButton_clicked()
{
    if (myTree) {
        myTree->removeNode(ui->removeKeySpinBox->value());
        ui->outputTextBrowser->setText(QString::fromStdString(myTree->printTree()));
    } else {
        QMessageBox::warning(this, "Ошибка!", "Дерево не инициализировано.");
    }
}

void MainWindow::on_clearPushButton_clicked()
{
    ui->addKeySpinBox->setValue(0);
    ui->removeKeySpinBox->setValue(0);
    delete myTree;
    myTree = nullptr;
    ui->outputTextBrowser->clear();
}

void MainWindow::on_printPushButton_clicked()
{
    if (myTree)
        ui->printerTextBrowser->setText(QString::fromStdString(myTree->printInfo()));
    else {
        QMessageBox::warning(this, "Ошибка!", "Дерево не инициализировано.");
    }
}

void MainWindow::on_printReversePushButton_clicked()
{
    if (myTree)
        ui->printerTextBrowser->setText(QString::fromStdString(myTree->printInfoReverse()));
    else {
        QMessageBox::warning(this, "Ошибка!", "Дерево не инициализировано.");
    }
}

void MainWindow::on_pushButton_clicked()
{
    if (myTree)
        ui->printerTextBrowser->setText(QString::fromStdString(myTree->printInfoByKey()));
    else {
        QMessageBox::warning(this, "Ошибка!", "Дерево не инициализировано.");
    }
}

void MainWindow::on_individualTaskButton_clicked()
{
    if (myTree)
        QMessageBox::information(this, "Результат", QString::fromStdString("Число узлов с двумя дочерьми: " + std::to_string(myTree->countNodesWithTwoAncestors())));
    else {
        QMessageBox::warning(this, "Ошибка!", "Дерево не инициализировано.");
    }
}
