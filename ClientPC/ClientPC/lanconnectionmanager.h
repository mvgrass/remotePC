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
    void newUser(const QString&, void*, const QString&);
    void newCommand(const QByteArray);
    void userDiconnected(void* socketId);

public slots:
    void start();
    void newConnection();
    void disconnectClient();
    void readData();
    //void
    //void deleteUser(const QString&);

private:
    QTcpServer* tcpServer;
    QSet<QTcpSocket*> unproovedClients;
    QSet<QTcpSocket*> proovedClients;
    QHash<QTcpSocket*, QByteArray*> inBuffers;
    QHash<QTcpSocket*, quint16> inSizes;

    QString approvedCode;
    quint32 PORT;

};

#endif // LANCONNECTIONMANAGER_H
