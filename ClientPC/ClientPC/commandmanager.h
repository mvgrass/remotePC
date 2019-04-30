#ifndef COMMANDMANAGER_H
#define COMMANDMANAGER_H

#include <QObject>
#include "virtualmouse.h"

class CommandManager : public QObject
{
    Q_OBJECT
public:
    explicit CommandManager(QObject *parent = nullptr);

signals:

public slots:
    void parseCommand(const QByteArray&);

private:
    VirtualMouse* mouse;
};

#endif // COMMANDMANAGER_H
