#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "networkallocation.h"
#include <QMenu>
#include <QAction>
#include <QTimer>

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    trayIcon = new QSystemTrayIcon(this);
    QMenu* trayMenu = new QMenu(this);
    QAction* exitAction = new QAction("Exit", this);
    connect(exitAction, SIGNAL(triggered()), this, SLOT(quitApp()));
    connect(trayIcon, SIGNAL(activated(QSystemTrayIcon::ActivationReason)),
            this, SLOT(iconActivated(QSystemTrayIcon::ActivationReason)));


    trayMenu->addAction(exitAction);
    trayIcon->setIcon(QIcon(":images/transfer-from-computer-to-phone.svg"));
    trayIcon->setContextMenu(trayMenu);
    trayIcon->show();

    initializeServices();
}

void MainWindow::initializeServices(){

    /*
     * Initializeng UDP broadcast sender of current IP
     */
    this->locationService = new NetWorkAllocation();
    QTimer* tmr = new QTimer(this);
    tmr->setInterval(5000);
    connect(tmr, SIGNAL(timeout()), locationService, SLOT(run()));
    tmr->start();

    /*
     * Initializeng TCP server for receiving remote commands
     */
    network_thread = new QThread;
    lanConnectionManager = new LanConnectionManager();

    connect(network_thread, SIGNAL(started()), lanConnectionManager, SLOT(start()));
    connect(network_thread, SIGNAL(finished()), lanConnectionManager, SLOT(deleteLater()));

    lanConnectionManager->moveToThread(network_thread);
    network_thread->start();

}

void MainWindow::quitApp()
{
    qApp->quit();
}

void MainWindow::iconActivated(QSystemTrayIcon::ActivationReason reason)
{
    switch (reason) {
    case QSystemTrayIcon::DoubleClick:
        this->show();
        break;
    default:
        break;
    }
}

void MainWindow::newConnection(const QString &)
{

}

void MainWindow::disconnected(const QString &)
{

}

MainWindow::~MainWindow()
{
    network_thread->terminate();
    delete network_thread;
    delete ui;
}

void MainWindow::closeEvent(QCloseEvent* event)
{
    if(this->isVisible()){
        event->ignore();
        this->hide();
    }
}
