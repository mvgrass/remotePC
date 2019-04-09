#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QThread>
#include <QSystemTrayIcon>
#include <QCloseEvent>
#include "networkallocation.h"
#include "lanconnectionmanager.h"

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
    QThread* network_thread;
    NetWorkAllocation* locationService;
    LanConnectionManager* lanConnectionManager;

    void initializeServices();

private slots:
    void quitApp();
    void iconActivated(QSystemTrayIcon::ActivationReason);
    void newConnection(const QString&);
    void disconnected(const QString&);
};

#endif // MAINWINDOW_H
