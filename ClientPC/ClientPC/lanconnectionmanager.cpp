#include "lanconnectionmanager.h"

LanConnectionManager::LanConnectionManager(const quint32 port,
                                           const QString& approvedCode,
                                           QObject *parent)
    : QObject(parent),
      PORT(port),
      approvedCode(approvedCode)
{

}

LanConnectionManager::~LanConnectionManager()
{
    QTcpSocket* client;
    foreach(client, proovedClients)
        client->close();

    foreach(client, unproovedClients)
        client->close();

    if(this->tcpServer!=nullptr)
        delete tcpServer;
}

void LanConnectionManager::start()
{
    this->tcpServer = new QTcpServer();
    this->tcpServer->listen(QHostAddress::Any, PORT);

    connect(tcpServer, SIGNAL(newConnection()), this, SLOT(newConnection()));
}

void LanConnectionManager::newConnection()
{
    QTcpSocket* client = this->tcpServer->nextPendingConnection();

    unproovedClients[client->socketDescriptor()] = client;

    if(buffers.find(client->socketDescriptor())==buffers.end()){
        buffers[client->socketDescriptor()] = new QByteArray();
        sizes[client->socketDescriptor()] = 0;
    }

    buffers[client->socketDescriptor()]->clear();

    connect(client, SIGNAL(readyRead()), this, SLOT(readData()));
    connect(client, SIGNAL(disconnected()), this, SLOT(disconnectUClient()));
}

void LanConnectionManager::disconnectClient()
{
    QTcpSocket* disClient= (QTcpSocket*)sender();
    unproovedClients.remove(disClient->socketDescriptor());
    proovedClients.remove(disClient->socketDescriptor());

    delete buffers[disClient->socketDescriptor()];

    buffers.remove(disClient->socketDescriptor());
    sizes.remove(disClient->socketDescriptor());

    emit userDiconnected(disClient->socketDescriptor());

    delete disClient;
}

void LanConnectionManager::readData()
{
    QTcpSocket* socket = (QTcpSocket*)sender();

    qint32 socketID = socket->socketDescriptor();

    if(unproovedClients.find(socket->socketDescriptor())!=unproovedClients.end()){

        emit newUser("jshajsh", socketID);
    }else{
        if(sizes[socketID]==0){
            //sizes[socketID] = socket->read()
        }
    }
}


