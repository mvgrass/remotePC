#ifndef LANCONNECTIONMANAGER_H
#define LANCONNECTIONMANAGER_H

#include <QObject>
#include <QTcpServer>
#include <QTcpSocket>

class LanConnectionManager : public QObject
{
    Q_OBJECT
public:
    explicit LanConnectionManager(const quint32 port = 23496,
                                  const QString& approvedCode = "",
                                  QObject *parent = nullptr);
    ~LanConnectionManager();

signals:
    void newUser(const QString&, qint32 socketId);
    void newCommand(QByteArray&);
    void userDiconnected(qint32 socketId);

public slots:
    void start();
    void newConnection();
    void disconnectClient();
    void readData();
    //void
    //void deleteUser(const QString&);

private:
    QTcpServer* tcpServer;
    QHash<int, QTcpSocket*> unproovedClients;
    QHash<int, QTcpSocket*> proovedClients;
    QHash<int, QByteArray*> buffers;
    QHash<int, quint32> sizes;

    QString approvedCode;
    quint32 PORT;

};

#endif // LANCONNECTIONMANAGER_H
