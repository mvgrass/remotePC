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

    initializeConnectionTable(QStringList()<<"Socket ID"<<"Name"<<"IP addres"<<"Disconnect");

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
    lanConnectionManager = new LanConnectionManager(ui->spinBox_2->value(),
                                                    ui->lineEdit->text());

    connect(network_thread, SIGNAL(started()), lanConnectionManager, SLOT(start()));
    connect(network_thread, SIGNAL(finished()), lanConnectionManager, SLOT(deleteLater()));

    connect(lanConnectionManager, SIGNAL(newUser(const QString&, void*, const QString&)),
            this, SLOT(newConnection(const QString&, void*, const QString&)));

    connect(lanConnectionManager, SIGNAL(userDiconnected(void*)),
            this, SLOT(disconnected(void*)));

    lanConnectionManager->moveToThread(network_thread);
    network_thread->start();


    /*
     * Initializing CommandManager
     */
    command_thread = new QThread;
    commandManager = new CommandManager();
    connect(lanConnectionManager, SIGNAL(newCommand(const QByteArray&)),
            commandManager, SLOT(parseCommand(const QByteArray&)));

    commandManager->moveToThread(command_thread);
    command_thread->start();

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

void MainWindow::newConnection(const QString& name, void* socketID, const QString& ip)
{
    ui->tableConnectedDevices->insertRow(ui->tableConnectedDevices->rowCount());

    ui->tableConnectedDevices->setItem(ui->tableConnectedDevices->rowCount()-1,
                                       0, new QTableWidgetItem(QString::number((long long)socketID)));
    ui->tableConnectedDevices->setItem(ui->tableConnectedDevices->rowCount()-1,
                                       1, new QTableWidgetItem(name));
    ui->tableConnectedDevices->setItem(ui->tableConnectedDevices->rowCount()-1,
                                       2, new QTableWidgetItem(ip));

    QWidget* pWidget = new QWidget();
    QPushButton* btnDelete = new QPushButton();
    btnDelete->setText("Delete");
    QHBoxLayout* pLayout = new QHBoxLayout(pWidget);
    pLayout->addWidget(btnDelete);
    pLayout->setAlignment(Qt::AlignCenter);
    pLayout->setContentsMargins(0, 0, 0, 0);
    pWidget->setLayout(pLayout);

    ui->tableConnectedDevices->setCellWidget(ui->tableConnectedDevices->rowCount()-1,
                                             3, pWidget);

}

void MainWindow::disconnected(void* socketID)
{
    for(int i = 0;i<ui->tableConnectedDevices->rowCount(); i++)
        if(ui->tableConnectedDevices->item(i,0)->text().toInt() == (long long)socketID){
            ui->tableConnectedDevices->removeRow(i);
            return;
        }
}

void MainWindow::initializeConnectionTable(const QStringList & headers)
{
    ui->tableConnectedDevices->setColumnCount(headers.size());
    ui->tableConnectedDevices->setShowGrid(true);

    ui->tableConnectedDevices->setSelectionMode(QAbstractItemView::SingleSelection);
    ui->tableConnectedDevices->setSelectionBehavior(QAbstractItemView::SelectRows);

    ui->tableConnectedDevices->setHorizontalHeaderLabels(headers);
    ui->tableConnectedDevices->horizontalHeader()->setStretchLastSection(true);
    ui->tableConnectedDevices->hideColumn(0);
}

MainWindow::~MainWindow()
{
    network_thread->terminate();
    delete network_thread;
    delete lanConnectionManager;

    command_thread->terminate();
    delete command_thread;
    delete commandManager;

    delete ui;
}

void MainWindow::closeEvent(QCloseEvent* event)
{
    if(this->isVisible()){
        event->ignore();
        this->hide();
    }
}
