#ifndef LANCONNECTIONMANAGER_H
#define LANCONNECTIONMANAGER_H

#include <QObject>
#include <QTcpServer>
#include <QTcpSocket>

class LanConnectionManager : public QObject
{
    Q_OBJECT
public:
    explicit LanConnectionManager(QObject *parent = nullptr);
    ~LanConnectionManager();

signals:
    void newUser(const QString&);

public slots:
    void start();
    void newConnection();
    void disconnectClient();
    void readData();
    //void deleteUser(const QString&);

private:
    QTcpServer* tcpServer;
    QMap<int, QTcpSocket*> clients;
};

#endif // LANCONNECTIONMANAGER_H
