#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QThread>
#include <QSystemTrayIcon>
#include <QCloseEvent>
#include "networkallocation.h"
#include "lanconnectionmanager.h"
#include "commandmanager.h"

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

protected:
    void closeEvent(QCloseEvent* event);

private:
    Ui::MainWindow *ui;
    QSystemTrayIcon* trayIcon;

    NetWorkAllocation* locationService;

    QThread* network_thread;
    LanConnectionManager* lanConnectionManager;

    QThread* command_thread;
    CommandManager* commandManager;

    void initializeServices();

private slots:
    void quitApp();
    void iconActivated(QSystemTrayIcon::ActivationReason);
    void newConnection(const QString&, void*, const QString&);
    void disconnected(void*);
    void initializeConnectionTable(const QStringList&);
};

#endif // MAINWINDOW_H
