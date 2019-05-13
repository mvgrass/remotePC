#include "lanconnectionmanager.h"
#include <QDataStream>

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


    unproovedClients.insert(client);
    proovedClients.remove(client);

    if(inBuffers.find(client)==inBuffers.end()){
        inBuffers[client] = new QByteArray();
        inSizes[client] = 0;
    }

    inBuffers[client]->clear();

    /*
    if(outBuffers.find(client)==outBuffers.end()){
        outBuffers[client->socketDescriptor()] = new QByteArray();
        outSizes[client->socketDescriptor()] = 0;
    }

    outBuffers[client->socketDescriptor()]->clear();
    */

    connect(client, SIGNAL(readyRead()), this, SLOT(readData()));
    connect(client, SIGNAL(disconnected()), this, SLOT(disconnectClient()));
}

void LanConnectionManager::disconnectClient()
{
    QTcpSocket* disClient= (QTcpSocket*)sender();

    unproovedClients.remove(disClient);
    proovedClients.remove(disClient);

    delete inBuffers[disClient];

    inBuffers.remove(disClient);
    inSizes.remove(disClient);

    emit userDiconnected(disClient);

    disClient->deleteLater();
}

void LanConnectionManager::readData()
{
    QTcpSocket* socket = (QTcpSocket*)sender();


    QDataStream in(socket);

    if(unproovedClients.find(socket)!=unproovedClients.end()){
        if(inSizes[socket]==0){
            if(socket->bytesAvailable()<2)
                return;
            else
                in>>inSizes[socket];

        }

        char temp[inSizes[socket]];
        int readBytes = in.readRawData(temp, inSizes[socket]);

        inBuffers[socket]->append(temp, readBytes);
        inSizes[socket]-=readBytes;

        if(inSizes[socket]==0){
            QByteArray answer;
            if(QString::fromUtf8(*inBuffers[socket])==approvedCode){
                unproovedClients.remove(socket);
                proovedClients.insert(socket);
                answer.append((char)1);

                emit newUser(socket->peerName(),
                             socket,
                             socket->peerAddress().toString());

            }else{
                answer.append((char)0);
            }

            socket->write(answer);

            inBuffers[socket]->clear();
        }

    }else{
        while(socket->bytesAvailable()>0){
            if(inSizes[socket]==0){
                if(socket->bytesAvailable()<2)
                    return;
                else{
                    in>>inSizes[socket];
                }

            }

            char temp[inSizes[socket]];

            int readBytes = in.readRawData(temp, inSizes[socket]);

            inBuffers[socket]->append(temp, readBytes);

            inSizes[socket]-=readBytes;

            if(inSizes[socket]==0){
                emit newCommand(*inBuffers[socket]);

                inBuffers[socket]->clear();
            }
        }
    }
    
}


