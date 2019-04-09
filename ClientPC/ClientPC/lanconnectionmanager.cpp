#include "lanconnectionmanager.h"

LanConnectionManager::LanConnectionManager(QObject *parent) : QObject(parent)
{

}

LanConnectionManager::~LanConnectionManager()
{
    QTcpSocket* client;
    foreach(client, clients)
        client->close();

    if(this->tcpServer!=nullptr)
        delete tcpServer;
}

void LanConnectionManager::start()
{
    this->tcpServer = new QTcpServer();
    this->tcpServer->listen(QHostAddress::Any, 11498);

    connect(tcpServer, SIGNAL(newConnection()), this, SLOT(newConnection()));
}

void LanConnectionManager::newConnection()
{
    QTcpSocket* client = this->tcpServer->nextPendingConnection();

    clients[client->socketDescriptor()] = client;
    connect(client, SIGNAL(readyRead()), this, SLOT(readData()));
    connect(client, SIGNAL(disconnected()), this, SLOT(disconnectUClient()));
}

void LanConnectionManager::disconnectClient()
{
    QTcpSocket* disClient= (QTcpSocket*)sender();
    clients.remove(disClient->socketDescriptor());
    delete disClient;
}

void LanConnectionManager::readData()
{

}


